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
  # Explicitly install every compiled first-party JAR; never report a bridge smoke
  # PASS if the declared JAR was missing, ignored, or not copied into mods/.
  EXTRA_JAR_LIST="${SMOKE_EXTRA_MOD_JARS:-${SMOKE_EXTRA_MOD_JAR:-}}"
  if [[ -n "$EXTRA_JAR_LIST" ]]; then
    IFS=':' read -r -a EXTRA_JARS <<< "$EXTRA_JAR_LIST"
    for EXTRA in "${EXTRA_JARS[@]}"; do
      [[ -f "$EXTRA" && "$EXTRA" == *.jar ]] || { echo "Missing or invalid first-party JAR: $EXTRA"; exit 4; }
      cp -v -- "$EXTRA" "$WORK_DIR/mods/$(basename "$EXTRA")"
      cmp -- "$EXTRA" "$WORK_DIR/mods/$(basename "$EXTRA")"
    done
    echo "Custom first-party modules installed: ${#EXTRA_JARS[@]}"
  fi
  cp -a "$ROOT/kubejs" "$WORK_DIR/kubejs"
  if [[ -d "$ROOT/config" ]]; then
    mkdir -p "$WORK_DIR/config"
    cp -a "$ROOT/config/." "$WORK_DIR/config/"
    if [[ -d "$WORK_DIR/config/ftbquests/quests/chapters" ]]; then
      echo "FTB Quests chapter files copied: $(find "$WORK_DIR/config/ftbquests/quests/chapters" -maxdepth 1 -name '*.snbt' -type f | wc -l)"
    fi
  fi
  if [[ -d "$ROOT/defaultconfigs" ]]; then
    mkdir -p "$WORK_DIR/defaultconfigs"
    cp -a "$ROOT/defaultconfigs/." "$WORK_DIR/defaultconfigs/"
  fi
  echo "Starting hash-pinned MODDED dedicated server"
else
  echo "Starting loader-only dedicated server (no mods installed)"
fi
# Every full modded QA that includes the Mahou KubeJS catalyst bridge must
# exercise an actual dropped-item entity tick. A plain server boot did not
# catch the reported world-start crash.
if [[ "${SMOKE_WITH_MODS:-0}" == "1" && -f "$WORK_DIR/kubejs/server_scripts/mahou_neo_vitae_catalyst_exchange.js" ]]; then
  SMOKE_ITEM_TICK_TEST=1
fi
mkfifo cmd.fifo
# Stop as soon as the server becomes ready, instead of needlessly idling for minutes.
# A time limit remains to avoid indefinite hangs on a broken startup.
(
  for ((elapsed=0; elapsed<SMOKE_TIMEOUT_SECONDS; elapsed++)); do
    if grep -Eq 'Done \(' server.log 2>/dev/null; then
      # A loader reaching "Done" does NOT execute KubeJS EntityTickEvent.Pre.
      # The reported first-world crash happened on the first dropped item.
      # Enable this only for the comprehensive modded integration QA job.
      if [[ "${SMOKE_ITEM_TICK_TEST:-0}" == "1" ]]; then
        printf 'forceload add 0 0\n'
        printf 'summon minecraft:item 0 128 0 {Item:{id:"minecraft:stone",count:1}}\n'
        sleep 5  # Let the real NeoForge server tick the item several times.
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
  if [[ "${SMOKE_ITEM_TICK_TEST:-0}" == "1" ]]; then
    # If the summoned item failed to spawn, the regression wasn't exercised.
    if ! grep -Fq 'Summoned new Item' server.log; then
      tail -100 server.log
      echo "FAIL: dropped item regression summon not confirmed in server log"
      exit 1
    fi
    echo "PASS: real modded server spawned/ticked an ordinary dropped item without crashing"
  fi
  # For the QA pack, prove NeoForge copied the experimental default config
  # into the new world, not merely that packwiz indexed the template file.
  if [[ -f "$WORK_DIR/defaultconfigs/tmm_transformations-server.toml" ]]; then
    python3 - "$WORK_DIR/world/serverconfig/tmm_transformations-server.toml" <<'PY'
import sys, tomllib
from pathlib import Path
path = Path(sys.argv[1])
assert path.is_file(), f"Missing newly-created vampire serverconfig: {path}"
settings = tomllib.loads(path.read_text(encoding="utf-8"))
expected = {
    "experimentalVampireChassis": True,
    "experimentalAutomaticDualAcquisition": True,
    "experimentalAllowUnverifiedOtherProviders": True,
    "experimentalOperatorCure": False,
}
assert settings == expected, f"New-world vampire settings not loaded as intended: {settings}"
print("PASS: new world really activated the disposable QA vampire config defaults")
PY
  fi
  echo "PASS: dedicated server reached Done and shut down cleanly (modded=${SMOKE_WITH_MODS:-0}; exit=$STATUS)"
  exit 0
fi
tail -100 server.log
echo "FAIL: loader did not report ready (exit=$STATUS)"
exit 1
