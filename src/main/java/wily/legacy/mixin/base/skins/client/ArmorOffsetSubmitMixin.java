package wily.legacy.mixin.base.skins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wily.legacy.skins.client.render.ArmorOffsetRenderContext;

//~ if >=26.2 'SubmitNodeStorage.ModelSubmit.class' -> 'ModelFeatureRenderer.Submit.class'
@Mixin(ModelFeatureRenderer.Submit.class)
public abstract class ArmorOffsetSubmitMixin implements ArmorOffsetRenderContext.SubmitAccess {
    @Unique
    private ArmorOffsetRenderContext.Offsets consoleskins$armorOffsets;

    @Override
    public ArmorOffsetRenderContext.Offsets consoleskins$getArmorOffsets() {
        return consoleskins$armorOffsets;
    }

    @Inject(method = "<init>", at = @At("RETURN"), require = 0)
    private void consoleskins$captureArmorOffsets(CallbackInfo ci) {
        consoleskins$armorOffsets = ArmorOffsetRenderContext.submitOffsets();
    }
}