# Placement patch investigation

## Scope and evidence

Target artifact: installed `tweakeroo-liteloader-1.12.2-0.31.0.litemod` (metadata says Tweakeroo 0.31.0, Minecraft 1.12.2, depends on MaLiLib). The installed `PlacementTweaks` bytecode was inspected with `javap`; the companion MaLiLib 0.54.0 `PlacementUtils.isReplaceable(World, BlockPos, boolean)` bytecode was also inspected.

The newer comparison is Sakura Ryoko's Tweakeroo `26.3` branch, [`PlacementTweaks.java`](https://raw.githubusercontent.com/sakura-ryoko/tweakeroo/26.3/src/main/java/fi/dy/masa/tweakeroo/tweaks/PlacementTweaks.java), especially `tryPlaceBlock` lines 460–489 and `canPlaceBlockIntoPosition` lines 1101–1105. The 1.12.2 comparison is the official [`ornithe/1.12.2` source](https://raw.githubusercontent.com/maruohon/tweakeroo/ornithe/1.12.2/src/main/java/tweakeroo/tweaks/PlacementTweaks.java), especially `tryPlaceBlock` lines 444–470 and `canPlaceBlockIntoPosition` lines 963–967. The reference commit hash still needs recording before implementation is treated as source-pinned.

## Findings

- Both generations explicitly accept liquid target cells in their general placement-position check. MaLiLib 0.54.0's helper also accepts liquid material when its `includeLiquids` argument is true, which the flexible-placement branch uses.
- In the newer flexible-placement code, adjacent and offset modes update both `posNew` and `hitVec` by the same directional displacement before handing the request to block placement. The 1.12.2 code updates `posNew` but leaves `hitVec` at the original click location.
- Fast placement reuses Tweakeroo's placement-position selection and placement path, so a correction in the shared flexible request path may also affect repeated placement. This needs confirmation against the exact acceptance scenes.

## Implemented correction

`PlacementTweaksMixin` modifies the hit vector at the installed 0.31.0 `tryPlaceBlock` call to `handleFlexibleBlockPlacement`, adding the same displacement used by adjacent and offset placement. It retains Tweakeroo's existing mode checks and changes only this placement request path. The Mixin targets the installed method descriptor directly; that descriptor was checked against the installed jar bytecode. Its HEAD handler uses the target's `WorldClient` argument and `CallbackInfoReturnable`, since `tryPlaceBlock` returns `EnumActionResult`. The packaged Mixin refmap is empty because the selectors use `remap=false` and the already-obfuscated dependency descriptor.

The first installed build failed at startup because the HEAD handler used `World` instead of `WorldClient` and `CallbackInfo` instead of `CallbackInfoReturnable`; the crash log reported the expected target descriptor. Those signatures were corrected in the rebuilt artifact. The user confirmed the corrected mod starts and fixes the reported issue. The newer source branch commit is not pinned; broader compatibility remains untested.
