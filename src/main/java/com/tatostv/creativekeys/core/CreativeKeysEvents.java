package com.tatostv.creativekeys.core;

import com.tatostv.creativekeys.CreativeKeys;
import com.tatostv.creativekeys.item.CreativeKeyItem;
import com.tatostv.creativekeys.network.NetworkMessages;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = CreativeKeys.MODID)
public class CreativeKeysEvents {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer sp))
            return;

        long now = sp.serverLevel().getGameTime();
        long expires = sp.getPersistentData().getLong(CreativeKeyItem.NBT_EXPIRES);

        if (expires > 0 && now >= expires) {
            // Time’s up 
            // Clear NBT and revert
            sp.getPersistentData().remove(CreativeKeyItem.NBT_EXPIRES);

            sp.setGameMode(GameType.SURVIVAL);
            sp.displayClientMessage(
                    Component.literal("Creative expired. Returning to " + niceName(GameType.SURVIVAL) + ".")
                            .withStyle(ChatFormatting.RED),
                    true);
            // Sync clear to client
            NetworkMessages.sendExpires(sp, 0L);
        }
    }

    private static String niceName(GameType type) {
        return switch (type) {
            case SURVIVAL -> "Survival";
            case CREATIVE -> "Creative";
            case ADVENTURE -> "Adventure";
            case SPECTATOR -> "Spectator";
        };
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer sp)) return;
        long expires = sp.getPersistentData().getLong(CreativeKeyItem.NBT_EXPIRES);
        // Always send current value to initialize client-side HUD state
        NetworkMessages.sendExpires(sp, expires);
        long pausedRemaining = sp.getPersistentData().getLong("creative_keys:paused_remaining");
        NetworkMessages.sendPausedRemaining(sp, pausedRemaining);
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        com.tatostv.creativekeys.command.CreativeKeysCommand.register(event.getDispatcher());
    }
}
