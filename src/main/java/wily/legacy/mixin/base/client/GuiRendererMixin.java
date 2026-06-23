package wily.legacy.mixin.base.client;

//? if >=26.2 {
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.util.ARGB;
import net.minecraft.client.gui.render.GuiItemAtlas;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.state.gui.BlitRenderState;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jspecify.annotations.Nullable;

import java.util.Set;
//?}
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.gui.render.pip.GuiEntityRenderer;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.state.gui.GuiItemRenderState;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.client.renderer.state.gui.pip.GuiEntityRenderState;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
//? if <26.2 {
/*import net.minecraft.client.renderer.MultiBufferSource;
*///?}
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wily.legacy.Legacy4J;
import wily.legacy.client.LegacyGuiEntityRenderer;
import wily.legacy.client.LegacyGuiItemRenderState;
import wily.legacy.client.LegacyGuiItemRenderer;
import wily.legacy.client.LegacyOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mixin(GuiRenderer.class)
public abstract class GuiRendererMixin {
    @Unique
    private static final int GUI_ENTITY_RENDERER_POOL_SIZE = 20;
    @Shadow
    @Final
    GuiRenderState renderState;
    //? if <26.2 {
    /*@Shadow
    @Final
    private MultiBufferSource.BufferSource bufferSource;
    @Shadow
    @Final
    private SubmitNodeCollector submitNodeCollector;
    *///?}
    @Shadow
    @Final
    private FeatureRenderDispatcher featureRenderDispatcher;
    @Unique
    private Long2ObjectMap<LegacyGuiItemRenderer> guiItemRenderers = new Long2ObjectArrayMap<>();
    @Unique
    private List<GuiEntityRenderer> guiEntityRenderers;
    @Unique
    private int legacyFrameNumber;

    @Inject(method = "<init>", at = @At("TAIL"))
    void initTail(GuiRenderState guiRenderState,
                  //? if <26.2 {
                  /*MultiBufferSource.BufferSource bufferSource, SubmitNodeCollector submitNodeCollector,
                  *///?}
                  FeatureRenderDispatcher featureRenderDispatcher, List list, CallbackInfo ci) {
        guiEntityRenderers = createGuiEntityRenderers(
                //? if <26.2 {
                /*bufferSource
                *///?}
        );
        //? if >=26.2 {
        ITEM_ATLASES = new Long2ObjectArrayMap<>();
        //?}
    }

    @Inject(method = "prepareItemElements", at = @At("HEAD"))
    private void prepareItemElementsHead(CallbackInfo ci) {
        legacyFrameNumber++;
        if (guiItemRenderers == null) {
            Legacy4J.LOGGER.error("that can't be!");
            guiItemRenderers = new Long2ObjectArrayMap<>();
        }
        guiItemRenderers.forEach((i, renderer) -> renderer.markInvalid());
    }

    //? if <26.2 {
    /*@ModifyArg(method = "prepareItemElements", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/state/gui/GuiRenderState;forEachItem(Ljava/util/function/Consumer;)V"))
    private Consumer<GuiItemRenderState> prepareItemElements(Consumer<GuiItemRenderState> consumer) {
        return renderState -> {
            LegacyGuiItemRenderState legacyRenderState = LegacyGuiItemRenderState.of(renderState);
            if (legacyRenderState.size() == 16 && legacyRenderState.opacity() == 1.0) consumer.accept(renderState);
            else
                guiItemRenderers.computeIfAbsent(((long) legacyRenderState.size() << 32) | (Float.floatToIntBits(LegacyOptions.enhancedItemTranslucency.get() ? 1.0f : legacyRenderState.opacity()) & 4294967295L), LegacyGuiItemRenderer::new).markValid();
        };
    }
    *///?}

    @Inject(method = "prepareItemElements", at = @At("RETURN"))
    private void prepareItemElements(CallbackInfo ci) {
        for (ObjectIterator<LegacyGuiItemRenderer> iter = guiItemRenderers.values().iterator(); iter.hasNext(); ) {
            var renderer = iter.next();
            if (renderer.isValid())
                renderer.prepareItemElements(featureRenderDispatcher,
                        //? if <26.2 {
                        /*submitNodeCollector, bufferSource,
                        *///?}
                        renderState, legacyFrameNumber);
            else {
                renderer.close();
                iter.remove();
            }
        }
    }

    @Inject(method = "close", at = @At("RETURN"), remap = false)
    private void close(CallbackInfo ci) {
        guiItemRenderers.forEach((i, renderer) -> renderer.close());
        guiEntityRenderers.forEach(PictureInPictureRenderer::close);
        //? if >=26.2 {
        List<Runnable> runnables = new ArrayList<>();
        ITEM_ATLASES.forEach((k, atlas) -> {
            if (atlas != null) {atlas.close();runnables.add(() -> ITEM_ATLASES.remove(k));}
        });
        runnables.forEach(Runnable::run);
        //?}
    }

    @Inject(method = "preparePictureInPicture", at = @At("HEAD"))
    void preparePictureInPicture(CallbackInfo ci) {
        for (GuiEntityRenderer guiEntityRenderer : guiEntityRenderers) {
            LegacyGuiEntityRenderer.of(guiEntityRenderer).available();
        }
    }

    @Inject(method = "preparePictureInPictureState", at = @At("HEAD"), cancellable = true/*? if neoforge {*//*, remap = false*//*?}*/)
    void preparePictureInPictureState(PictureInPictureRenderState arg, int i, /*? if neoforge {*//*boolean firstPass, CallbackInfoReturnable<Boolean> cir*//*?} else {*/CallbackInfo ci/*?}*/) {
        if (arg.getClass() == GuiEntityRenderState.class) {
            GuiEntityRenderer guiEntityRenderer = guiEntityRenderers.stream().map(LegacyGuiEntityRenderer::of).filter(LegacyGuiEntityRenderer::isAvailable).findFirst().map(a -> ((GuiEntityRenderer) a)).orElse(guiEntityRenderers.get(0));
            LegacyGuiEntityRenderer.of(guiEntityRenderer).use();
            guiEntityRenderer.prepare((GuiEntityRenderState) arg, this.renderState,
                    //? if >=26.2 {
                    featureRenderDispatcher,
                    //?}
                    i);
            //? if neoforge {
            /*cir.setReturnValue(true);
             *///?} else {
            ci.cancel();
            //?}
        }
    }

    @Unique
    private static List<GuiEntityRenderer> createGuiEntityRenderers(
            //? if <26.2 {
            /*MultiBufferSource.BufferSource bufferSource
            *///?}
    ) {
        var dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        var renderers = new ArrayList<GuiEntityRenderer>(GUI_ENTITY_RENDERER_POOL_SIZE);
        for (int i = 0; i < GUI_ENTITY_RENDERER_POOL_SIZE; i++) {
            renderers.add(new GuiEntityRenderer(
                    //? if <26.2 {
                    /*bufferSource,
                    *///?}
                    dispatcher));
        }
        return List.copyOf(renderers);
    }

    //? if >=26.2 {
    private @Unique Long2ObjectMap<@Nullable GuiItemAtlas> ITEM_ATLASES;

    @Unique
    private static final ScopedValue<Long> UNSCALED_SIZE = ScopedValue.newInstance();

    @Unique
    private static final ScopedValue<Set<Object>> frame = ScopedValue.newInstance();

    @Definition(id = "itemAtlas", field = "Lnet/minecraft/client/gui/render/GuiRenderer;itemAtlas:Lnet/minecraft/client/gui/render/GuiItemAtlas;")
    @Expression("@(this.itemAtlas)")
    @ModifyExpressionValue(method = "prepareItemAtlas", at = @At("MIXINEXTRAS:EXPRESSION"))
    GuiItemAtlas prepareItemAtlasRedirectGetsToThisItemAtlasToTheMap(GuiItemAtlas original) {
        if (UNSCALED_SIZE.isBound()) {
            Long i = UNSCALED_SIZE.get();
            return ITEM_ATLASES.get((long) i);
        } else return original;
    }


    @Definition(id = "itemAtlas", field = "Lnet/minecraft/client/gui/render/GuiRenderer;itemAtlas:Lnet/minecraft/client/gui/render/GuiItemAtlas;")
    @Expression("this.itemAtlas = ?")
    @WrapOperation(method = "prepareItemAtlas", at = @At("MIXINEXTRAS:EXPRESSION"))
    void prepareItemAtlasRedirectAssignmentsToThisItemAtlasToTheMap(GuiRenderer instance, GuiItemAtlas value, Operation<Void> original) {
        if (UNSCALED_SIZE.isBound()) {
            Long i = UNSCALED_SIZE.get();
            ITEM_ATLASES.put((long) i, value);
        } else original.call(instance, value);
    }

    @Shadow
    protected abstract GuiItemAtlas prepareItemAtlas(Set<Object> itemsInFrame, int slotTextureSize);

    @Shadow
    protected abstract int getGuiScaleInvalidatingItemAtlasIfChanged();

    @Inject(method = "lambda$prepareItemElements$0", at = @At("HEAD"))
    void prepareItemElementsLambdaAddSizeContext(MutableBoolean hasOversizedItems, GuiItemAtlas itemAtlas, GuiItemRenderState itemState, CallbackInfo ci, @Local(argsOnly = true) LocalRef<GuiItemAtlas> itemAtlasRef) {
        LegacyGuiItemRenderState legacyGuiItemRenderState = LegacyGuiItemRenderState.of(itemState);
        ScopedValue.where(UNSCALED_SIZE, ((long) legacyGuiItemRenderState.size() << 32) | (Float.floatToIntBits(LegacyOptions.enhancedItemTranslucency.get() ? 1.0f : legacyGuiItemRenderState.opacity()) & 4294967295L)).run(() -> {
            itemAtlasRef.set(prepareItemAtlas(frame.get(), LegacyGuiItemRenderState.of(itemState).size() * getGuiScaleInvalidatingItemAtlasIfChanged()));
        });
    }

    @ModifyArg(method = "prepareItemElements", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/state/gui/GuiRenderState;forEachItem(Ljava/util/function/Consumer;)V"))
    private Consumer<GuiItemRenderState> prepareItemElementsForEach(Consumer<GuiItemRenderState> consumer, @Local(name = "itemsInFrame") Set<Object> itemsInFrame) {
        return renderState -> {
            ScopedValue.where(frame, itemsInFrame).run(() -> {
                LegacyGuiItemRenderState legacyRenderState = LegacyGuiItemRenderState.of(renderState);
                if (legacyRenderState.size() == 16 && legacyRenderState.opacity() == 1.0) consumer.accept(renderState);
                else consumer.accept(renderState);
            });
        };
    }

    @Inject(method = "endFrame", at = @At("RETURN"))
    void endFrameTail(CallbackInfo ci) {
        ITEM_ATLASES.forEach((_, atlas) -> {
            if (atlas != null) atlas.endFrame();
        });
    }

    @Definition(id = "BlitRenderState", type = BlitRenderState.class)
    @Expression("new BlitRenderState(?,?,?,?,?,?,?,?,?,?,?,@(?),?,?)")
    @ModifyExpressionValue(method = "submitBlitFromItemAtlas", at = @At("MIXINEXTRAS:EXPRESSION"))
    private int submitBlitFromItemAtlas(int original, @Local(argsOnly = true) GuiItemRenderState state) {
        return ARGB.color(LegacyGuiItemRenderState.of(state).opacity(), original);
    }

    @Definition(id = "BlitRenderState", type = BlitRenderState.class)
    @Expression("new BlitRenderState(@(?),?,?,?,?,?,?,?,?,?,?,?,?,?)")
    @ModifyExpressionValue(method = "submitBlitFromItemAtlas", at = @At("MIXINEXTRAS:EXPRESSION"))
    private RenderPipeline submitBlitFromItemAtlas(RenderPipeline original, @Local(argsOnly = true) GuiItemRenderState state) {
        return LegacyGuiItemRenderState.of(state).opacity() != 1.0f ? RenderPipelines.GUI_TEXTURED : original;
    }
    //?}
}
