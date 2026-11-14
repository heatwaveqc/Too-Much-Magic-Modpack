#!/usr/bin/env bash
# Loader-only by default; set SMOKE_WITH_MODS=1 for hash-verified packwiz mod jars.
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
if [[ "${SMOKE_WITH_MODS:-0}" == "1" ]]; then
  python3 "$ROOT/scripts/download_mods.py" "$WORK_DIR/mods"
  cp -a "$ROOT/kubejs" "$WORK_DIR/kubejs"
  if [[ -d "$ROOT/config" ]]; then
    mkdir -p "$WORK_DIR/config"
    cp -a "$ROOT/config/." "$WORK_DIR/config/"
    if [[ -d "$WORK_DIR/config/ftbquests/quests/chapters" ]]; then
      echo "FTB Quests chapter files copied: $(find "$WORK_DIR/config/ftbquests/quests/chapters" -maxdepth 1 -name '*.snbt' -type f | wc -l)"
    fi
  fi
  if [[ "${SMOKE_AU_FULL_RECIPE_DEBUG:-0}" == "1" ]]; then
    # Diagnostic only: full recipe before/after, AU's dedup decisions and
    # transformations. These can be large: never ship enabled to players.
    mkdir -p "$WORK_DIR/config/almostunified"
    cat > "$WORK_DIR/config/almostunified/debug.json" <<'JSON'
{
  "dump_tags": true,
  "dump_overview": true,
  "dump_recipes": true,
  "dump_unification": true,
  "dump_duplicates": true,
  "log_invalid_tags": true,
  "cache_recipe_conditions": true
}
JSON
  fi
  if [[ "${SMOKE_AU_DEBUG:-0}" == "1" ]]; then
    # CI-only registry audit; never ship generated debug dumps in the Prism instance.
    mkdir -p "$WORK_DIR/config/almostunified"
    cat > "$WORK_DIR/config/almostunified/debug.json" <<'JSON'
{
  "dump_tags": true,
  "dump_overview": true,
  "dump_recipes": false,
  "dump_unification": false,
  "dump_duplicates": false,
  "log_invalid_tags": true,
  "cache_recipe_conditions": true
}
JSON
  fi
  echo "Starting hash-pinned MODDED dedicated server"
else
  echo "Starting loader-only dedicated server (no mods installed)"
fi
mkfifo cmd.fifo
# Stop as soon as the server becomes ready, instead of needlessly idling for minutes.
# A time limit remains to avoid indefinite hangs on a broken startup.
(
  for ((elapsed=0; elapsed<SMOKE_TIMEOUT_SECONDS; elapsed++)); do
    if grep -Eq 'Done \(' server.log 2>/dev/null; then
      if [[ "${SMOKE_POLYMORPH_AUDIT:-0}" == "1" ]]; then
        echo "Polymorph audit: issuing 'polymorph conflicts' on modded server" >&2
        # Console commands have NO leading slash.
        printf 'polymorph conflicts\n'
        # Polymorph runs asynchronously; wait until it reports a result.
        for ((waited=0; waited<180; waited++)); do
          if [[ -s logs/polymorph-conflicts.log ]] || grep -Eq 'Found [0-9]+ potential conflicts' server.log 2>/dev/null; then
            echo "Polymorph audit finished after ${waited}s" >&2
            sleep 2
            break
          fi
          sleep 1
        done
      fi
      printf 'stop\n'
      exit 0
    fi
    sleep 1
  done
  printf 'stop\n'
) > cmd.fifo &
STOPPER=$!
set +e
timeout "$((SMOKE_TIMEOUT_SECONDS + 60))" bash ./run.sh nogui < cmd.fifo > server.log 2>&1
STATUS=$?
set -e
kill "$STOPPER" 2>/dev/null || true
if [[ "$STATUS" -eq 0 ]] && grep -Eq 'Done \([0-9.,]+s\)!|Done \(' server.log; then
  if [[ "${SMOKE_POLYMORPH_AUDIT:-0}" == "1" ]] && [[ ! -s logs/polymorph-conflicts.log ]] && ! grep -Eq 'Found 0 potential conflicts' server.log; then
    echo "FAIL: Polymorph diagnostic did not finish" >&2
    tail -80 server.log
    exit 1
  fi
  echo "PASS: dedicated server reached Done and shut down cleanly (modded=${SMOKE_WITH_MODS:-0}; exit=$STATUS)"
  exit 0
fi
tail -100 server.log
echo "FAIL: loader did not report ready (exit=$STATUS)"
exit 1
