# Pilot 02 — category review candidates

Owner direction: [category standards v0.2](../../CATEGORY-STANDARDS.md). This revision changes **10 of the original 16 samples**, carries forward 6 without approval, and adds one iron chestplate icon to pair with the existing wearable UV candidate. No category is approved.

| Review sheet | Purpose |
|---|---|
| [gem-family.png](https://chatgpt.com/library?file_id=libfile_fa7eef0732908191b1f1fc72754fab28&account_id=personal) | Corrected round brilliant side profile; identical ordinary/mana geometry |
| [ingot-family.png](https://chatgpt.com/library?file_id=libfile_b90fdbe3cc848191ab0c4e2d72182d4f&account_id=personal) | One slightly angled master; iron/gold material variants |
| [block-families.png](https://chatgpt.com/library?file_id=libfile_e0f34e65843c8191a07daf156b730ad5&account_id=personal) | Six final native tiles, each repeated 5×5 |
| [model-face-previews.png](https://chatgpt.com/library?file_id=libfile_71eff20338d881918a6f49c816a8a054&account_id=personal) | Offline face/axis previews; no client acceptance |
| [wearable-pair.png](https://chatgpt.com/library?file_id=libfile_ce9b166bfaf08191a70ca8df0261e4e5&account_id=personal) | Inventory icon and UV pairing; equipped pose validation pending |

## Native PNGs

All files below are individual native-resolution review files at their intended asset keys, **outside active pack assets until approval**. The old candidates remain recoverable at the original pilot commit and branch.

| Asset key | Candidate | State |
|---|---|---|
| `assets/minecraft/textures/item/diamond.png` | [PNG](https://chatgpt.com/library?file_id=libfile_4c737ab696388191b12d6b8bcbcf8293&account_id=personal) | revised |
| `assets/minecraft/textures/item/emerald.png` | [PNG](https://chatgpt.com/library?file_id=libfile_7ae1bd2c1cf88191b400e75925d7721b&account_id=personal) | carried-forward-unapproved |
| `assets/minecraft/textures/item/iron_ingot.png` | [PNG](https://chatgpt.com/library?file_id=libfile_0108769972888191b0609b50b17ae0c5&account_id=personal) | revised |
| `assets/minecraft/textures/item/gold_ingot.png` | [PNG](https://chatgpt.com/library?file_id=libfile_7a37a681dbe08191932e11dcd8dae7aa&account_id=personal) | revised |
| `assets/botania/textures/item/mana_diamond.png` | [PNG](https://chatgpt.com/library?file_id=libfile_26066cb291e481919605c4b3edac8be5&account_id=personal) | revised |
| `assets/botania/textures/item/rune_mana.png` | [PNG](https://chatgpt.com/library?file_id=libfile_5bdcd6c9200c8191839cd207c2b03c07&account_id=personal) | carried-forward-unapproved |
| `assets/botania/textures/block/pure_daisy.png` | [PNG](https://chatgpt.com/library?file_id=libfile_17faac198790819185ea08d7a632f037&account_id=personal) | carried-forward-unapproved |
| `assets/minecraft/textures/block/stone.png` | [PNG](https://chatgpt.com/library?file_id=libfile_3d79a05f3644819197909ce40cf63138&account_id=personal) | revised |
| `assets/minecraft/textures/block/cobblestone.png` | [PNG](https://chatgpt.com/library?file_id=libfile_4183ae58815c8191b706170be0eab480&account_id=personal) | revised |
| `assets/minecraft/textures/block/oak_planks.png` | [PNG](https://chatgpt.com/library?file_id=libfile_bd06ab431ff881919bd9517200a8309c&account_id=personal) | revised |
| `assets/minecraft/textures/block/oak_log.png` | [PNG](https://chatgpt.com/library?file_id=libfile_369960ff1bdc819180e32c0f93b1ee3a&account_id=personal) | revised |
| `assets/minecraft/textures/block/oak_log_top.png` | [PNG](https://chatgpt.com/library?file_id=libfile_44a6bcf8ab5c8191a6ca71e0e8f85891&account_id=personal) | revised |
| `assets/botania/textures/block/livingrock.png` | [PNG](https://chatgpt.com/library?file_id=libfile_24b7170955308191a11aefa4ecb157ad&account_id=personal) | revised |
| `assets/botania/textures/block/runic_altar_side.png` | [PNG](https://chatgpt.com/library?file_id=libfile_591c9d25ec5c81918dc4f57ede188e8d&account_id=personal) | carried-forward-unapproved |
| `assets/minecraft/textures/models/armor/iron_layer_1.png` | [PNG](https://chatgpt.com/library?file_id=libfile_df3c32f9bfc88191b883f1a51417a53a&account_id=personal) | carried-forward-unapproved |
| `assets/minecraft/textures/gui/sprites/widget/button.png` | [PNG](https://chatgpt.com/library?file_id=libfile_2d8464dff558819186f145a456e2af8c&account_id=personal) | carried-forward-unapproved |
| `assets/minecraft/textures/item/iron_chestplate.png` | [PNG](https://chatgpt.com/library?file_id=libfile_07ce96a77964819199dcd13d1435a05f&account_id=personal) | new-related-deliverable |

## Changes and limits

- Diamond now has a shallow crown, broad girdle and pointed pavilion in side profile. Mana diamond is a controlled light-blue recolor of that exact master, preserving alpha and per-pixel structure.
- Gold uses the iron master rather than an independent generated ingot. The same master scale, angle, outline and plane layout are retained.
- Stone/livingrock use original periodic material painting. Bark, cobble and planks use original generation followed by a periodic-plus-smooth Poisson correction, without mirrored border averaging. Log end grain uses an original continuous periodic growth field with no dark square frame. All six have 5×5 proofs and source-level review; owner and client acceptance remain pending.
- Emerald, Pure Daisy, rune mana, altar side, prior armor UV and button are carried forward without implying approval. The added iron chestplate icon closes the missing icon part of the wearable sample; the UV remains a technical candidate and still needs equipped/pose/overlay checks.
- Rune-family substrate and mana-pearl inheritance are planned in the registry. No additional rune or pearl production, or ore variant production, begins before the relevant masters/category pilots are approved.
- Offline cube pictures are affine face previews, not screenshots or proof of actual Minecraft rendering, lighting, tints or GUI behavior.
- The selected native PNGs, their metadata and review sheets are durably saved. No newly generated candidate PNG is committed. Prior production draft PNGs are withdrawn from the active art skeleton under v0.2; original PR #296 and its exact commit are preserved.

## Provenance and checks

[Sample hashes, inheritance and saved file identities](sample-manifest.json), [generation prompts](prompts.json), and [checks](checks.json). Built-in image generation supplied the original selected gem, ingot, chestplate and three material sources. Color derivatives and periodic painting are reproducible in `art/tools/pilot02_samples.py`; source originals and the master PNGs are retained by the review file references, not third-party art. No upstream image was used as an image-generation seed.

The [full categorization table](https://chatgpt.com/library?file_id=libfile_9edef89c4e988191837e186665980568&account_id=personal) contains all 42,542 keys. See [category scope and follow-up](../../registry/CLASSIFICATION-STATUS.md) for confidence and exclusions. No game launch, gameplay edit, source assembly, Prism/resource-pack ZIP or release.
