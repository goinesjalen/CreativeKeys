package com.tatostv.creativekeys.network;

import com.tatostv.creativekeys.CreativeKeys;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SyncPausedRemainingPayload(long remaining) implements CustomPacketPayload {
    public static final Type<SyncPausedRemainingPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(CreativeKeys.MODID, "sync_paused_remaining"));

    public static final StreamCodec<FriendlyByteBuf, SyncPausedRemainingPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_LONG, SyncPausedRemainingPayload::remaining,
                    SyncPausedRemainingPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

