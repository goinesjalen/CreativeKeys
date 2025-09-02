package com.tatostv.creativekeys.network;

import com.tatostv.creativekeys.CreativeKeys;
import com.tatostv.creativekeys.item.CreativeKeyItem;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@EventBusSubscriber(modid = CreativeKeys.MODID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkMessages {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");

        registrar.playToClient(
                SyncExpiresPayload.TYPE,
                SyncExpiresPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    Minecraft mc = Minecraft.getInstance();
                    if (mc.player != null) {
                        mc.player.getPersistentData().putLong(CreativeKeyItem.NBT_EXPIRES, payload.expires());
                    }
                })
        );

        registrar.playToClient(
                SyncPausedRemainingPayload.TYPE,
                SyncPausedRemainingPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    Minecraft mc = Minecraft.getInstance();
                    if (mc.player != null) {
                        mc.player.getPersistentData().putLong("creative_keys:paused_remaining", payload.remaining());
                    }
                })
        );
    }

    // Helper: send to a specific player
    public static void sendExpires(ServerPlayer player, long expires) {
        player.connection.send(new ClientboundCustomPayloadPacket(new SyncExpiresPayload(expires)));
    }

    public static void sendPausedRemaining(ServerPlayer player, long remaining) {
        player.connection.send(new ClientboundCustomPayloadPacket(new SyncPausedRemainingPayload(remaining)));
    }
}
