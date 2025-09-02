// src/main/java/com/tatostv/creativekeys/client/ClientOverlays.java
package com.tatostv.creativekeys.client;

import com.tatostv.creativekeys.CreativeKeys;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = CreativeKeys.MODID, value = Dist.CLIENT)
public class ClientOverlays {
    @SubscribeEvent
    public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
        var id = ResourceLocation.fromNamespaceAndPath(CreativeKeys.MODID, "creative_key_timer");
        event.registerAbove(VanillaGuiLayers.HOTBAR, id, new CreativeKeyHudOverlay());

        var pausedId = ResourceLocation.fromNamespaceAndPath(CreativeKeys.MODID, "creative_key_timer_paused");
        event.registerAbove(VanillaGuiLayers.HOTBAR, pausedId, new PausedKeyHudOverlay());
    }
}
