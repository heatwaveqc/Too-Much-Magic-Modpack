# Execute on Windows PowerShell 5.1, which Prism uses in the QA instance.
$ErrorActionPreference = "Stop"
Set-StrictMode -Version Latest
$root = (Resolve-Path (Join-Path $PSScriptRoot "..")).Path
$script = Join-Path $root "scripts\tmm_qa_preferences.ps1"
$testing = Join-Path $env:TEMP ("tmm-prism-prefs-" + [guid]::NewGuid().ToString("N"))
$env:TMM_QA_PROFILE_DIR = Join-Path $testing "personal"
New-Item -ItemType Directory -Path $testing | Out-Null
$old = Join-Path $testing "old-options.txt"
$new = Join-Path $testing "new-options.txt"
try {
    @(
      "version:3955",
      "key_key.inventory:key.keyboard.r",
      "key_key.jump:key.keyboard.space",
      "key_example_mod.blast:key.keyboard.f",
      "guiScale:4",
      "chatScale:0.7",
      "narrator:2",
      "onboardAccessibility:true",
      "soundCategory_master:0.31",
      "soundCategory_music:0.19",
      'resourcePacks:["secret resourcepack"]',
      "lastServer:PRIVATE_SERVER_ADDRESS",
      "renderDistance:32"
    ) | Set-Content -LiteralPath $old -Encoding UTF8
    & powershell.exe -NoProfile -ExecutionPolicy Bypass -File $script -Mode import -OptionsPath $old
    if ($LASTEXITCODE -ne 0) { throw "Preferences import exited nonzero" }
    $savedPath = Join-Path $env:TMM_QA_PROFILE_DIR "options-1.21.1.txt"
    $saved = Get-Content -LiteralPath $savedPath -Raw
    if ($saved -notmatch "key_example_mod.blast:key.keyboard.f") { throw "Mod key binding lost" }
    if ($saved -notmatch "guiScale:4") { throw "GUI scale lost" }
    if ($saved -notmatch "soundCategory_master:0.31") { throw "Sound setting lost" }
    if ($saved -match "resourcePacks|lastServer|renderDistance|PRIVATE_SERVER_ADDRESS") {
       throw "Sensitive or unrelated content was imported into personal profile"
    }
    @(
      "version:3955",
      "guiScale:3",
      "onboardAccessibility:false",
      "soundCategory_master:0.55",
      "key_key.inventory:key.keyboard.e",
      'resourcePacks:["keep-new-pack"]',
      "renderDistance:12"
    ) | Set-Content -LiteralPath $new -Encoding UTF8
    & powershell.exe -NoProfile -ExecutionPolicy Bypass -File $script -Mode apply -OptionsPath $new
    $result = Get-Content -LiteralPath $new -Raw
    if ($result -notmatch "key_key.inventory:key.keyboard.r") { throw "Old key binding not restored" }
    if ($result -notmatch "key_example_mod.blast:key.keyboard.f") { throw "Custom mod key binding not appended" }
    if ($result -notmatch "narrator:2") { throw "Existing narrator preference was lost" }
    if ($result -notmatch "onboardAccessibility:false" -or $result -match "onboardAccessibility:true") { throw "Old onboarding flag overrode QA default" }
    if ($result -notmatch "guiScale:4") { throw "Old UI scale not restored" }
    if ($result -notmatch "soundCategory_master:0.31") { throw "Old volume not restored" }
    if ($result -notmatch 'resourcePacks:\["keep-new-pack"\]') { throw "New resource pack was modified" }
    if ($result -notmatch "renderDistance:12") { throw "Graphics options were modified" }
    if ($result -match "PRIVATE_SERVER_ADDRESS") { throw "Old server leaked into current instance" }

    # A player can change the imported options during the session;
    # post-exit must persist the new chosen setting for next build.
    $result = $result.Replace("guiScale:4", "guiScale:2")
    Set-Content -LiteralPath $new -Value $result -Encoding UTF8
    & powershell.exe -NoProfile -ExecutionPolicy Bypass -File $script -Mode save -OptionsPath $new
    if ((Get-Content $savedPath -Raw) -notmatch "guiScale:2") { throw "Post-exit preference saving failed" }

    # Wrong Minecraft version must NEVER replace saved 1.21.1 options.
    $bad = Join-Path $testing "bad.txt"
    @("version:4300", "guiScale:9", "soundCategory_master:1.0") | Set-Content -LiteralPath $bad -Encoding UTF8
    & powershell.exe -NoProfile -ExecutionPolicy Bypass -File $script -Mode import -OptionsPath $bad
    if ((Get-Content $savedPath -Raw) -match "guiScale:9") { throw "Wrong-version settings were accepted" }

    Write-Host "PASS: imported old controls; restored mod bindings, chat/UI/sound; protected non-personal data; saved changes; rejected wrong Minecraft version"
} finally {
    Remove-Item Env:TMM_QA_PROFILE_DIR -ErrorAction SilentlyContinue
    Remove-Item -LiteralPath $testing -Recurse -Force -ErrorAction SilentlyContinue
}
