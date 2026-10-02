package dev.masabehavior.mixin;

import fi.dy.masa.malilib.util.position.PositionUtils.HitPart;
import fi.dy.masa.tweakeroo.config.Configs;
import fi.dy.masa.tweakeroo.config.Hotkeys;
import fi.dy.masa.tweakeroo.tweaks.PlacementTweaks;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlacementTweaks.class, remap = false)
abstract class PlacementTweaksMixin {
    @Shadow(remap = false) private static boolean firstWasOffset;

    private static HitPart originalHitPart;
    private static EnumFacing originalSide;
    private static EnumFacing originalSideRotated;

    @Inject(method = "tryPlaceBlock", at = @At("HEAD"), remap = false)
    private static void rememberHitPart(
            PlayerControllerMP controller,
            EntityPlayerSP player,
            WorldClient world,
            BlockPos pos,
            EnumFacing side,
            EnumFacing sideRotated,
            float playerYaw,
            Vec3d hitVec,
            EnumHand hand,
            HitPart hitPart,
            boolean firstClick,
            CallbackInfoReturnable<?> cir) {
        originalHitPart = hitPart;
        originalSide = side;
        originalSideRotated = sideRotated;
    }

    @ModifyVariable(
            method = "tryPlaceBlock",
            index = 7,
            at = @At(
                    value = "INVOKE",
                    remap = false,
                    target = "Lfi/dy/masa/tweakeroo/tweaks/PlacementTweaks;handleFlexibleBlockPlacement(Lbsa;Lbud;Lbsb;Let;Lfa;FLbhe;Lub;Lfi/dy/masa/malilib/util/position/PositionUtils$HitPart;)Lud;"),
            remap = false)
    private static Vec3d alignHitVector(Vec3d hitVec) {
        double x = 0;
        double y = 0;
        double z = 0;

        if (Hotkeys.FLEXIBLE_BLOCK_PLACEMENT_ADJACENT.getKeyBind().isKeyBindHeld()
                && originalHitPart != null && originalHitPart != HitPart.CENTER) {
            EnumFacing adjacent = originalSideRotated.getOpposite();
            EnumFacing back = originalSide.getOpposite();
            x += adjacent.getXOffset() + back.getXOffset();
            y += adjacent.getYOffset() + back.getYOffset();
            z += adjacent.getZOffset() + back.getZOffset();
        }

        boolean offset = Hotkeys.FLEXIBLE_BLOCK_PLACEMENT_OFFSET.getKeyBind().isKeyBindHeld()
                || (Configs.Generic.REMEMBER_FLEXIBLE.getBooleanValue() && firstWasOffset);
        if (offset) {
            EnumFacing diagonal = originalSideRotated.getOpposite();
            x += diagonal.getXOffset();
            y += diagonal.getYOffset();
            z += diagonal.getZOffset();
        }

        return x == 0 && y == 0 && z == 0 ? hitVec : hitVec.add(x, y, z);
    }
}
