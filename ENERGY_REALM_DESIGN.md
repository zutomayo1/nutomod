# Energy Realm Design (Target: Twilight Forest Scale 25%)

## 1. Design Goal
- Target volume: about 25% of Twilight Forest's overall playable content.
- Design pillars:
- Exploration-driven progression.
- Clear boss/event gates.
- Distinct environmental pressure with counterplay.
- Reusable systems for long-term expansion.

## 2. Scope Definition (What "25%" Means)
- 1 full dimension with stable portal loop.
- 4-6 biomes with unique hazards/resources.
- 6-8 landmark structures.
- 1 major boss + 2 elite encounters.
- 1 complete progression chain (early -> mid -> late).
- 20-30 unique content assets:
- 8-12 blocks.
- 8-12 items/materials.
- 4-6 mobs (including variants).
- 3-5 functional devices/mechanics.

## 3. Core Fantasy
- Theme: unstable energy ecosystem, ancient sanctums, charged storms.
- Player fantasy:
- Enter dangerous realm.
- Harness raw energy through rituals.
- Build stabilization tech.
- Conquer sanctum core and return stronger.

## 4. Dimension World Layout

### 4.1 Macro Layout
- Dimension name: `energy_realm`.
- Biome distribution style: multi-noise clusters with landmark-biome coupling.
- Global features:
- High-intensity sky tint and dynamic fog.
- Frequent vertical terrain variation.
- Energy altar nodes as progression anchors.

### 4.2 Biomes (Recommended 5)
1. Charged Forest (starter biome)
- Resources: basic shards, energy wood, stable flora.
- Threat: low-medium.

2. Crystal Plains (resource biome)
- Resources: crystal veins, reflection plants.
- Threat: medium.

3. Storm Fields (combat biome)
- Resources: storm fragments, volatile ore.
- Threat: high; event density increased.

4. Fracture Canyons (navigation biome)
- Resources: deep ore, relic debris.
- Threat: high due to vertical danger and ambush mobs.

5. Core Wastes (late biome)
- Resources: core-grade material.
- Threat: very high; boss-adjacent ecosystem.

## 5. Progression Loop

### 5.1 Stage A: Entry Survival
- Goal: survive pressure and collect `altar_shard`.
- Unlocks:
- Basic anti-pressure (core stabilizer).
- First altar activation.

### 5.2 Stage B: Stabilization
- Goal: secure reliable energy income.
- Unlocks:
- Altar wave farming.
- Mid-tier tools/armor upgrades.
- Access to locked structures (sanctum keys).

### 5.3 Stage C: Sanctum Campaign
- Goal: clear elite encounters and gather core seals.
- Unlocks:
- Boss arena access.
- Endgame recipes and realm-wide buffs.

### 5.4 Stage D: Core Conquest
- Goal: defeat dimension boss and stabilize realm core.
- Rewards:
- Endgame component.
- Permanent utility unlock (portal bonus, reduced pressure, or new crafting path).

## 6. Structures and Events

### 6.1 Required Structures (8)
1. Minor Altar Node
- Current altar system, common.

2. Ruined Relay Tower
- Loot + lore + mini encounter trigger.

3. Crystal Vault
- Puzzle-style reward chamber.

4. Storm Obelisk
- Timed defense event, storm shard source.

5. Fracture Forge
- Midgame crafting station unlock.

6. Sanctum Gate
- Consumes crafted key item.

7. Guardian Court (elite arena)
- Elite wave encounter, drops one seal.

8. Core Citadel (boss structure)
- Final campaign destination.

### 6.2 Event Framework
- Event tiers:
- Tier 1: altar skirmish (already present).
- Tier 2: elite defense.
- Tier 3: sanctum challenge.
- Global constraints:
- Per-structure cooldown.
- Escalating wave logic.
- Clear success/fail feedback.

## 7. Mobs and Combat Roles

### 7.1 Mob Roster (5 total)
1. Energy Being (base melee) - existing.
2. Spark Stalker (fast flanker).
3. Crystal Sentinel (tank/bruiser).
4. Storm Caster (ranged control/debuff).
5. Sanctum Warden (elite miniboss).

### 7.2 Boss (1)
- Name: Core Regent.
- Phases:
- P1 adds + beam pressure.
- P2 arena hazard amplification.
- P3 burst + weakpoint exposure.
- Required preparation:
- Stabilizer tier II+.
- Sanctum seal set.

## 8. Resources and Crafting Tree

### 8.1 Material Tiers
- Tier 0: raw_energy, altar_shard.
- Tier 1: core_stabilizer, charged_ingot.
- Tier 2: storm_alloy, crystal_matrix.
- Tier 3: core_heart (boss material).

### 8.2 Device/Functional Blocks (5)
1. Stabilizer Beacon
- Area pressure reduction.
2. Fracture Forge
- Mid-late recipes.
3. Energy Condenser
- Shard conversion automation-lite.
4. Sanctum Key Assembler
- Gate unlock items.
5. Core Relay
- Boss unlock sequence block.

## 9. Environmental Systems

### 9.1 Pressure System (already started)
- Base pressure applies debuffs in realm.
- Counterplay:
- Stabilizer in hand.
- Full energy armor.
- Beacon radius support.

### 9.2 Storm System
- Periodic charged storms:
- Reduced visibility.
- Mob buff.
- Unique drop multiplier.

### 9.3 Corruption Pockets
- Localized hazard zones.
- High risk/high reward ore density.

## 10. Loot and Reward Philosophy
- Every structure should give one of:
- Progression key material.
- Power spike gear component.
- Utility unlock fragment.
- Boss and elite loot should unlock systems, not only stats.

## 11. Quest/Advancement Plan
- Advancement tabs:
1. Enter the Realm.
2. First Stabilization.
3. Altar Mastery.
4. Sanctum Expedition.
5. Core Conquest.
- Each stage has hard gates and explicit next-step hints.

## 12. Content Production Backlog

### 12.1 Must-Have (MVP+)
- 5 biomes finalized.
- 4 structures implemented (altar, relay, obelisk, sanctum gate).
- 3 mobs + 1 elite.
- Full pressure + stabilizer + wave system.
- Advancement chain.

### 12.2 Phase 2
- 4 additional structures.
- Boss and boss arena.
- Tier 2-3 crafting devices.
- Storm global event.

### 12.3 Phase 3 (Polish)
- Ambience audio pack.
- Lore tablets and flavor loot.
- Balance pass on drops and spawn rates.
- Multiplayer stress balancing.

## 13. Technical Implementation Map (For This Project)
- Keep library stack:
- Fabric API (core systems).
- Custom Portal API (dimension travel).
- GeckoLib (mob/boss animation).
- Recommended add: Cardinal Components API (persistent player realm data).
- Data-first approach:
- New biome/feature/loot/recipe via JSON where possible.
- Java for behaviors: pressure, wave logic, boss AI, device logic.

## 14. Milestone Plan (8 Weeks Example)
1. Week 1-2: biome completion + pressure/storm base + loot tier cleanup.
2. Week 3-4: structures set A + 2 new mobs + advancement chain v1.
3. Week 5-6: sanctum campaign + elite encounters + devices.
4. Week 7-8: boss, balancing, polish, multiplayer tests.

## 15. Acceptance Criteria (Done Definition)
- Player can enter realm, learn counterplay, and progress without external docs.
- Midgame loop is repeatable and rewarding (altar/events/structures).
- Boss unlock path is clear and gated by obtained systems.
- Dimension has unique identity beyond recolored overworld.
- No hard blockers in dedicated server playthrough.

## 16. New Production Plan (Phase 2-4)

### 16.1 Phase 2: Core Loop Deepening (Target: next 7 weeks)
Goal: make the mainline fully clearable and improve feedback quality.

#### 2.1 Energy Storm System Deepening (2 weeks)
- Pre-storm warning:
- Screen-edge yellow tint.
- Warning sound cue.
- Special behavior during storm:
- `energy_being` enraged behavior.
- Rare storm creatures appear.
- Post-storm residue:
- Spawn collectible `charged_crystal` on ground.
- Building interaction:
- Lightning rod can absorb storm lightning and protect nearby area.

#### 2.2 Pressure System Improvement (1.5 weeks)
- High-pressure visual/audio feedback:
- Screen crack effect.
- Heartbeat sound.
- Biome-based base pressure:
- `storm_fields` pressure rises faster.
- Recovery methods:
- Energy potion.
- Rest point effects (bed / armor set bonus).
- Pressure-energy coupling:
- Higher pressure causes higher energy consumption.

#### 2.3 Altar Wave Event Completion (2 weeks)
- Three difficulty modes:
- Normal / Hard / Epic, decided by altar level.
- Five waves per run:
- Different enemy compositions per wave (melee + ranged + flying).
- Wave break:
- 10-second rest between waves; players can manually start next wave early.
- Wave rewards:
- Higher altar level gives better rewards (`altar_shard` amount + rare materials).
- Multiplayer support:
- Nearby players share wave progress.

#### 2.4 Sanctum Gate and Ending Content (1.5 weeks)
- Sanctum gate opening sequence:
- Particles + audio.
- New small Sanctum dimension after teleport:
- Mini boss arena or reward chamber.
- First Sanctum Guardian boss:
- Basic AI with about 3 attacks.
- Clear rewards:
- Exclusive weapon `sanctum_judicator`.
- New advancement/achievement.

#### Phase 2 Deliverables
- Fully clearable mainline:
- Enter realm -> altar progression -> open sanctum gate -> defeat guardian.
- Pressure + storm system with complete visual/audio feedback.
- At least one playable boss fight.

### 16.2 Phase 3: Content Expansion (Target: Jul-Aug, 7-8 weeks)
Goal: increase long-term replay value and exploration variety.

#### 3.1 Biome Identity Expansion (2 weeks)
Each biome gets one distinct mechanic + one signature resource:

| Biome | Unique Mechanic | Exclusive Resource |
|---|---|---|
| charged_forest | Electrified trees; chopping may shock player | Charged Log |
| crystal_plains | Crystal ore exposed on surface | Surface Crystal Cluster |
| storm_fields | Constant mini-storm; movement speed boost | Storm Grass (potion ingredient) |
| fracture_canyons | Fissure traps; pressure doubled | Deep Energy Ore |
| core_wastes | No natural light; very high pressure | Core Fragment |

#### 3.2 New Structures (2 weeks)
- Energy Tower (5 floors):
- Floor mini-bosses + top chest (high-level enchanted book, storm alloy).
- Abandoned Laboratory:
- Loot includes polishing-machine upgrade component and energy battery.
- Energy Vein Site:
- Large energy ore cluster guarded by `energy_being`.
- Portal Ruins:
- Repairable ancient portal to hidden area.

#### 3.3 New Boss (2 weeks)
- Boss: Core Devourer.
- Spawn condition:
- Use `core_bait` (crafted from `core_heart`) in `core_wastes`.
- Attacks:
- Pull/consume (drags players toward boss).
- Energy burst (large AOE).
- Summon energy spikes.
- Drops:
- Core Heart for final weapon upgrade path.

#### 3.4 New Item/Equipment Line (1 week)
- Charged tool upgrades:
- Upgrade tools with storm alloy for better efficiency + durability.
- Sanctum gear set:
- Unlocked after guardian clear; stronger than energy gear.
- Energy flight device:
- Consumes energy for creative-like flight (jetpack style).
- Five new potions:
- Storm resistance, pressure resistance, energy drain, etc.

#### Phase 3 Deliverables
- All 5 biomes have distinct gameplay identities.
- 4 new structures, including at least 1 multi-floor tower.
- 1 new boss + 1 new equipment progression line.

### 16.3 Phase 4: Optimization and Release (Target: September)
Goal: polish, stabilize, and ship v1.0.

#### 4.1 Balance Pass (1 week)
- Stage difficulty curve (early -> mid -> late).
- Material costs vs acquisition efficiency.
- Altar wave reward values.
- Compare energy gear strength against vanilla diamond/netherite.

#### 4.2 Multiplayer Completion (1 week)
- Pressure sync in multiplayer.
- Shared altar wave progress.
- Sanctum gate team teleport logic.
- Energy storm affects all online players consistently.

#### 4.3 Performance Optimization (0.5 week)
- Configurable storm particle density.
- Pressure calculation frequency optimization.
- Structure generation loading optimization.

#### 4.4 Localization and Documentation (0.5 week)
- Complete `en_us.json` and `zh_cn.json`.
- Patchouli guide review pass.
- Advancement text review/update.
- CurseForge/Modrinth page assets:
- Screenshots, feature text, video.

#### 4.5 Release Candidate (1 week)
- Internal test with 3-5 players.
- Fix critical feedback bugs.
- Generate API docs for mod integrations.
- Official v1.0 release.

#### Phase 4 Deliverables
- v1.0 release build.
- CurseForge/Modrinth pages online.
- Complete Chinese + English documentation.
