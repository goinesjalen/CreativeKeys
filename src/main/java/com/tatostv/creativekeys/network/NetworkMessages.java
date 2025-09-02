package com.tatostv.creativekeys.network;

import com.tatostv.creativekeys.CreativeKeys;
import com.tatostv.creativekeys.item.CreativeKeyItem;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@EventBusSubscriber(modid = CreativeKeys.MODID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkMessages {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1"); // Version 1 - increment if packet format changes

        registrar.playToClient(
                SyncExpiresPayload.TYPE,
                SyncExpiresPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    try {
                        Minecraft mc = Minecraft.getInstance();
                        if (mc.player != null) {
                            mc.player.getPersistentData().putLong(CreativeKeyItem.NBT_EXPIRES, payload.expires());
                        }
                    } catch (Exception e) {
                        CreativeKeys.LOGGER.error("Failed to handle SyncExpiresPayload: {}", e.getMessage());
                    }
                })
        );

        registrar.playToClient(
                SyncPausedRemainingPayload.TYPE,
                SyncPausedRemainingPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    try {
                        Minecraft mc = Minecraft.getInstance();
                        if (mc.player != null) {
                            mc.player.getPersistentData().putLong("creative_keys:paused_remaining", payload.remaining());
                        }
                    } catch (Exception e) {
                        CreativeKeys.LOGGER.error("Failed to handle SyncPausedRemainingPayload: {}", e.getMessage());
                    }
                })
        );
    }

    // Helper methods with null safety
    public static void sendExpires(ServerPlayer player, long expires) {
        if (player != null && player.connection != null) {
            try {
                player.connection.send(new ClientboundCustomPayloadPacket(new SyncExpiresPayload(expires)));
            } catch (Exception e) {
                CreativeKeys.LOGGER.error("Failed to send expires packet to {}: {}", 
                    player.getName().getString(), e.getMessage());
            }
        }
    }

    public static void sendPausedRemaining(ServerPlayer player, long remaining) {
        if (player != null && player.connection != null) {
            try {
                player.connection.send(new ClientboundCustomPayloadPacket(new SyncPausedRemainingPayload(remaining)));
            } catch (Exception e) {
                CreativeKeys.LOGGER.error("Failed to send paused remaining packet to {}: {}", 
                    player.getName().getString(), e.getMessage());
            }
        }
    }
}