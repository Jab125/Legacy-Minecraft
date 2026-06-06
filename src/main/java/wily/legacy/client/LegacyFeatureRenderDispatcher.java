package wily.legacy.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
//? if <26.2 {
/*import net.minecraft.client.renderer.MultiBufferSource;
*///?}
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.rendertype.RenderType;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface LegacyFeatureRenderDispatcher {
    static LegacyFeatureRenderDispatcher of(FeatureRenderDispatcher dispatcher) {
        return (LegacyFeatureRenderDispatcher) dispatcher;
    }

    //? if >=26.2 {
    BiFunction<SubmitNodeCollector, RenderType, RenderType> getMap();
    void setMap(BiFunction<SubmitNodeCollector, RenderType, RenderType> biFunction);
    //?} else {
    /*MultiBufferSource.BufferSource getBufferSource();

    void setBufferSource(MultiBufferSource.BufferSource bufferSource);
    *///?}
}
