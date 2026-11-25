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
  # Read-only analysis of the circle's NATIVE processing path. The old
  # global KubeJS item-tick bridge is blocked by the assembler.
  if [[ -f "$ROOT/scripts/audit_mahou_catalyst_bytecode.py" ]]; then
    python3 "$ROOT/scripts/audit_mahou_catalyst_bytecode.py" \
      "$WORK_DIR/mods/mahoutsukai-1.21.1-v1.36.31.jar"
  fi
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
  if [[ "${VERIFY_FULL_QA_INVENTORY:-0}" == "1" ]]; then
    verify_args=()
    if [[ -n "$EXTRA_JAR_LIST" ]]; then
      for EXTRA in "${EXTRA_JARS[@]}"; do verify_args+=(--extra "$EXTRA"); done
    fi
    python3 "$ROOT/scripts/verify_qa_inventory.py" --strict-target --mods-dir "$WORK_DIR/mods" --side server "${verify_args[@]}"
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
  # The next OWNER-AUTHORIZED full smoke stages custom blueprints into this
  # already-empty, disposable server work directory. No user's world is touched.
  if [[ -f "$ROOT/research/prefab-donors/stage_all_houses_qa.py" ]]; then
    printf 'DISPOSABLE QA ONLY\n' > "$WORK_DIR/.tmm-disposable-qa"
    python3 "$ROOT/research/prefab-donors/stage_all_houses_qa.py" \
      --stage-qa-game-dir "$WORK_DIR" --target server > "$WORK_DIR/housing-previews-static.json"
    test "$(find "$WORK_DIR/prefab-extension/server-cache" -maxdepth 1 -name '*.nbt' -type f | wc -l)" -eq 18
    echo 'Staged 18 approved custom-house NBTs for authorized modded smoke (not granted)'
  fi
  echo "Starting hash-pinned MODDED dedicated server"
else
  echo "Starting loader-only dedicated server (no mods installed)"
fi
# Every full modded QA that includes the Mahou KubeJS catalyst bridge must
# exercise an actual dropped-item entity tick. A plain server boot did not
# catch the reported world-start crash.
if [[ "${SMOKE_WITH_MODS:-0}" == "1" ]]; then
  SMOKE_ITEM_TICK_TEST=1
  test ! -f "$WORK_DIR/kubejs/server_scripts/mahou_neo_vitae_catalyst_exchange.js" || {
    echo "FAIL: legacy global Catalyst Exchange ItemEntity tick script was packaged"
    exit 5
  }
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
    if ! grep -Fq 'Summoned new Stone' server.log; then
      tail -100 server.log
      echo "FAIL: dropped item regression summon not confirmed in server log"
      exit 1
    fi
    # KubeJS errors are logged but may not crash the JVM; fail closed.
    if grep -Eq '\[KubeJS Server/\].*(Error in|ERROR)' server.log; then
      grep -E '\[KubeJS Server/\].*(Error in|ERROR)' server.log | tail -30
      echo "FAIL: KubeJS server-side script error during dropped-item smoke"
      exit 1
    fi
    echo "PASS: real modded server ticked dropped Stone without KubeJS errors"
  fi
  # NeoForge 1.21.1 SERVER config normally loads from config/; a
  # world/serverconfig file, when present, takes precedence. Some servers
  # never create a per-world copy. Validate the effective candidate, not
  # the existence of an optional per-world override.
  if [[ -f "$WORK_DIR/defaultconfigs/tmm_transformations-server.toml" ]]; then
    python3 - "$WORK_DIR" <<'PY'
import sys, tomllib
from pathlib import Path
root = Path(sys.argv[1])
global_path = root / "config/tmm_transformations-server.toml"
override = root / "world/serverconfig/tmm_transformations-server.toml"
template = root / "defaultconfigs/tmm_transformations-server.toml"
assert global_path.is_file(), f"QA vampire SERVER config missing: {global_path}"
assert template.is_file(), f"QA vampire world template missing: {template}"
expected = {
    "experimentalVampireChassis": True,
    "experimentalAutomaticDualAcquisition": True,
    "experimentalAllowUnverifiedOtherProviders": True,
    "experimentalOperatorCure": False,
}
for p in (global_path, template):
    assert tomllib.loads(p.read_text()) == expected, f"Invalid QA vampire profile {p}"
effective = override if override.is_file() else global_path
actual = tomllib.loads(effective.read_text())
assert actual == expected, f"Active vampire QA world config differs from test profile: {effective}: {actual}"
print(f"PASS: NeoForge QA vampire flags resolved from {effective}")
print(f"INFO: world-specific override {'present' if override.is_file() else 'absent; global SERVER config used'}")
PY
  fi
  echo "PASS: dedicated server reached Done and shut down cleanly (modded=${SMOKE_WITH_MODS:-0}; exit=$STATUS)"
  exit 0
fi
tail -100 server.log
echo "FAIL: loader did not report ready (exit=$STATUS)"
exit 1
