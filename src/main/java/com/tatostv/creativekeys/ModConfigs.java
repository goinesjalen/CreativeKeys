package com.tatostv.creativekeys;

import java.util.List;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Holds all configuration options for the Creative Keys mod.
 * These values are written to and read from the generated
 * config file: creative_keys-common.toml
 */
public class ModConfigs {
    // Central builder for defining configuration options
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    /**
     * Toggle for enabling or disabling the Creative Key item.
     * Default = true
     * 
     * If set to false, the Creative Key will not be registered/usable.
     */
    public static final ModConfigSpec.BooleanValue ENABLE_CREATIVE_KEY = BUILDER
            .comment("Whether the Creative Key item is enabled")
            .define("enableCreativeKey", true);

    /**
     * An optional list of tags/notes for the Creative Key.
     * Default = empty list []
     * 
     * This can be used for adding metadata or descriptions.
     * Only accepts strings.
     */
    public static final ModConfigSpec.ConfigValue<List<? extends String>> KEY_TAGS = BUILDER
            .comment("Optional list of tags or notes associated with the key")
            .defineListAllowEmpty(
                    // Config key
                    "keyTags",
                    // Default value
                    List.of(),
                    // Default element supplier
                    () -> "",
                    // Validator: ensures every entry is a String
                    o -> o instanceof String
            );

    // Final built config specification (registered in CreativeKeys.java)
    static final ModConfigSpec SPEC = BUILDER.build();
}
