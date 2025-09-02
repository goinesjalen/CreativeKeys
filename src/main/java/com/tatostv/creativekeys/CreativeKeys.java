package com.tatostv.creativekeys;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import com.tatostv.creativekeys.item.CreativeKeyItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Main mod class for Creative Keys.
 * Handles registration of items, creative tabs, and configuration.
 */
@Mod(CreativeKeys.MODID)
public class CreativeKeys {
    // The mod ID (must match build.gradle and gradle.properties)
    public static final String MODID = "creative_keys";

    // Logger for debugging and informational messages
    public static final Logger LOGGER = LogUtils.getLogger();

    // Deferred register for all items in this mod
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    // Deferred register for creative mode tabs (custom creative tab for the mod)
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Register the "Creative Key" item
    public static final DeferredItem<Item> CREATIVE_KEY =
            ITEMS.register("creative_key", () -> new CreativeKeyItem(new Item.Properties()));

    // Register the custom creative tab for this mod
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_TAB =
            CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + MODID)) // Tab name (uses lang file key)
                    .withTabsBefore(CreativeModeTabs.TOOLS_AND_UTILITIES) // Position before Tools tab
                    .icon(() -> CREATIVE_KEY.get().getDefaultInstance()) // Icon for the tab
                    .displayItems((parameters, output) -> {
                        output.accept(CREATIVE_KEY.get()); // Show Creative Key in this tab
                    })
                    .build());

    /**
     * Constructor – called when the mod is loaded.
     * Registers items, tabs, events, and configuration.
     */
    public CreativeKeys(IEventBus modEventBus, ModContainer modContainer) {
        // Register item and tab registries to the event bus
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        // Add listener to insert our item into vanilla creative tabs
        modEventBus.addListener(this::addCreative);

        // Register configuration file (common config type)
        modContainer.registerConfig(ModConfig.Type.COMMON, ModConfigs.SPEC);
    }

    /**
     * Adds our Creative Key to the vanilla "Tools and Utilities" tab.
     */
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(CREATIVE_KEY);
        }
    }
}
