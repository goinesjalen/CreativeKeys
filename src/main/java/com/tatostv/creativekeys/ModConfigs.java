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

    // Currently no config options are needed for this simple mod
    // Future options can be added here as needed
    
    // Final built config specification (registered in CreativeKeys.java)
    static final ModConfigSpec SPEC = BUILDER.build();
}