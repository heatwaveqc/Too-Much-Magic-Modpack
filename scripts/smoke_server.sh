#!/usr/bin/env bash
# This performs a *loader-only* smoke. It does not install mods from the manifest.
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
VERSION="$(python3 -c 'import tomllib,pathlib; p=tomllib.loads((pathlib.Path("'"$ROOT"'")/"pack.toml").read_text()); print(p["versions"]["neoforge"])')"
: "${SMOKE_TIMEOUT_SECONDS:=240}"
: "${WORK_DIR:=$(mktemp -d)}"
mkdir -p "$WORK_DIR"
cd "$WORK_DIR"
test ! -e world || { echo "Refusing to overwrite an existing world"; exit 2; }
URL="https://maven.neoforged.net/releases/net/neoforged/neoforge/${VERSION}/neoforge-${VERSION}-installer.jar"
echo "Downloading exact NeoForge installer: $URL"
curl -fL --retry 3 --connect-timeout 20 -o installer.jar "$URL"
java -jar installer.jar --installServer
printf 'eula=true\n' > eula.txt  # CI smoke only, disposable world
echo "Starting loader-only dedicated server (no mods pinned)"
mkfifo cmd.fifo
# Ensure an open writer while server runs, and stop after the timeout window.
( sleep "$SMOKE_TIMEOUT_SECONDS"; printf 'stop\n' ) > cmd.fifo &
STOPPER=$!
set +e
timeout "$((SMOKE_TIMEOUT_SECONDS + 60))" bash ./run.sh nogui < cmd.fifo > server.log 2>&1
STATUS=$?
set -e
kill "$STOPPER" 2>/dev/null || true
if grep -Eq 'Done \([0-9.,]+s\)!|Done \(' server.log; then
  echo "PASS: loader-only dedicated server reached Done (exit=$STATUS)"
  exit 0
fi
tail -100 server.log
echo "FAIL: loader did not report ready (exit=$STATUS)"
exit 1
