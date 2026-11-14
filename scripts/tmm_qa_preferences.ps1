# Too Much Magic DEV-ONLY Prism preferences helper.
# This never contacts GitHub, reads worlds/saves, or touches packwiz/other mods.
# The Windows QA ZIP wires Apply before game startup and Save after game exit.
param(
    [ValidateSet("apply", "save", "import", "status")]
    [string] $Mode = "apply",
    [string] $OptionsPath = ""
)

$ErrorActionPreference = "Stop"
Set-StrictMode -Version Latest
$ExpectedVersion = "3955" # Minecraft Java 1.21.1 ONLY

if ($env:TMM_QA_PROFILE_DIR) {
    # Intended for CI and advanced testers. Never embedded in distributed files.
    $ProfileDirectory = $env:TMM_QA_PROFILE_DIR
} elseif ($env:LOCALAPPDATA) {
    $ProfileDirectory = Join-Path $env:LOCALAPPDATA "TooMuchMagic\TestPreferences"
} else {
    throw "LOCALAPPDATA is missing. This QA auto-sync is supported on Windows only."
}
$ProfileFile = Join-Path $ProfileDirectory "options-1.21.1.txt"
$Utf8 = New-Object System.Text.UTF8Encoding($false)

function IsPersonalOption([string] $Name) {
    # Bindings from any installed mod, including absent/replaced mods, are safe
    # to carry as text; Minecraft ignores mappings for mods not currently loaded.
    if ($Name -match '^key_[A-Za-z0-9._/\-]+$') { return $true }
    if ($Name -match '^soundCategory_[a-z_]+$') { return $true }
    return @(
        "guiScale", "chatScale", "chatWidth", "chatHeightFocused",
        "chatHeightUnfocused", "chatOpacity", "chatLineSpacing",
        "textBackgroundOpacity", "chatDelay", "subtitles",
        "directionalAudio", "narrator"
    ) -contains $Name
}

function ReadLines([string] $Path) {
    if (-not (Test-Path -LiteralPath $Path -PathType Leaf)) { return $null }
    $info = Get-Item -LiteralPath $Path
    if ($info.Length -gt 131072) { throw "Refusing oversized options file $Path" }
    $lines = @([System.IO.File]::ReadAllLines($Path, [System.Text.Encoding]::UTF8))
    # Do not mix newer/older Minecraft's option formats into a 1.21.1 instance.
    if (-not ($lines -contains "version:$ExpectedVersion")) {
        throw "Wrong or absent Minecraft 1.21.1 options version in $Path (need version:$ExpectedVersion)."
    }
    return ,$lines
}

function SelectedOptions([string[]] $Lines) {
    $prefs = @{}
    foreach ($line in $Lines) {
        $separator = $line.IndexOf(":")
        if ($separator -lt 1) { continue }
        $name = $line.Substring(0, $separator)
        $value = $line.Substring($separator + 1)
        if ((IsPersonalOption $name) -and $value.Length -le 1024) {
            $prefs[$name] = $value
        }
    }
    return $prefs
}

function AtomicWrite([string] $Path, [string[]] $Lines) {
    $folder = Split-Path -Parent $Path
    if (-not (Test-Path -LiteralPath $folder)) {
        New-Item -ItemType Directory -Path $folder -Force | Out-Null
    }
    $tmp = Join-Path $folder ([System.IO.Path]::GetRandomFileName())
    try {
        [System.IO.File]::WriteAllLines($tmp, $Lines, $Utf8)
        Move-Item -LiteralPath $tmp -Destination $Path -Force
    } finally {
        if (Test-Path -LiteralPath $tmp) {
            Remove-Item -LiteralPath $tmp -Force
        }
    }
}

function SaveProfile([string] $From) {
    $lines = ReadLines $From
    if ($null -eq $lines) { throw "Minecraft options not found: $From" }
    $prefs = SelectedOptions $lines
    if ($prefs.Count -lt 2) {
        throw "Refusing to overwrite saved preferences with an empty/invalid options file."
    }
    $output = @("version:$ExpectedVersion")
    foreach ($key in ($prefs.Keys | Sort-Object)) {
        $output += ($key + ":" + $prefs[$key])
    }
    AtomicWrite $ProfileFile $output
    Write-Host "[TMM QA] Saved $($prefs.Count) personal keys locally in $ProfileFile"
}

try {
    if ($Mode -eq "status") {
        Write-Host "[TMM QA] Local profile: $ProfileFile"
        if (Test-Path -LiteralPath $ProfileFile) {
            $saved = SelectedOptions (ReadLines $ProfileFile)
            Write-Host "[TMM QA] $($saved.Count) saved settings. No data is uploaded."
        } else {
            Write-Host "[TMM QA] No shared profile yet. Your first game exit will create one."
        }
        exit 0
    }

    if ($Mode -eq "import" -and [string]::IsNullOrWhiteSpace($OptionsPath)) {
        # Explicit, one-time opt-in for preferences from an OLDER Prism instance.
        Add-Type -AssemblyName System.Windows.Forms
        $picker = New-Object System.Windows.Forms.OpenFileDialog
        $picker.Title = "Choose the OLD Prism instance's minecraft\options.txt"
        $picker.Filter = "Minecraft options file (options.txt)|options.txt|All files (*.*)|*.*"
        if ($picker.ShowDialog() -ne [System.Windows.Forms.DialogResult]::OK) {
            Write-Host "[TMM QA] Import cancelled without changing your profile."
            exit 0
        }
        $OptionsPath = $picker.FileName
    }

    if ([string]::IsNullOrWhiteSpace($OptionsPath)) {
        throw "Specify -OptionsPath for $Mode."
    }

    if ($Mode -eq "save" -or $Mode -eq "import") {
        SaveProfile $OptionsPath
        exit 0
    }

    # Apply: merge ONLY personal options. Preserve all target flags, mod data,
    # pack resources, last server, graphics settings and unknown keys.
    if (-not (Test-Path -LiteralPath $ProfileFile -PathType Leaf)) {
        Write-Host "[TMM QA] No saved profile. Using one-time packaged defaults."
        exit 0
    }
    $saved = SelectedOptions (ReadLines $ProfileFile)
    $current = ReadLines $OptionsPath
    if ($null -eq $current) {
        # A fresh import should include packaged defaults; if not, keep the
        # standard Minecraft startup path instead of fabricating a full file.
        Write-Host "[TMM QA] No local options.txt yet; Minecraft will create it."
        exit 0
    }
    $updated = New-Object 'System.Collections.Generic.List[string]'
    $used = @{}
    foreach ($line in $current) {
        $separator = $line.IndexOf(":")
        if ($separator -ge 1) {
            $name = $line.Substring(0, $separator)
            if ((IsPersonalOption $name) -and $saved.ContainsKey($name)) {
                $updated.Add($name + ":" + $saved[$name])
                $used[$name] = $true
                continue
            }
        }
        $updated.Add($line)
    }
    foreach ($key in ($saved.Keys | Sort-Object)) {
        if (-not $used.ContainsKey($key)) {
            $updated.Add($key + ":" + $saved[$key])
        }
    }
    if ((@($current) -join "\n") -ne (@($updated) -join "\n")) {
        AtomicWrite $OptionsPath $updated.ToArray()
    }
    Write-Host "[TMM QA] Applied $($saved.Count) saved preferences; other settings untouched."
} catch {
    # A sync error should NEVER abort packwiz or stop the game from starting.
    # This is a convenience helper, not an integrity or build-enforcement step.
    Write-Warning "[TMM QA] Preference sync skipped: $($_.Exception.Message)"
    exit 0
}
