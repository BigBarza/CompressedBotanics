package com.pression.compressedbotanics;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> ELYTRA_MANA_REPAIR;
    public static final ForgeConfigSpec.ConfigValue<Integer> ELYTRA_MANA_DAMAGE_COST;
    public static final ForgeConfigSpec.ConfigValue<Integer> ELYTRA_MANA_REPAIR_COST;
    public static final ForgeConfigSpec.ConfigValue<Boolean> COCOON_NO_EMERALD;
    public static final ForgeConfigSpec.ConfigValue<Boolean> COCOON_NO_CHORUS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> COCOON_NO_GAIA;
    public static final ForgeConfigSpec.ConfigValue<Double> COCOON_RARE_CHANCE;

    static {
        BUILDER.push("Compressed Botanics Config");

        ELYTRA_MANA_REPAIR = BUILDER.comment("Whether Elytras can repair themselves with mana, like manasteel armor")
                        .define("Enable Elytra Mana Repair", true);
        ELYTRA_MANA_DAMAGE_COST = BUILDER.comment("How much mana should the elytra draw instead of taking a point of durability. Manasteel armor is 70.")
                        .define("Elytra Damage Negation Cost", 100);
        ELYTRA_MANA_REPAIR_COST = BUILDER.comment("How much mana should the elytra draw to repair a previously lost point of durability. Manasteel armor is 140.")
                .define("Elytra Damage Repair Cost", 200);
        COCOON_NO_EMERALD = BUILDER.comment("Whether to block emeralds from being used on the Cocoon of Caprice, making it unable to spawn villagers")
                        .define("No Villagers from Cocoon", false);
        COCOON_NO_CHORUS = BUILDER.comment("Whether to block chorus fruits from being used on the Cocoon of Caprice, making it unable to spawn shulkers")
                .define("No Shulkers from Cocoon", false);
        COCOON_NO_GAIA = BUILDER.comment("Whether to block gaia spirits from being used on the Cocoon of Caprice, forcing players to use the normal rare mob chances")
                .define("No guaranteed rares from Cocoon", false);
        COCOON_RARE_CHANCE = BUILDER.comment("The chance that a Cocoon of Caprice spawns from the rare list instead of the common list. Default is 0.075 or 7.5%")
                .defineInRange("Cocoon Rare Chance", 0.075, 0,1);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
