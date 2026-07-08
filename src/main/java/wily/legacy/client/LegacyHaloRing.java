package wily.legacy.client;

//? if >=26.2 {
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
//?} else {
/*import com.mojang.blaze3d.vertex.Tesselator;
*///?}
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
//? if >=26.1 {
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
//?} else {
/*import com.mojang.blaze3d.platform.DepthTestFunction;
 *///?}
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import  com.mojang.renderpearl.api.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.AbstractTexture;
//? if >=1.21.11 {
import net.minecraft.resources.Identifier;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.dimension.DimensionType;
//?} else {
/*import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.Identifier;
 *///?}
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import wily.factoryapi.mixin.base.RenderPipelinesAccessor;
import wily.legacy.Legacy4J;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

public final class LegacyHaloRing {
    private static final int RADIUS = 100;
    private static final int SEGMENTS = 50;
    private static final int VERTEX_COUNT = (SEGMENTS + 1) * 2;
    private static final /*? if >=1.21.11 {*/Identifier/*?} else {*//*ResourceLocation*//*?}*/ TEXTURE = Legacy4J.createModLocation("textures/misc/halo_ring.png");
    private static final RenderPipeline PIPELINE = RenderPipelinesAccessor.register(
            RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
                    .withLocation(Legacy4J.createModLocation("pipeline/halo_ring"))
                    .withVertexShader("core/position_tex_color")
                    .withFragmentShader("core/position_tex_color")
                    //? if <26.2 {
                    /*.withSampler("Sampler0")
                    *///?} else {
                    .withBindGroupLayout(BindGroupLayout.builder().withSampler("Sampler0").build())
                    //?}
                    //? if >=26.1 {
                    .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
                    //?} else {
                    /*.withBlend(BlendFunction.TRANSLUCENT)
                     *///?}
                    //? if >=26.2 {
                    .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
                    .withPrimitiveTopology(PrimitiveTopology.TRIANGLE_STRIP)
                    //?} else {
                    /*.withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.TRIANGLE_STRIP)
                    *///?}
                    //? if >=26.2 {
                    .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
                    //?} else if >=26.1 {
                    /*.withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
                    *///?} else {
                    /*.withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                    .withDepthWrite(false)
                     *///?}
                    .withCull(false)
                    .build()
    );
    private static GpuBuffer ringBuffer;

    private LegacyHaloRing() {
    }

    public static void render(PoseStack poseStack) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level == null || /*? if >=1.21.11 {*/level.dimensionType().skybox() != DimensionType.Skybox.OVERWORLD/*?} else {*//*DimensionSpecialEffects.forType(level.dimensionType()).skyType() != DimensionSpecialEffects.SkyType.OVERWORLD*//*?}*/ || !CommonValue.HALO_RING.get()) {
            return;
        }

        //~ if >=26.2 'getMainCamera' -> 'mainCamera'
        Camera camera = minecraft.gameRenderer.mainCamera();
        float partialTick = minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);
        int skyColor = /*? if >=1.21.11 {*/camera.attributeProbe().getValue(EnvironmentAttributes.SKY_COLOR, partialTick)/*?} else {*//*level.getSkyColor(camera.getPosition(), partialTick)*//*?}*/;
        float luminance = (ARGB.redFloat(skyColor) * 2.0f + ARGB.greenFloat(skyColor) * 3.0f + ARGB.blueFloat(skyColor)) / 6.0f;
        float brightness = 0.6f + Mth.clamp(luminance, 0.0f, 1.0f) * 0.4f;

        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0f));
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0f));
        draw(poseStack.last().pose(), brightness);
        poseStack.popPose();
    }

    private static void draw(Matrix4f pose, float brightness) {
        //~ if >=26.2 'getModelViewMatrix' -> 'getModelViewStack'
        Matrix4f modelView = new Matrix4f(RenderSystem.getModelViewStack()).mul(pose);
        GpuBufferSlice transforms = RenderSystem.getDynamicUniforms().writeTransform(modelView, new Vector4f(brightness, brightness, brightness, 1.0f), new Vector3f(), new Matrix4f()/*? if <1.21.11 {*//*, 0.0f*//*?}*/);
        //~ if >=26.2 'getMainRenderTarget' -> 'gameRenderer.mainRenderTarget'
        RenderTarget target = Minecraft.getInstance().gameRenderer.mainRenderTarget();
        AbstractTexture haloTexture = Minecraft.getInstance().getTextureManager().getTexture(TEXTURE);

        //~ if >=26.2 'OptionalInt.empty()' -> 'Optional.empty()'
        try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Legacy halo ring", target.getColorTextureView(), Optional.empty(), target.getDepthTextureView(), OptionalDouble.empty())) {
            renderPass.setPipeline(PIPELINE);
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", transforms);
            //? if >=1.21.11 {
            renderPass.bindTexture("Sampler0", haloTexture.getTextureView(), haloTexture.getSampler());
            //?} else {
            /*renderPass.bindSampler("Sampler0", haloTexture.getTextureView());
             *///?}
            renderPass.setVertexBuffer(0, getRingBuffer()/*? if >=26.2 {*/.slice()/*?}*/);
            //~ if >=26.2 '0, VERTEX_COUNT' -> 'VERTEX_COUNT, 1, 0, 0'
            renderPass.draw(VERTEX_COUNT, 1, 0, 0);
        }
    }

    private static GpuBuffer getRingBuffer() {
        if (ringBuffer == null || ringBuffer.isClosed()) {
            ringBuffer = buildRingBuffer();
        }
        return ringBuffer;
    }

    private static GpuBuffer buildRingBuffer() {
        //? if >=26.2 {
        try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(DefaultVertexFormat.POSITION_TEX_COLOR.getVertexSize() * (SEGMENTS + 1) * 2)) {
        //?}
        //? if <26.2 {
        /*BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_TEX_COLOR);
        *///?} else {
        BufferBuilder builder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_TEX_COLOR);
        //?}
        float u = 0.0f;
        float verticalOffset = RADIUS * 999.0f / 1000.0f;
        float arcRadians = Mth.TWO_PI / SEGMENTS;
        float halfSegments = SEGMENTS / 2.0f;
        float wideSegments = SEGMENTS / 8.0f;
        float wideSegmentsSqr = wideSegments * wideSegments;

        for (int i = 0; i <= SEGMENTS; i++) {
            float diff = Math.abs(i - halfSegments);
            float edge = halfSegments - wideSegments;
            diff = diff < edge ? 0.0f : diff - edge;
            float width = 1.0f + diff * diff / wideSegmentsSqr * 10.0f;
            float x = RADIUS * Mth.cos(i * arcRadians) - verticalOffset;
            float y = RADIUS * Mth.sin(i * arcRadians);
            int alpha = getFogAlpha(i);
            builder.addVertex(x, y, -width).setUv(u, 0.0f).setColor(255, 255, 255, alpha);
            builder.addVertex(x, y, width).setUv(u, 1.0f).setColor(255, 255, 255, alpha);
            u -= 0.25f;
        }

        try (MeshData meshData = builder.buildOrThrow()) {
            return RenderSystem.getDevice().createBuffer(() -> "Legacy halo ring", GpuBuffer.USAGE_VERTEX, meshData.vertexBuffer());
        }
        //? if >=26.2 {
        }
        //?}
    }

    private static int getFogAlpha(int segment) {
        float edgeFade = Mth.clamp(Math.min(segment, SEGMENTS - segment) / 14.0f, 0.0f, 1.0f);
        float alpha = edgeFade * edgeFade * (3.0f - 2.0f * edgeFade);
        return Mth.floor(alpha * 255.0f);
    }
}
