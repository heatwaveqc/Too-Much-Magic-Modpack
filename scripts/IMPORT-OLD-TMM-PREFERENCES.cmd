@echo off
rem One-time optional import of your existing Prism instance's options.txt.
rem Only keybindings, GUI/chat sizing and volume preferences are retained.
powershell.exe -NoProfile -ExecutionPolicy Bypass -STA -File "%~dp0tmm-qa-preferences.ps1" -Mode import
echo.
echo Your preferences are saved locally and will follow future TMM test builds.
pause
