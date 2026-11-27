# Ars Nouveau pinned spellbook progression, casting and mana

Exact `ars_nouveau-1.21.1-5.13.5.jar`, SHA-1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`; direct JSON/CFR0.152 and targeted javap checks. Evidence: SpellBook/SpellTier/BookUpgradeRecipe, GuiSpellBook, AbstractCaster/Spell/SpellResolver, StandardSpellValidator, PlayerCaster/LivingCaster, ManaCap/ManaCapEvents/ManaUtil/ServerConfig, update/cast packets. [Parent](ars-nouveau.md), [guide/glyph learning](ars-nouveau-pinned-guide-glyphs.md), [processing/enchanting](ars-nouveau-pinned-crafting.md). This is native player spellbook path, not all equipment/turret/addon casting. No actual client cast/mana/GUI test.

## Start, compose and cast

1. Craft Worn Notebook and open native documentation. Use **Getting Started → Spell Books / spell creation** and **Glyph Index** references; exact guide access and85glyph recipes are in linked companion. The Notebook is reference; the spellbook is casting/composition tool.
2. Craft **Novice Spell Book** shapeless: Book + Iron Shovel + Iron Pickaxe + Iron Axe + Iron Sword. Native `novice_spellbook_alt` path below also accepts Worn Notebook in place of Book; that is a separate consumed recipe input, not a free second reference book. Tools are recipe ingredients, not equipment you merely need equipped.
3. Hold spellbook, assign/use Ars **Open Spell Book** control in Minecraft Options and open composition UI. Native default keys can be unbound by TMM policy, so do not strand player by assuming C works. Select a saved slot, place a cast form followed by effect(s) and compatible augment(s), name and save. Start with configurable default Touch/Self/Projectile plus Break/Harm choices; actual enabled starters/learned glyphs govern palette.
4. Select the stored spell through configured radial HUD/slot controls, aim and use book. Client cast request carries selected slot/hand/view; actual resolution runs on server. Expect actual block/entity/effect change and mana debit, not merely a saved name or animation. Shift when deliberately casting at eligible block entities; ordinary nonshift block-entity handling can return without cast.
5. Learn more glyphs through Scribe's Table and **use the completed glyph** to unlock player knowledge. That consumes XP points/ingredients in the learning process; ordinary spellbook casting spends personal mana. Source Jars are a different machine/ritual economy, not fuel for every hand cast. Learning a Tier2/3 glyph is not the same as using it in a lower-tier composition UI.

The spellbook item initializes **10 saved spell slots**. GuiSpellBook separately starts with **10 composition glyph links**, plus caster bonus links and optional configured infinite-spell length modifier. These are two different limits. Don't import an older4/7/10links-per-book ladder. Book tier restricts nominal glyph tier in composition, not the count of saved spell slots.

## Exact upgrade gates

| Native recipe / book | Consumed input list | Capability taught |
| --- | --- | --- |
| novice_spell_book / Novice | Book, Iron Shovel/Pickaxe/Axe/Sword | SpellTier.ONE(value1); starter composition and first real cast |
| novice_spellbook_alt | Worn Notebook, same four Iron tools | Alternate Novice input route; not another tier |
| apprentice_book_upgrade / Apprentice | Novice Book;1#c:obsidians;3#c:gems/diamond;2Quartz Block;2#c:rods/blaze | SpellTier.TWO(value2), Tier2 composition choices |
| archmage_book_upgrade / Archmage | Apprentice Book;3#c:ender_pearls;2#c:gems/emerald;Totem of Undying;Nether Star;Wilden Tribute | SpellTier.THREE(value3), Tier3 composition/capstone |

Exact alternate resource is `data/ars_nouveau/recipe/novice_spellbook_alt.json`.

BookUpgradeRecipe extends ShapelessRecipe, copies SpellBook ingredient component patch onto result, preserving stored caster/spell settings through upgrade. Learned glyphs remain player capability, so component preservation and learned-knowledge persistence are different facts. Book use on server raises remembered ManaCap bookTier only when new tier is higher and catches up glyphBonus from known glyph count; switching to a weaker book does not lower that remembered tier in this method.

Archmage inputs explicitly include Wilden Tribute and Nether Star/Totem gates. Use [Wilden ritual branch](ars-nouveau-pinned-rituals.md) plus actual encounter/Tribute acquisition; summon/animation is not a kill reward. Do not reward those gating materials to shortcut book progression. Core book route does not require completing every ritual, helper, enchantment or addon; those are optional branch choices.

## Composition validation vs cast-time validation

GuiSpellBook palette combines known displayed glyphs with enabled default starters. Ordinary UI uses crafting StandardSpellValidator plus GlyphMaxTierValidator(actual held book tier). Paste path also uses GlyphKnownValidator except creative exceptions. Creative book can display all registered parts, not a survival acquisition route.

Crafting validator enforces at most one cast method, augment compatibility/augmentation policies, glyph occurrence and invalid-combination policies. Cast-time StandardSpellValidator instead enforces at most one cast method, nonempty and starting cast method; augmentation cap and glyph occurrence checks are enabled by native server defaults `enforceCapOnCast=true`, `enforceGlyphLimitOnCast=true`. Those are distinct validator assemblies. PacketUpdateCaster stores received spell into a caster item; the inspected update body doesn't independently repeat the GUI's tier/knowledge validators. Do not describe every client UI restriction as a fully audited universal server gate for foreign items/addons. This records validation layers, not instructions to bypass them.

Spell.isValid itself only means nonempty. Resolver.canCast uses configured casting validator and enough mana; first validation error can be messaged. Rejected syntax/insufficient mana or canceled SpellCastEvent returns before form resolution/mana expenditure in these resolver methods. Custom glyphs/caster tools have additional state costs and their own checks; no blanket all-spell rollback promise.

## Casting debit and rejected actions

| Step | Exact order / consequence |
| --- | --- |
| Prepare spell | AbstractCaster modifies spell before casting, rejects empty when invalidMessage exists, wraps player/living caster and creates resolver. Target raytrace distinguishes block/entity/general cast. |
| Block interaction | A targeted ScribesTile always returns without normal cast here. Nonshift target with block entity not in IGNORE_TILE returns without resolver cast; shift can proceed. Thus no visible effect may be a deliberate UI/use arbitration, not no mana/source. |
| Admission cost | Spell.getCost sums part.getCastingCost; augment uses getCostForPart on preceding nonaugment part. Resolver subtracts player discounts, posts SpellCostCalcEvent.Pre and clamps minimum0. Creative can bypass enough-mana admission; normal player uses current ManaCap. |
| Resolve | canCast then uncanceled SpellCastEvent, then cast form's block/entity/general method. Only exact CastResolveType.SUCCESS triggers expendMana; other resolve types have their own wasSuccess flag. Actual downstream effect eligibility still belongs to each effect. |
| Debit cost | Recomputes spell cost/discounts and posts **SpellCostCalcEvent.Post**, clamps0, then wrapped caster removes personal mana. Pre and Post listeners can differ; enough-mana checked value is not guaranteed same as final debit. Form effects occur before this debit call. |
| State storage | ManaCap clamps0..getMaxMana; negative removal converted0; stores attachment. Ordinary hand cast has no ingredient or XP item consumption in these generic resolver methods, but individual effects/tools may consume items/durability and must be audited separately. |

Tile/turret casters and equipment override cost/activation; resolver explicitly skips expendMana for a TileCaster whose type is TURRET. This is a scope boundary, not a finding that turrets have no Source/other cost. Do not treat a successful cast/advancement as proof that every spell effect changed a protected block or damaged an immune target.

## Native capacity and regeneration formulas

Before attributes/events: raw maximum=`baseMax100 + 15×knownGlyphBonus + 50×rememberedBookTier`. Base raw regen=`baseRegen5 + 0.33×knownGlyphBonus + 1×rememberedBookTier`. Numbers are ServerConfig defaults, not effective TMM settings. Perks/attributes modify those values; MaxManaCalcEvent and ManaRegenCalcEvent can modify again. Familiar reserve defaults0.15; final ManaUtil.Mana.getRealMax truncates Max×(1-Reserve). Thus a familiar's reserve is percentage capacity, not fifteen flat mana or Source.

ManaCapEvents server Pre tick recalculates on default updateInterval5; regen increment is computed regen divided by max(1, integer(MEAN_TPS)/interval). Uses sampled mean TPS rather than claiming exactly per20real-time ticks under lag. Adds regen before recalculating changed max/reserve in that pass, syncs when changed/forced60tick boundary. No hunger cost in this generic loop; faction/diet interactions require both-side research, not an automatic vampire exemption claim.

Known glyphs/remembered tier, gear/perks/enchantments and active familiar can all affect final mana. Native Mana Boost/Mana Regen apparatus enchant recipes are in crafting companion; their final combat/gear behavior still belongs equipment audit. Maximum formula does not mean every85recipe glyph is required before Archmage progression.

## Quest and acceptance map

Guide → Novice → save/perform simple cast → Scribe/learn chosen glyph → working Source/processing when chosen → Apprentice and chosen Tier2 cast → safe Wilden encounter/other material gates → Archmage and chosen Tier3 cast. Show optional helper/ritual/equipment/addon branches separately. Every quest needs exact Notebook/JEI page, positive XP and safe thematic materials, with acquisition vs operation distinction.

Novice/Apprentice/Archmage item tasks prove ownership only. Glyph possession isn't learning; learned glyph isn't actual cast; mana decrement isn't proof target effect succeeded. Prefer a confirmed native action criterion or explicit manual observation until FTB detector is independently established. Do not invent per-cast progression XP or a mandatory all-glyph checklist.

Acceptance: new player guide/keybinding; save/select/cast first spell; invalid ordering/incompatible augment; insufficient mana vs canceled event; ordinary block entity vs shift/Scribe; known unknown glyph/tier UI; both upgrades retaining settings; stronger→weaker book remembered mana tier; Pre vs Post discount; no Source hand cast; familiar capacity reserve; regen/full cap/lag; relog/dimension/two players. Equipment/addon casting boundaries remain open. Parent researching.
