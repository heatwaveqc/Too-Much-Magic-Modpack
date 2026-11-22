# Run under Windows PowerShell on Windows runner; never starts Minecraft or Packwiz.
# The tiny local Java fixture tests real java.exe and GUI javaw.exe completion.
$ErrorActionPreference = 'Stop'
if (-not $env:RUNNER_TEMP -or -not $env:JAVA_HOME) {
    throw 'RUNNER_TEMP and JAVA_HOME are required; test only in isolated Windows CI'
}
$repo = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
$verifier = Join-Path $repo 'scripts/tmm_qa_install_verify.ps1'
$root = Join-Path $env:RUNNER_TEMP ('TMM QA Wrapper With Spaces ' + [guid]::NewGuid().ToString('N'))
$java = Join-Path $env:JAVA_HOME 'bin/java.exe'
$javaw = Join-Path $env:JAVA_HOME 'bin/javaw.exe'
$javac = Join-Path $env:JAVA_HOME 'bin/javac.exe'
$jar = Join-Path $env:JAVA_HOME 'bin/jar.exe'
foreach ($exe in @($java, $javaw, $javac, $jar)) {
    if (-not (Test-Path -LiteralPath $exe -PathType Leaf)) {
        throw "JDK 21 executable missing: $exe"
    }
}
New-Item -ItemType Directory -Path $root -Force | Out-Null
$previousMode = $env:TMM_QA_FIXTURE_MODE
try {
    $src = @'
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
public final class FixtureMain {
    public static void main(String[] args) throws Exception {
        if (args.length != 3 || !args[0].equals("-s") || !args[1].equals("client") ||
                !args[2].startsWith("https://raw.githubusercontent.com/")) {
            System.exit(19);
        }
        Thread.sleep(750);
        String mode = System.getenv("TMM_QA_FIXTURE_MODE");
        if (!"skip".equals(mode)) {
            Files.createDirectories(Paths.get("mods"));
            Files.write(Paths.get("mods", "toy.jar"),
                "fixture-jar".getBytes(StandardCharsets.UTF_8));
        }
        if ("nonzero".equals(mode)) System.exit(17);
    }
}
'@
    [System.IO.File]::WriteAllText((Join-Path $root 'FixtureMain.java'), $src, (New-Object System.Text.UTF8Encoding($false)))
    & $javac -d $root (Join-Path $root 'FixtureMain.java')
    if ($LASTEXITCODE -ne 0) { throw 'Java fixture compilation failed' }
    Push-Location -LiteralPath $root
    try {
        & $jar --create --file 'packwiz-installer-bootstrap.jar' --main-class FixtureMain FixtureMain.class
        if ($LASTEXITCODE -ne 0) { throw 'Java fixture packaging failed' }
    } finally {
        Pop-Location
    }

    $sha = 'a' * 40
    $url = "https://raw.githubusercontent.com/heatwaveqc/Too-Much-Magic-Modpack/$sha/pack.toml"
    $toyHash = (Get-FileHash -LiteralPath (Join-Path $root 'packwiz-installer-bootstrap.jar') -Algorithm SHA256).Hash
    # The fixture's one mod JAR is a byte string, not a real/modded JAR.
    $hashObject = [System.Security.Cryptography.SHA256]::Create()
    try {
        $toyHash = ([BitConverter]::ToString($hashObject.ComputeHash([System.Text.Encoding]::UTF8.GetBytes('fixture-jar')))).Replace('-', '').ToLowerInvariant()
    } finally {
        $hashObject.Dispose()
    }
    $inventoryFile = Join-Path $root 'tmm-qa-expected-mods.json'
    $inventory = @{schema=1; source_commit=$sha; mods=@(@{filename='toy.jar';algorithm='sha256';hash=$toyHash})}
    function Write-Inventory {
        $inventory | ConvertTo-Json -Depth 6 | Set-Content -LiteralPath $inventoryFile -Encoding UTF8
    }
    function Clean-Mods {
        $dir = Join-Path $root 'mods'
        if (Test-Path -LiteralPath $dir) { Remove-Item -LiteralPath $dir -Recurse -Force }
        New-Item -ItemType Directory -Path $dir -Force | Out-Null
    }
    function Check-Case([string]$title, [string]$exe, [string]$mode, [bool]$shouldPass, [string]$message) {
        $env:TMM_QA_FIXTURE_MODE = $mode
        $passed = $false
        $failure = ''
        $watch = [System.Diagnostics.Stopwatch]::StartNew()
        try {
            & $verifier -JavaPath $exe -PackUrl $url -InstancePath $root | Out-Null
            $passed = $true
        } catch {
            $failure = $_.Exception.Message
        } finally {
            $watch.Stop()
        }
        if ($passed -ne $shouldPass) {
            throw "$title expected success=$shouldPass, observed success=$passed, exception=$failure"
        }
        if (-not $shouldPass -and -not $failure.Contains($message)) {
            throw "$title expected failure [$message], observed [$failure]"
        }
        if ($mode -eq 'success' -and $watch.ElapsedMilliseconds -lt 600) {
            throw "$title returned before the delayed Java child finished"
        }
        Write-Host "PASS: $title ($($watch.ElapsedMilliseconds) ms)"
    }
    Write-Inventory
    Clean-Mods
    Check-Case 'GUI javaw success, waits, instance path has spaces' $javaw 'success' $true ''
    Clean-Mods
    Check-Case 'Console java success, waits' $java 'success' $true ''
    Clean-Mods
    Check-Case 'GUI javaw nonzero must propagate actual exit status' $javaw 'nonzero' $false 'Packwiz installer failed (17)'
    Clean-Mods
    Check-Case 'Console java nonzero must propagate actual exit status' $java 'nonzero' $false 'Packwiz installer failed (17)'
    Clean-Mods
    Check-Case 'Successful javaw installer but missing mod fails closed' $javaw 'skip' $false 'Missing QA mods'
    Clean-Mods
    $inventory.mods[0].hash = '0' * 64
    Write-Inventory
    Check-Case 'Successful javaw installer but wrong hash fails closed' $javaw 'success' $false 'Wrong JAR bytes'
    $inventory.mods[0].hash = $toyHash
    Write-Inventory
    Clean-Mods
    $missing = Join-Path $root 'missing javaw.exe'
    Check-Case 'Invalid launcher path is a hard failure' $missing 'success' $false 'Could not start Packwiz installer'
    Write-Host 'PASS: 7 Windows native wrapper cases; no game, no network, no genuine Packwiz installer run'
} finally {
    $env:TMM_QA_FIXTURE_MODE = $previousMode
    if (Test-Path -LiteralPath $root) {
        Remove-Item -LiteralPath $root -Recurse -Force
    }
}
