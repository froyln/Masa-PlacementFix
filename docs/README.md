# Project context

## Goal

Create a Minecraft 1.12.2 LiteLoader companion mod that corrects Tweakeroo block placement around liquids using relevant newer Tweakeroo placement behavior.
The reported issue concerns Tweakeroo placement when liquids occupy the intended placement position or are near block corners: fast block placement and flexible block placement behave unexpectedly.
These are user-reported symptoms; no cause or exact behavioral difference has been verified yet.

## Initial scope

- Correct fast block placement around liquids first, then flexible block placement.
- Identify the relevant newer-version behavior before selecting a patch.
- Keep the target Minecraft version and LiteLoader environment compatible with the existing masa mods.
- Only Tweakeroo placement is in scope; other masa mod behavior is excluded.

The companion mod adds the newer flexible placement hit-vector adjustment to Tweakeroo 0.31.0. The user confirmed that the installed LiteLoader mod works and fixes the reported issue in the target instance.

The complete first-release sequence and acceptance gates are in [RELEASE_PLAN.md](RELEASE_PLAN.md). Current source comparison and candidate patch evidence are in [PATCH_DESIGN.md](PATCH_DESIGN.md).

## Target environment

PrismLauncher instance: `1.12.2-ai-masabehavior`.

Local instance path: `/home/froyln/.local/share/PrismLauncher/instances/1.12.2-ai-masabehavior`.

Confirmed from `mmc-pack.json` and installed filenames during setup:

| Component | Version |
| --- | --- |
| Minecraft | 1.12.2 |
| LiteLoader | 1.12.2-SNAPSHOT |
| Tweakeroo | 0.31.0 |
| MaLiLib | 0.54.0 |
| Litematica | 0.31.4 |
| MiniHUD | 0.41.1 |
| Item Scroller | 0.31.1 |

Minecraft directory: `minecraft/` within the instance. Masa mod files are in `minecraft/mods/1.12.2/`; Tweakeroo configuration is in `minecraft/liteconfig/common/tweakeroo.json`.
Installed filenames establish the local baseline. The testing instance contains `Masa-PlacementFix.litemod`, and the user confirmed it fixes the reported placement issue.

## Build and validation

Build with the cached Gradle 4.10.3 distribution and Java 8: `gradle build` (or invoke the local Gradle 4.10.3 binary). The build expects installed Tweakeroo 0.31.0 and MaLiLib 0.54.0 in the target instance mods directory; override with `-PinstanceModsDir=...` when needed. Output: `build/libs/Masa-PlacementFix.litemod`.

The hook targets Tweakeroo 0.31.0's `tryPlaceBlock` flexible-placement invocation. Its hit vector is shifted along with the target displacement for adjacent and offset modes, matching the newer source path. The user confirmed the fix in game for the reported setup; broader compatibility is not claimed.
