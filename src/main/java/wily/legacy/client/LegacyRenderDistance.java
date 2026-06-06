package wily.legacy.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
//? if >=26.2 {
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
//?}
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

public final class LegacyRenderDistance {
    private static final int BLOCK_ENTITY_CAP = 62;
    private static final Map<EntityType<?>, Integer> ENTITY_CAPS = Util.make(new IdentityHashMap<EntityType<?>, Integer>(), caps -> {
        put(caps, 23, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.SILVERFISH, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.ENDERMITE);
        put(caps, 25, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.TROPICAL_FISH);
        put(caps, 27, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.RABBIT);
        put(caps, 30, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.COD, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.SALMON);
        put(caps, 32, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.CHICKEN);
        put(caps, 33, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.CAT);
        put(caps, 40, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.CAVE_SPIDER, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.BAT);
        put(caps, 41, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.PARROT, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.WOLF);
        put(caps, 42, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.OCELOT);
        put(caps, 49, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.PHANTOM);
        put(caps, 51, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.SQUID, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.PUFFERFISH, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.DOLPHIN);
        put(caps, 55, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.GUARDIAN, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.MINECART, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.CHEST_MINECART, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.FURNACE_MINECART, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.HOPPER_MINECART, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.TNT_MINECART, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.SPAWNER_MINECART, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.COMMAND_BLOCK_MINECART);
        put(caps, 57, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.PIG);
        put(caps, 59, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.TURTLE);
        put(caps, 61, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.CREEPER);
        put(caps, 64, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.BLAZE, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.SHULKER, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.VILLAGER, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.WANDERING_TRADER);
        put(caps, 66, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.ZOMBIE, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.SHEEP);
        put(caps, 67, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.ZOMBIE_VILLAGER, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.HUSK, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.DROWNED, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.WITCH, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.ZOMBIFIED_PIGLIN, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.EVOKER, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.VINDICATOR, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.PILLAGER);
        put(caps, 68, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.SKELETON, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.STRAY, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.COW);
        put(caps, 69, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.SNOW_GOLEM);
        put(caps, 79, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.WITHER_SKELETON, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.SPIDER, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.IRON_GOLEM);
        put(caps, 80, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.GHAST, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.ENDERMAN, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.RAVAGER, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.ELDER_GUARDIAN, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.POLAR_BEAR, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.PANDA, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.HORSE, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.DONKEY, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.MULE, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.SKELETON_HORSE, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.ZOMBIE_HORSE, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.LLAMA, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.TRADER_LLAMA, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.WITHER);
        put(caps, 159, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.ITEM_FRAME, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.GLOW_ITEM_FRAME, /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.PAINTING);
    });
    private static final Set<BlockEntityType<?>> BLOCK_ENTITIES = Set.of(/*? if <26.2 {*//*BlockEntityType*//*?} else {*/BlockEntityTypes/*?}*/.CHEST, /*? if <26.2 {*//*BlockEntityType*//*?} else {*/BlockEntityTypes/*?}*/.TRAPPED_CHEST, /*? if <26.2 {*//*BlockEntityType*//*?} else {*/BlockEntityTypes/*?}*/.ENDER_CHEST, /*? if <26.2 {*//*BlockEntityType*//*?} else {*/BlockEntityTypes/*?}*/.BANNER, /*? if <26.2 {*//*BlockEntityType*//*?} else {*/BlockEntityTypes/*?}*/.SIGN, /*? if <26.2 {*//*BlockEntityType*//*?} else {*/BlockEntityTypes/*?}*/.HANGING_SIGN);

    private LegacyRenderDistance() {
    }

    public static void initDefault() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft == null || minecraft.options == null || minecraft.options.entityDistanceScaling().get() >= 0.5) return;
        minecraft.options.entityDistanceScaling().set(1.0);
        LegacyOptions.legacyEntityDistance.set(true);
        minecraft.options.save();
        LegacyOptions.CLIENT_STORAGE.save();
    }

    public static boolean shouldRender(Entity entity, double x, double y, double z) {
        if (!usingLegacyEntityDistance()) return true;
        int cap = cap(entity);
        return cap == 0 || entity.distanceToSqr(x, y, z) <= (double) cap * cap;
    }

    public static boolean shouldRender(BlockEntity blockEntity, Vec3 cameraPos) {
        if (!usingLegacyEntityDistance()) return true;
        if (cameraPos == null) return true;
        return !BLOCK_ENTITIES.contains(blockEntity.getType()) || Vec3.atCenterOf(blockEntity.getBlockPos()).distanceToSqr(cameraPos) <= (double) BLOCK_ENTITY_CAP * BLOCK_ENTITY_CAP;
    }

    public static boolean usingLegacyEntityDistance() {
        return LegacyOptions.legacyEntityDistance.get();
    }

    private static int cap(Entity entity) {
        Integer cap = ENTITY_CAPS.get(entity.getType());
        if (cap == null) return 0;
        if (entity instanceof LivingEntity living && living.isBaby() && !isFish(entity.getType())) return cap / 2;
        return cap;
    }

    @SafeVarargs
    private static <T> void put(Map<T, Integer> caps, int cap, T... types) {
        for (T type : types) caps.put(type, cap);
    }

    private static boolean isFish(EntityType<?> type) {
        return type == /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.COD || type == /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.SALMON || type == /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.PUFFERFISH || type == /*? if <26.2 {*//*EntityType*//*?} else {*/EntityTypes/*?}*/.TROPICAL_FISH;
    }
}
