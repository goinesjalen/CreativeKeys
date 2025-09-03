package com.tatostv.creativekeys;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Holds all configuration options for the Creative Keys mod.
 * These values are written to and read from the generated
 * config file: creative_keys-common.toml
 */
public class ModConfigs {
    // Central builder for defining configuration options
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // Whether to clear inventory when Creative mode expires
    public static final ModConfigSpec.BooleanValue CLEAR_INVENTORY_ON_EXPIRE;

    static {
        BUILDER.comment("Creative Keys Configuration");
        
        CLEAR_INVENTORY_ON_EXPIRE = BUILDER
            .comment("Whether to clear player inventory when Creative mode expires")
            .define("clearInventoryOnExpire", true);
    }
    
    // Final built config specification (registered in CreativeKeys.java)
    static final ModConfigSpec SPEC = BUILDER.build();
}