package com.tatostv.creativekeys.network;

import com.tatostv.creativekeys.CreativeKeys;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SyncExpiresPayload(long expires) implements CustomPacketPayload {
    public static final Type<SyncExpiresPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(CreativeKeys.MODID, "sync_expires"));

    public static final StreamCodec<FriendlyByteBuf, SyncExpiresPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_LONG, SyncExpiresPayload::expires,
                    SyncExpiresPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

