#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
command -v javac >/dev/null || { echo 'Requires Java 21 javac'; exit 1; }
work="$(mktemp -d)"
trap 'rm -rf "$work"' EXIT
find src/main/java harness/src -name '*.java' -print0 | xargs -0 javac --release 21 -d "$work"
java -ea -cp "$work" dev.heatwaveqc.tmm.hexpilot.PilotHarnessTest
