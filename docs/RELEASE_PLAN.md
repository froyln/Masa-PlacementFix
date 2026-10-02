# First release plan — v0.1.0

## Scope

Create a Minecraft 1.12.2 LiteLoader companion mod correcting Tweakeroo block placement around liquids to follow a specific newer Tweakeroo implementation where 1.12.2 permits it.

Start with fast block placement when water or lava occupies the intended placement position or is near block corners. Then address flexible block placement, reusing a shared correction if source inspection establishes a shared cause.

Only Tweakeroo placement is in scope. Preserve compatibility with installed masa mods; changes to their other behavior are excluded. Waterlogging and mechanics absent from Minecraft 1.12.2 are excluded.

## 1. Compare the placement code

- Use the reported symptoms as the starting point. In-game reproduction is not a prerequisite for source inspection or choosing a correction.
- Confirm installed Tweakeroo/MaLiLib artifact metadata as needed; reuse the existing environment inventory.
- Locate official 1.12.2 and newer Tweakeroo sources. Record exact versions/commits and select a newer placement implementation that addresses the relevant liquid handling.
- Trace fast placement from input through hit targeting, target position, replaceability, and placement requests. Inspect flexible placement and all callers of any proposed shared target.
- Identify the incorrect decision and the smallest compatible correction. Distinguish Tweakeroo logic from Minecraft version differences and server restrictions.
- Record source evidence, expected placement position/orientation, and any unresolved assumptions. Check licenses before reusing code.

**Done when:** The responsible methods, newer reference, proposed correction, and expected results are identified. Ask for missing user details only if they prevent choosing the fix.

## 2. Build and implement the focused correction

- Confirm that a companion mod can hook the relevant methods using facilities available in the target LiteLoader environment. Record supported dependency versions and handle incompatible targets clearly.
- Add only the build setup, metadata, and hooks needed to produce the working mod.
- Fix fast placement first. Apply the same correction to flexible placement if appropriate; otherwise make the smallest additional correction needed for that feature.
- Preserve hotkeys, feature enable/disable behavior, ordinary dry placement, and placement restrictions.
- Use one focused runnable regression check where the corrected logic can be checked independently, and build the artifact. Record the exact commands and results.
- If a companion mod is infeasible, present the concrete limitation before changing to a Tweakeroo fork or replacement.

**Done when:** A built artifact implements the targeted correction and is ready for user testing. A successful build does not establish in-game behavior.

## 3. Validate the artifact in game

Target: PrismLauncher instance `1.12.2-ai-masabehavior`, Minecraft 1.12.2 with LiteLoader.

| Check | Expected result |
| --- | --- |
| Fast placement in the reported liquid scene | Intended blocks placed at the specified positions |
| Flexible placement in the reported liquid scene | Specified target position and orientation |
| Dry equivalents and features disabled | Existing placement behavior preserved |
| Both placement features enabled | No conflicting placement behavior |
| Relevant water/lava and corner cases | Correct handling without invalid placement or persistent ghost blocks |
| Startup with installed masa mods | No hook failures or compatibility errors |

Expand checks only when code evidence or a failure identifies another relevant case. Record settings, scene, expected/actual result, artifact identity, and limitations. Multiplayer support requires evidence before making that claim.

**Done when:** User testing confirms the targeted corrections and relevant regression checks. Untested behavior remains explicitly unverified.

## 4. Deliver v0.1.0

- Provide the tested artifact, checksum, supported versions, installation/removal instructions, confirmed fixes, and known limitations.
- Include required license and upstream attribution notices. Exclude game binaries, personal configuration, and local workflow notes.
- Deliver locally unless external publication is explicitly authorized.

**Done when:** The user can install and remove the exact tested artifact, and the compatibility claims match the available evidence.

## Current state

The user confirmed that the installed mod works and fixes the reported Tweakeroo issue. The placement fix is complete for the tested setup: Minecraft 1.12.2, LiteLoader, Tweakeroo 0.31.0, and MaLiLib 0.54.0. The exact newer-source branch commit is not pinned, and broader compatibility has not been tested. No further implementation is planned unless the user reports another issue.
