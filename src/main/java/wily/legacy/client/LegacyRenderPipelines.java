package wily.legacy.client;

import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.renderpearl.api.vertex.VertexFormat;
//? if >=26.2 {
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
//?}
import net.minecraft.client.renderer.RenderPipelines;
import wily.factoryapi.mixin.base.RenderPipelinesAccessor;
import wily.legacy.Legacy4J;

public class LegacyRenderPipelines {
    public static final RenderPipeline LEGACY_SKY = RenderPipelinesAccessor.register(RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET).withLocation(Legacy4J.createModLocation("pipeline/sky")).withVertexShader("core/sky").withFragmentShader("core/sky")
            //? if >=26.2 {
            .withVertexBinding(0, DefaultVertexFormat.POSITION)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            //?} else {
            /*.withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)
            *///?}
            .build());
    public static final RenderPipeline LEGACY_FLAT_CLOUDS = RenderPipelinesAccessor.register(
            RenderPipeline.builder(RenderPipelines.CLOUDS_SNIPPET)
                    .withLocation(Legacy4J.createModLocation("pipeline/flat_clouds"))
                    .withVertexShader(Legacy4J.createModLocation("core/legacy_rendertype_clouds"))
                    .withFragmentShader(Legacy4J.createModLocation("core/legacy_clouds"))
                    //? if >=26.2 {
                    .withDepthStencilState(DepthStencilState.DEFAULT)
                    //?} else {
                    /*.withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, true))
                    *///?}
                    .withCull(false)
                    .build()
    );
    public static final RenderPipeline LEGACY_CLOUDS = RenderPipelinesAccessor.register(
            RenderPipeline.builder(RenderPipelines.CLOUDS_SNIPPET)
                    .withLocation(Legacy4J.createModLocation("pipeline/clouds"))
                    .withVertexShader(Legacy4J.createModLocation("core/legacy_rendertype_clouds"))
                    .withFragmentShader(Legacy4J.createModLocation("core/legacy_clouds"))
                    //? if >=26.2 {
                    .withDepthStencilState(DepthStencilState.DEFAULT)
                    //?} else {
                    /*.withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, true))
                    *///?}
                    .build()
    );
    public static final RenderPipeline LEGACY_WARM_FLAT_CLOUDS = RenderPipelinesAccessor.register(
            RenderPipeline.builder(RenderPipelines.CLOUDS_SNIPPET)
                    .withLocation(Legacy4J.createModLocation("pipeline/warm_flat_clouds"))
                    .withVertexShader(Legacy4J.createModLocation("core/legacy_rendertype_clouds"))
                    .withFragmentShader(Legacy4J.createModLocation("core/legacy_clouds_warm"))
                    //? if >=26.2 {
                    .withDepthStencilState(DepthStencilState.DEFAULT)
                    //?} else {
                    /*.withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, true))
                    *///?}
                    .withCull(false)
                    .build()
    );
    public static final RenderPipeline LEGACY_WARM_CLOUDS = RenderPipelinesAccessor.register(
            RenderPipeline.builder(RenderPipelines.CLOUDS_SNIPPET)
                    .withLocation(Legacy4J.createModLocation("pipeline/warm_clouds"))
                    .withVertexShader(Legacy4J.createModLocation("core/legacy_rendertype_clouds"))
                    .withFragmentShader(Legacy4J.createModLocation("core/legacy_clouds_warm"))
                    //? if >=26.2 {
                    .withDepthStencilState(DepthStencilState.DEFAULT)
                    //?} else {
                    /*.withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, true))
                    *///?}
                    .build()
    );
    public static final RenderPipeline LEGACY_PACK_FLAT_CLOUDS = RenderPipelinesAccessor.register(
            RenderPipeline.builder(RenderPipelines.CLOUDS_SNIPPET)
                    .withLocation(Legacy4J.createModLocation("pipeline/pack_flat_clouds"))
                    .withVertexShader(Legacy4J.createModLocation("core/legacy_rendertype_clouds"))
                    .withFragmentShader("core/rendertype_clouds")
                    //? if >=26.2 {
                    .withDepthStencilState(DepthStencilState.DEFAULT)
                    //?} else {
                    /*.withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, true))
                    *///?}
                    .withCull(false)
                    .build()
    );
    public static final RenderPipeline LEGACY_PACK_CLOUDS = RenderPipelinesAccessor.register(
            RenderPipeline.builder(RenderPipelines.CLOUDS_SNIPPET)
                    .withLocation(Legacy4J.createModLocation("pipeline/pack_clouds"))
                    .withVertexShader(Legacy4J.createModLocation("core/legacy_rendertype_clouds"))
                    .withFragmentShader("core/rendertype_clouds")
                    //? if >=26.2 {
                    .withDepthStencilState(DepthStencilState.DEFAULT)
                    //?} else {
                    /*.withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, true))
                    *///?}
                    .build()
    );
    public static final RenderPipeline GAMMA = RenderPipelinesAccessor.register(
            RenderPipeline.builder(RenderPipelines.POST_PROCESSING_SNIPPET)
                    .withLocation(Legacy4J.createModLocation("pipeline/gamma"))
                    //? if <26.2 {
                    /*.withSampler("InSampler")
                    *///?} else {
                    .withBindGroupLayout(BindGroupLayout.builder().withSampler("InSampler").withUniform("GammaInfo", UniformType.UNIFORM_BUFFER).build())
                    //?}
                    .withVertexShader("core/screenquad")
                    .withFragmentShader(Legacy4J.createModLocation("core/gamma"))
                    //? if >=26.3 {
                    .withColorTargetState(ColorTargetState.DEFAULT)
                    //?}
                    //? if <26.2 {
                    /*.withUniform("GammaInfo", UniformType.UNIFORM_BUFFER)
                    *///?}
                    .build()
    );
}
