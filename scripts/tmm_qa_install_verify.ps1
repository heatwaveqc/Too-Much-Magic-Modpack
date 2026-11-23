# UNSTABLE QA only. Fail closed after Packwiz, before Minecraft launches.
param(
    [Parameter(Mandatory=$true)][string]$JavaPath,
    [Parameter(Mandatory=$true)][string]$PackUrl,
    [Parameter(Mandatory=$true)][string]$InstancePath
)
$ErrorActionPreference = 'Stop'
Push-Location -LiteralPath $InstancePath
try {
    $expected = Get-Content -Raw -LiteralPath 'tmm-qa-expected-mods.json' | ConvertFrom-Json
    if ($expected.schema -ne 1 -or $expected.source_commit -cnotmatch '^[0-9a-f]{40}$') {
        throw 'Invalid QA inventory manifest'
    }
    $exact = "https://raw.githubusercontent.com/heatwaveqc/Too-Much-Magic-Modpack/$($expected.source_commit)/pack.toml"
    if ($PackUrl -cne $exact) { throw 'QA pack source differs from inventory manifest' }
    # Prism passes javaw.exe on Windows. Invoking a GUI executable with PowerShell's
    # call operator can leave $LASTEXITCODE unset even when the process succeeds.
    # Start-Process -Wait/-PassThru waits for the actual child to finish and gives
    # us its exit status for either java.exe or javaw.exe. Keep this fail-closed.
    $installer = $null
    try {
        $installer = Start-Process -FilePath $JavaPath -WorkingDirectory $InstancePath `
            -ArgumentList @('-jar', 'packwiz-installer-bootstrap.jar', '-s', 'client', $PackUrl) `
            -Wait -PassThru -ErrorAction Stop
    } catch {
        throw "Could not start Packwiz installer: $($_.Exception.Message)"
    }
    try {
        if ($null -eq $installer -or -not $installer.HasExited) {
            throw 'Packwiz installer completion could not be verified'
        }
        $installerExitCode = $installer.ExitCode
        if ($null -eq $installerExitCode) {
            throw 'Packwiz installer returned no verified exit code'
        }
        if ($installerExitCode -ne 0) {
            throw "Packwiz installer failed ($installerExitCode)"
        }
    } finally {
        if ($null -ne $installer) { $installer.Dispose() }
    }
    $wanted = @{}
    foreach ($row in $expected.mods) {
        if ($row.filename -notmatch '^[^/\\:\r\n]+\.jar$' -or $wanted.ContainsKey($row.filename)) {
            throw "Unsafe or duplicate expected JAR: $($row.filename)"
        }
        $wanted[$row.filename] = $row
    }
    if ($wanted.Count -eq 0) { throw 'No expected QA JARs' }
    $actual = @(Get-ChildItem -LiteralPath 'mods' -File | Where-Object { $_.Extension -ieq '.jar' })
    $found = @{}
    foreach ($file in $actual) {
        if (!$wanted.ContainsKey($file.Name) -or $found.ContainsKey($file.Name)) { throw "Unexpected JAR: $($file.Name)" }
        $row = $wanted[$file.Name]
        $value = (Get-FileHash -LiteralPath $file.FullName -Algorithm $row.algorithm.ToUpperInvariant()).Hash
        if ($value -ine $row.hash) { throw "Wrong JAR bytes: $($file.Name)" }
        $found[$file.Name] = $true
    }
    $missing = @($wanted.Keys | Where-Object { !$found.ContainsKey($_) })
    if ($missing.Count -ne 0) { throw "Missing QA mods: $($missing -join ', ')" }
    Write-Host "PASS: $($found.Count) exact client QA JARs, including first-party modules."
} finally {
    Pop-Location
}
