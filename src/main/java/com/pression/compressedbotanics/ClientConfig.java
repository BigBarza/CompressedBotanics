package com.pression.compressedbotanics;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;


    public static final ForgeConfigSpec.ConfigValue<Integer> RED_STRING_WIDTH;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RED_STRING_IN_PONDERS;

    static {
        BUILDER.push("Compressed Botanics Client Config");

        RED_STRING_WIDTH = BUILDER.comment("A multiplier on the render thickness for red string. Increasing it will make it easier to see. Do not go overboard.")
                        .define("Red String Render Thickness", 1);
        RED_STRING_IN_PONDERS = BUILDER.comment("Whether red string blocks should always render the string in ponders, regardless of what the player's holding.")
                .define("Always show red string in ponders", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
