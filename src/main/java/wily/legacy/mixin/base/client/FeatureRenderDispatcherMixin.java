package wily.legacy.mixin.base.client;

//? if <26.2 {
/*import net.minecraft.client.renderer.MultiBufferSource;
*///?}
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import wily.legacy.client.LegacyFeatureRenderDispatcher;

import java.util.function.BiFunction;

@Mixin(FeatureRenderDispatcher.class)
public class FeatureRenderDispatcherMixin implements LegacyFeatureRenderDispatcher {
    //? if <26.2 {
    /*@Mutable
    @Shadow
    @Final
    private MultiBufferSource.BufferSource bufferSource;

    @Override
    public MultiBufferSource.BufferSource getBufferSource() {
        return bufferSource;
    }

    @Override
    public void setBufferSource(MultiBufferSource.BufferSource bufferSource) {
        this.bufferSource = bufferSource;
    }
    *///?} else {

    @Unique
    private BiFunction<SubmitNodeCollector, RenderType, RenderType> map = (_, b) -> b;

    @Override
    public BiFunction<SubmitNodeCollector, RenderType, RenderType> getMap() {
        return map;
    }

    @Override
    public void setMap(BiFunction<SubmitNodeCollector, RenderType, RenderType> biFunction) {
        this.map = biFunction;
    }
    //?}
}
