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
        // Null safety and type checking
        if (!(event.getEntity() instanceof ServerPlayer sp) || sp.isRemoved()) {
            return;
        }

        try {
            long now = sp.serverLevel().getGameTime();
            long expires = sp.getPersistentData().getLong(CreativeKeyItem.NBT_EXPIRES);

            if (expires > 0 && now >= expires) {
                // Clear NBT first to prevent infinite loops
                sp.getPersistentData().remove(CreativeKeyItem.NBT_EXPIRES);
                
                // Revert to survival (simple and safe)
                sp.setGameMode(GameType.SURVIVAL);
                
                sp.displayClientMessage(
                        Component.literal("Creative expired. Returning to Survival.")
                                .withStyle(ChatFormatting.RED),
                        true);
                
                // Sync to client
                NetworkMessages.sendExpires(sp, 0L);
            }
        } catch (Exception e) {
            // Log error but don't crash the server
            CreativeKeys.LOGGER.error("Error in Creative Keys player tick for {}: {}", 
                sp.getName().getString(), e.getMessage());
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        // Null safety
        if (!(event.getEntity() instanceof ServerPlayer sp)) {
            return;
        }

        try {
            // Sync current state to newly connected client
            long expires = sp.getPersistentData().getLong(CreativeKeyItem.NBT_EXPIRES);
            NetworkMessages.sendExpires(sp, expires);
            
            long pausedRemaining = sp.getPersistentData().getLong("creative_keys:paused_remaining");
            NetworkMessages.sendPausedRemaining(sp, pausedRemaining);
        } catch (Exception e) {
            // Log but don't prevent login
            CreativeKeys.LOGGER.warn("Failed to sync Creative Keys data for {}: {}", 
                sp.getName().getString(), e.getMessage());
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        try {
            com.tatostv.creativekeys.command.CreativeKeysCommand.register(event.getDispatcher());
        } catch (Exception e) {
            CreativeKeys.LOGGER.error("Failed to register Creative Keys commands: {}", e.getMessage());
        }
    }
}