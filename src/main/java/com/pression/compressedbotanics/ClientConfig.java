package com.pression.compressedbotanics;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;


    public static final ForgeConfigSpec.ConfigValue<Integer> RED_STRING_WIDTH;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RED_STRING_IN_PONDERS;
    public static final ForgeConfigSpec.ConfigValue<Double> SPARK_STAR_SCALE_BASE;
    public static final ForgeConfigSpec.ConfigValue<Double> SPARK_STAR_SCALE_PONDER;

    static {
        BUILDER.push("Compressed Botanics Client Config");

        RED_STRING_WIDTH = BUILDER.comment("A multiplier on the render thickness for red string. Increasing it will make it easier to see. Do not go overboard.")
                        .define("Red String Render Thickness", 1);
        RED_STRING_IN_PONDERS = BUILDER.comment("Whether red string blocks should always render the string in ponders, regardless of what the player's holding.")
                .define("Always show red string in ponders", true);
        SPARK_STAR_SCALE_BASE = BUILDER.comment("A multiplier on the size of the color indicator on sparks.")
                .defineInRange("Spark Star Base Scale", 1d, 0.1d, 10d);
        SPARK_STAR_SCALE_PONDER = BUILDER.comment("A multiplier on the size of the color indicator on sparks. This one is for ponders and ignores the base scale")
                .defineInRange("Spark Star Ponder Scale", 1d, 0.1d, 10d);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
