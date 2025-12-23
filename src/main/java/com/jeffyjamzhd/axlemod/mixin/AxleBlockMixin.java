package com.jeffyjamzhd.axlemod.mixin;

import btw.block.blocks.AxleBlock;
import btw.client.render.util.RenderUtils;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AABBPool;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.RenderBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AxleBlock.class)
@Environment(EnvType.CLIENT)
public class AxleBlockMixin {
    // Alignment bits
    // 0 - UpDown
    // 1 - NorthSouth
    // 2 - EastWest

    private static final int CENTER = 0;
    private static final int EXTRUSION_1 = 1;
    private static final int EXTRUSION_2 = 2;

    @Inject(method = "renderBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/src/RenderBlocks;setRenderBounds(Lnet/minecraft/src/AxisAlignedBB;)V"
            ), cancellable = true)
    private void renderNewAxle(RenderBlocks renderer,
                               int i, int j, int k,
                               CallbackInfoReturnable<Boolean> cir,
                               @Local(ordinal = 3) int alignment) {
        for (int step = 0; step < 3; step++) {
            this.boundsForStep(renderer, alignment, step);
            renderer.renderStandardBlock((AxleBlock) (Object) this, i, j, k);
        }
        cir.setReturnValue(true);
    }

    @Inject(method = "renderBlockAsItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lbtw/client/render/util/RenderUtils;renderInvBlockWithMetadata(Lnet/minecraft/src/RenderBlocks;Lnet/minecraft/src/Block;FFFI)V"
            ), cancellable = true)
    private void renderNewAxleItem(RenderBlocks renderer,
                                   int iItemDamage, float fBrightness,
                                   CallbackInfo ci) {
        for (int step = 0; step < 3; step++) {
            this.boundsForStep(renderer, 1, step);
            RenderUtils.renderInvBlockWithMetadata(renderer, (AxleBlock) (Object) this, -.5F, -.5F, -.5F, iItemDamage);
        }
        ci.cancel();
    }

    @Unique
    private void boundsForStep(RenderBlocks renderer, int alignment, int step) {
        switch (step) {
            case CENTER: {
                switch (alignment) {
                    case 0 -> renderer.setRenderBounds(getBox((6.5/16D), 0D, (6.5/16D), (9.5/16D), 1D, (9.5/16D)));
                    case 1 -> renderer.setRenderBounds(getBox((6.5/16D), (6.5/16D), 0D, (9.5/16D), (9.5/16D), 1D));
                    case 2 -> renderer.setRenderBounds(getBox(0D, (6.5/16D), (6.5/16D), 1D, (9.5/16D), (9.5/16D)));
                }
                break;
            }
            case EXTRUSION_1: {
                switch (alignment) {
                    case 0 -> renderer.setRenderBounds(getBox((6/16D), 0D, (7/16D), (10/16D), 1D, (9/16D)));
                    case 1 -> renderer.setRenderBounds(getBox((6/16D), (7/16D), 0D, (10/16D), (9/16D), 1D));
                    case 2 -> renderer.setRenderBounds(getBox(0D, (6/16D), (7/16D), 1D, (10/16D), (9/16D)));
                }
                break;
            }
            case EXTRUSION_2: {
                switch (alignment) {
                    case 0 -> renderer.setRenderBounds(getBox((7/16D), 0D, (6/16D), (9/16D), 1D, (10/16D)));
                    case 1 -> renderer.setRenderBounds(getBox((7/16D), (6/16D), 0D, (9/16D), (10/16D), 1D));
                    case 2 -> renderer.setRenderBounds(getBox(0D, (7/16D), (6/16D), 1D, (9/16D), (10/16D)));
                }
                break;
            }
        }
    }

    @Unique
    private AxisAlignedBB getBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return AxisAlignedBB.getAABBPool().getAABB(minX, minY, minZ, maxX, maxY, maxZ);
    }
}
