package com.tatostv.creativekeys.item;

import com.tatostv.creativekeys.network.NetworkMessages;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

public class CreativeKeyItem extends Item {
    public static final String NBT_EXPIRES = "creative_keys:expires";
    public static final String NBT_PREV_GAMEMODE = "creative_keys:prev_gamemode";
    public static final long DURATION_TICKS = 20L * 60L * 30L; // 30 minutes

    public CreativeKeyItem(Properties properties) {
        super(properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Component getName(ItemStack stack) {
        // Only do animated rainbow on client side
        if (Minecraft.getInstance().level != null) {
            return createAnimatedRainbowName();
        }
        // Fallback for server or when client not ready
        return createStaticRainbowName();
    }

    @OnlyIn(Dist.CLIENT)
    private Component createAnimatedRainbowName() {
        String text = "Creative Key";
        MutableComponent rainbow = Component.empty();
        
        // Use game time for smooth animation
        long time = Minecraft.getInstance().level.getGameTime();
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == ' ') {
                rainbow.append(Component.literal(" "));
            } else {
                // Calculate color based on position and time for wave effect
                float hue = ((float)(i * 30 + time * 2) % 360) / 360.0f;
                int rgb = java.awt.Color.HSBtoRGB(hue, 1.0f, 1.0f);
                
                Component coloredChar = Component.literal(String.valueOf(c))
                        .withStyle(Style.EMPTY.withColor(rgb));
                rainbow.append(coloredChar);
            }
        }
        
        return rainbow;
    }

    private Component createStaticRainbowName() {
        String text = "Creative Key";
        MutableComponent rainbow = Component.empty();
        
        ChatFormatting[] colors = {
            ChatFormatting.RED,
            ChatFormatting.GOLD, 
            ChatFormatting.YELLOW,
            ChatFormatting.GREEN,
            ChatFormatting.AQUA,
            ChatFormatting.BLUE,
            ChatFormatting.LIGHT_PURPLE
        };
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == ' ') {
                rainbow.append(Component.literal(" "));
            } else {
                ChatFormatting color = colors[i % colors.length];
                rainbow.append(Component.literal(String.valueOf(c)).withStyle(color));
            }
        }
        
        return rainbow;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!(player instanceof ServerPlayer sp)) {
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }

        long now = sp.serverLevel().getGameTime();
        long expiresAt = sp.getPersistentData().getLong(NBT_EXPIRES);

        sp.displayClientMessage(
                Component.literal("Creative Enabled")
                        .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC),
                true);
        sp.setGameMode(GameType.CREATIVE);

        long newExpires = Math.max(expiresAt, now) + DURATION_TICKS;
        sp.getPersistentData().putLong(NBT_EXPIRES, newExpires);
        NetworkMessages.sendExpires(sp, newExpires);
        stack.shrink(1);

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Right-click to gain 30m Creative")
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    /**
     * Expires the creative mode for a player, clearing their inventory and reverting to survival
     */
    public static void expireCreativeMode(ServerPlayer player) {
        if (player == null) return;

        try {
            // Clear NBT first to prevent loops
            player.getPersistentData().remove(NBT_EXPIRES);
            player.getPersistentData().remove(NBT_PREV_GAMEMODE);
            
            // Clear inventory before reverting
            clearPlayerInventory(player);
            
            // Revert to survival
            player.setGameMode(GameType.SURVIVAL);
            
            player.displayClientMessage(
                    Component.literal("Creative expired. Inventory cleared and returned to Survival.")
                            .withStyle(ChatFormatting.RED),
                    true);
            
            // Sync to client
            NetworkMessages.sendExpires(player, 0L);
        } catch (Exception e) {
            // Log error but don't crash
            System.err.println("Error expiring creative mode for " + player.getName().getString() + ": " + e.getMessage());
        }
    }

    /**
     * Clears all items from the player's inventory, armor, and offhand
     */
    public static void clearPlayerInventory(ServerPlayer player) {
        if (player == null) return;

        try {
            // Clear main inventory (hotbar + main inventory slots)
            player.getInventory().clearContent();
            
            // Clear armor slots
            for (int i = 0; i < player.getInventory().armor.size(); i++) {
                player.getInventory().armor.set(i, ItemStack.EMPTY);
            }
            
            // Clear offhand
            for (int i = 0; i < player.getInventory().offhand.size(); i++) {
                player.getInventory().offhand.set(i, ItemStack.EMPTY);
            }
            
            // Sync inventory to client
            player.inventoryMenu.broadcastChanges();
            
        } catch (Exception e) {
            // Log error but don't crash
            System.err.println("Failed to clear inventory for " + player.getName().getString() + ": " + e.getMessage());
        }
    }
}