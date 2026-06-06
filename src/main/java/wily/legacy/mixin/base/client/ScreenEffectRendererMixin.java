package wily.legacy.mixin.base.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
//? if <26.2 {
/*import net.minecraft.client.renderer.MultiBufferSource;
*///?}
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wily.factoryapi.util.ColorUtil;
import wily.legacy.client.LegacyActivationAnim;

import java.util.Collections;
import java.util.List;

@Mixin(ScreenEffectRenderer.class)
public abstract class ScreenEffectRendererMixin {

    @Unique
    private static int texRenderColor = 0xFFFFFFFF;
    @Shadow
    private ItemStack itemActivationItem;
    @Shadow
    @Final
    private Minecraft minecraft;

    //? if <26.2 {
    /*@Shadow
    @Final
    private MultiBufferSource bufferSource;
    *///?}

    //? if <26.2 {
    /*@ModifyArg(method = "renderTex", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;setColor(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
    *///?} else {
    @Inject(method = "submitBlockSprite", at = @At("HEAD"))
    //?}
    private static /*? if <26.2 {*//*int*//*?} else {*/void/*?}*/ renderTex(/*? if <26.2 {*//*int*//*?} else {*/CallbackInfo ci, @Local(argsOnly = true, name = "color") LocalIntRef/*?}*/ i) {
        /*? if <26.2 {*//*return*//*?} else {*/i.set/*?}*/(ColorUtil.mergeColors(texRenderColor, i/*? if >=26.2 {*/.get()/*?}*/));
    }

    @ModifyArg(method =
            //? if <26.2 {
            /*"renderScreenEffect"
             *///?} else {
            "submit"
            //?}
            , at = @At(value = "INVOKE", target =
            //? if <26.2 {
            /*"Lnet/minecraft/client/renderer/ScreenEffectRenderer;renderTex(Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V"
            *///?} else {
            "Lnet/minecraft/client/renderer/ScreenEffectRenderer;submitBlockSprite(Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V"
            //?}
    ))
    private TextureAtlasSprite renderScreenEffect(TextureAtlasSprite f,
                                                  //? if <26.2 {
                                                  /*PoseStack i, MultiBufferSource f1, SubmitNodeCollector submitNodeCollector,
                                                  *///?} else {

                                                  //?}
                                                  @Local /*? if neoforge {*//*Pair<BlockState, BlockPos> pair*//*?} else {*/BlockState state/*?}*/) {
        //? if neoforge {
        /*BlockState state = pair.getLeft();
         *///?}
        List<BakedQuad> quads = Collections.emptyList();
        List<BlockStateModelPart> parts = new java.util.ArrayList<>();
        minecraft.getModelManager().getBlockStateModelSet().get(state).collectParts(minecraft.player.getRandom(), parts);
        if (!parts.isEmpty()) quads = parts.get(0).getQuads(Direction.UP);
        if (!quads.isEmpty()) {
            BakedQuad quad = quads.get(0);
            f = quad.materialInfo().sprite();
            texRenderColor = quad.materialInfo().isTinted() ? ColorUtil.withAlpha(minecraft.getBlockColors().getTintSource(state, quad.materialInfo().tintIndex()).colorInWorld(state, minecraft.level, minecraft.player.blockPosition()), 1.0f) : 0xFFFFFFFF;
        } else texRenderColor = 0xFFFFFFFF;

        return f;
    }

    @Redirect(method =
            //? if <26.2 {
            /*"renderScreenEffect"
            *///?} else {
            "submit"
            //?}
            , at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isOnFire()Z"))
    private boolean renderScreenEffect(LocalPlayer player) {
        return player.isOnFire() && !player.hasEffect(MobEffects.FIRE_RESISTANCE);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void tick(CallbackInfo ci) {
        if (itemActivationItem == null && LegacyActivationAnim.itemActivationRenderReplacement != null)
            LegacyActivationAnim.itemActivationRenderReplacement = null;
    }

    @Inject(method = "renderItemActivationAnimation", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V"), cancellable = true)
    private void renderItemActivationAnimation(PoseStack poseStack, float f, SubmitNodeCollector submitNodeCollector, CallbackInfo ci) {
        if (LegacyActivationAnim.itemActivationRenderReplacement != null) {
            ci.cancel();
            LegacyActivationAnim.itemActivationRenderReplacement.render(poseStack, f,
                    //? if <26.2 {
                    /*bufferSource
                    *///?} else {
                    submitNodeCollector
                    //?}
            );
            poseStack.popPose();
        }
    }

}
