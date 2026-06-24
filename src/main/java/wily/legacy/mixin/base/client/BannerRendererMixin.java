package wily.legacy.mixin.base.client;

import net.minecraft.client.renderer.blockentity.BannerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import wily.legacy.util.client.LegacyHeadRenderState;

@Mixin(BannerRenderer.class)
public class BannerRendererMixin {
    @ModifyArg(method = "submitBanner", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;IIILnet/minecraft/client/resources/model/sprite/SpriteId;Lnet/minecraft/client/resources/model/sprite/SpriteGetter;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"), index = 5)
    private static int legacy$hurtTintBannerBase(int color) {
        return LegacyHeadRenderState.getBannerTint(color);
    }

    //~ if >=26.2 'Lnet/minecraft/client/renderer/SubmitNodeCollector;' -> 'Lnet/minecraft/client/renderer/OrderedSubmitNodeCollector;' {
    //~ if >=26.3 'TextureAtlasSprite' -> 'UvMapping' {
    @ModifyArg(method = "submitPatternLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/OrderedSubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/texture/UvMapping;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"), index = 6)
    //~}
    //~}
    private static int legacy$hurtTintBannerPatterns(int color) {
        return LegacyHeadRenderState.getBannerTint(color);
    }
}
