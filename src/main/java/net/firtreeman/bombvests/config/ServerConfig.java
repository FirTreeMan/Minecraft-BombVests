package net.firtreeman.bombvests.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Double> EXPLOSION_RADIUS_LOG_BASE;
    public static final ForgeConfigSpec.ConfigValue<Integer> EXPLOSION_RADIUS_LOG_SCALAR;

    public static final ForgeConfigSpec.ConfigValue<Double> DYNAMITE_EXPLOSION_VALUE;
    public static final ForgeConfigSpec.ConfigValue<Double> VOLATILE_DYNAMITE_EXPLOSION_VALUE;
    public static final ForgeConfigSpec.ConfigValue<Double> HIGH_EXPLOSIVE_EXPLOSION_VALUE;
    public static final ForgeConfigSpec.ConfigValue<Double> SHRAPNEL_EXPLOSION_VALUE;
    public static final ForgeConfigSpec.ConfigValue<Double> PACKED_EXPLOSION_VALUE;

    public static final ForgeConfigSpec.ConfigValue<Boolean> EXPLODE_BLOCKS;

    static {
        BUILDER.push("Server Config for Bomb Vests");

        EXPLOSION_RADIUS_LOG_BASE = BUILDER.comment("Logarithm base for calculating bomb vest explosion radius")
                .defineInRange("Explosion Radius Log Base", 2.75, 1.0, 10.0);
        EXPLOSION_RADIUS_LOG_SCALAR = BUILDER.comment("Logarithm scalar for calculating bomb vest explosion radius")
                .defineInRange("Explosion Radius Log Scalar", 10, 1, 25);

        DYNAMITE_EXPLOSION_VALUE = BUILDER.comment("Explosiveness of Dynamite")
                .defineInRange("Dynamite Explosiveness", 1.0, 0.1, 10.0);
        VOLATILE_DYNAMITE_EXPLOSION_VALUE = BUILDER.comment("Explosiveness of Volatile Dynamite")
                .defineInRange("Volatile Dynamite Explosiveness", 1.65, 0.1, 10.0);
        HIGH_EXPLOSIVE_EXPLOSION_VALUE = BUILDER.comment("Explosiveness of High-Explosive Dynamite")
                .defineInRange("High-Explosive Dynamite Explosiveness", 2.75, 0.1, 10.0);
        SHRAPNEL_EXPLOSION_VALUE = BUILDER.comment("Explosiveness of Fragmenting Dynamite")
                .defineInRange("Fragmenting Dynamite Explosiveness", 1.1, 0.1, 10.0);
        PACKED_EXPLOSION_VALUE = BUILDER.comment("Explosiveness of Packed Dynamite")
                .defineInRange("Packed Dynamite Explosiveness", 2.75F, 0.1, 10.0);

        EXPLODE_BLOCKS = BUILDER.comment("When false, Bomb Vests will not break blocks")
                .define("Explode Blocks", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
