# Generic XP boundary hardening — #475

Base b2143903b9e5f74b91313d2bb726c6d10dbf7858; existing architecture #580/#588/#595/#598, integrated #604. PR #654 is the single active XP boundary slice; Monk #629 owns event hooks, ChannelEngine, ProgressionApi and GUI. Design-only #570 not replayed.

## Owner decision 2026-11-24
Per configured XP system:
- `max_level` in 1..10,000,000 inclusive (existing hard maximum). Exact maximum XP = max_level * xp_per_point; excess effective XP discarded, including fixed awards and runaway decreasing-growth sequences. No next-level partial at the cap.
- `min_reward` is minimum positive effective XP per settled event, default/hard minimum 1e-128; `round_subminimum_up` boolean (default false). A positive eligible award below minimum grants zero or the minimum according to this toggle, after growth and before final cap. The cap takes precedence, including the final partial amount to reach it. Zero/negative invalid nominal amounts remain invalid, not free XP. Existing max_base_award remains input validation, distinct from computed cap.
- Both fixed and ordinary awards use the same settlement rule; first/recent receipts are consumed even if a legitimate event yields zero, preventing replay after reset. Award.accepted denotes settled authentic event, not strictly positive XP.
- Existing saved XP is not silently destroyed if a cap is lowered; reconciling incompatible live save commitments on config reload is a separate task.
- Ordinary growth uses bounded BigDecimal geometric cost and binary search (logarithmic crossing count), retaining the actual factor at every level and supporting large finite awards rather than equating an arbitrary loop limit to overflow. Decimal metadata <=128 integer digits, abs scale <=128; intermediate precision 288 digits; persisted fraction truncated at 128 decimal places. Malformed config/nominal input rejected.
- Custom thresholds >1000 supported by Wallet projection; exact integer fields reject malformed fractional/overflow values. TMM default cap stays 10m, minimum defaults to existing technical resolution, round-up false, no new normal balance rates.

Type A: GenericXpBoundaryTest and ExperienceEconomyTest cover fixed/ordinary cap, both rounding modes, no replay, near-cap precedence, large finite crossings, extreme factors, independent currency config, malformed input and generic wallet view. Quick CI required. B/C after authorized inclusion: alternate 2500 threshold and independent min reward modes, max-level UI/purchases, relog and normal MXP regression; no full build authorized.
