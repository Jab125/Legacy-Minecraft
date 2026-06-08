package wily.legacy.mixin.base.client;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.BlockModelLighter;
import net.minecraft.core.BlockPos;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wily.legacy.client.LegacyChunkLoading;

@Mixin(BlockModelLighter.class)
public class BlockModelLighterMixin {
    @Inject(method = "getLightCoords", at = @At("RETURN"), cancellable = true)
    private void getLightCoords(BlockState state, BlockAndTintGetter level, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (LegacyChunkLoading.hasPendingFeatures(pos)) {
            cir.setReturnValue(LightCoordsUtil.max(cir.getReturnValue(), LightCoordsUtil.FULL_SKY));
        }
    }
}
