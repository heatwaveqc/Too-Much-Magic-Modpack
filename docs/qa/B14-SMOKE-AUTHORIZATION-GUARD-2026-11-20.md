# B14 automatic server smoke regression — preventive CI (#260)

**2026-11-20, source review:** The 2026-11-16 reproduction [run 43966546114](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43966546114) triggered an expensive B14 NeoForge server smoke from a documentation-only PR. In the current `test/full-integration` source, `.github/workflows/b14-cc-eldritch-iesnium.yml` still runs **lightweight static checks** on push/PR, but the actual `bash scripts/smoke_server.sh` invocation now has the explicit step guard `github.event_name == 'workflow_dispatch' && inputs.full_smoke == true`. It must remain so.

## Scope and implementation

- The current source survey of **14 B-series workflows** (B4 through B16, including legacy round-specific workflows) plus `verify.yml` found smoke, client packaging or assembly in manual authorization gates. Current source does not reproduce automatic B14 *server boot*; do not confuse automatic safe metadata CI with full game launch.
- `scripts/audit_smoke_workflows.py` scans actual YAML `run` commands, not PR path filters or compiled script names. Full server smoke, Prism package, or integration assembly must be conjunctively guarded by **manual `workflow_dispatch` plus `inputs.full_smoke==true` or `inputs.authorized_build==true`**, at the job or executable-step level. Permissive OR conditions are rejected.
- `tests/test_smoke_workflow_policy.py` contains negative fixtures reproducing the original B14 regression, missing consent flag, missing manual event, OR bypass, wrong-step gate, and unguarded Prism; positive fixtures for B14 guarded smoke, job-gated client packages and static source compilation.
- `.github/workflows/verify.yml` now runs both checks inside the quick metadata job on PR, push and manual invocation. It never executes a game boot. No mod pins, quests, or packwiz files modified.

## Limits and acceptance

This guard intentionally audits the **legacy B-series family and bootstrap** affected by Issue #260, not every arbitrary future workflow or indirect shell-script helper. Keep broader owner-authorized workflow protections and workflow-call provenance audits under CI/integration maintenance; do not imply this alone blocks every possible future dangerous command or manually authorized run.

To validate: `python3 scripts/audit_smoke_workflows.py` and `python3 -m unittest discover -s tests -p 'test_smoke_workflow_policy.py'`. The active PR's Verify bootstrap / metadata check must PASS with no server or Prism steps. A future simulated B14 smoke without a guard must FAIL the test. No automated cancellation, actual game launch, Prism package, or owner smoke authorization occurs here.

The 2026-11-16 historical run cannot be undone; this adds a prevention mechanism. Leave #260 open until CI result and integration review, then resolve the original accidental-trigger regression separately from broader integration audit.
