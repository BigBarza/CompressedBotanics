package com.pression.compressedbotanics;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> ELYTRA_MANA_REPAIR;
    public static final ForgeConfigSpec.ConfigValue<Integer> ELYTRA_MANA_DAMAGE_COST;
    public static final ForgeConfigSpec.ConfigValue<Integer> ELYTRA_MANA_REPAIR_COST;

    static {
        BUILDER.push("Compressed Botanics Config");

        ELYTRA_MANA_REPAIR = BUILDER.comment("Whether Elytras can repair themselves with mana, like manasteel armor")
                        .define("Enable Elytra Mana Repair", true);
        ELYTRA_MANA_DAMAGE_COST = BUILDER.comment("How much mana should the elytra draw instead of taking a point of durability. Manasteel armor is 70.")
                        .define("Elytra Repair Mana Cost", 70);
        ELYTRA_MANA_REPAIR_COST = BUILDER.comment("How much mana should the elytra draw to repair a previously lost point of durability. Manasteel armor is 140.")
                .define("Elytra Repair Mana Cost", 140);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
