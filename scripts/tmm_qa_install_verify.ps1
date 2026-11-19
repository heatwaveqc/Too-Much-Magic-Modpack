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
    & $JavaPath -jar 'packwiz-installer-bootstrap.jar' -s client $PackUrl
    if ($LASTEXITCODE -ne 0) { throw "Packwiz installer failed ($LASTEXITCODE)" }
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
