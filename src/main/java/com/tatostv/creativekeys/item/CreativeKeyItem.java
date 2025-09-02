package com.tatostv.creativekeys.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;

import java.util.List;
import com.tatostv.creativekeys.network.NetworkMessages;

public class CreativeKeyItem extends Item {
    // NBT keys to store expiration time and the player’s previous gamemode
    public static final String NBT_EXPIRES = "creative_keys:expires";
    public static final String NBT_PREV_GAMEMODE = "creative_keys:prev_gamemode";

    // 30 minutes in Minecraft ticks (20 ticks = 1 second)
    public static final long DURATION_TICKS = 20L * 60L * 30L;

    public CreativeKeyItem(Properties properties) {
        super(properties);
    }

    /**
     * Called when the player right-clicks with this item.
     * Grants/extends Creative Mode for 30 minutes and consumes one key.
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Only run logic on the server side
        if (!(player instanceof ServerPlayer sp)) {
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }

        long now = sp.serverLevel().getGameTime(); // Current world time in ticks
        long expiresAt = sp.getPersistentData().getLong(NBT_EXPIRES); // Time Creative expires

        // Always ensure player is in Creative. Only save previous gamemode
        // when (re)starting a session.
        sp.displayClientMessage(
                Component.literal("Creative Enabled")
                        .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC),
                true);
        sp.setGameMode(GameType.CREATIVE);

        // Extend the timer: either from existing expiration, or from now if expired
        long newExpires = Math.max(expiresAt, now) + DURATION_TICKS;
        sp.getPersistentData().putLong(NBT_EXPIRES, newExpires);
        // Sync to client
        NetworkMessages.sendExpires(sp, newExpires);
        // Consume one key item
        stack.shrink(1);

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    /**
     * Adds a tooltip to the item when hovered in the inventory.
     * Displays remaining Creative time if active, or instructions if inactive.
     */
    @Override
    public void appendHoverText(ItemStack stack,
            Item.TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag) {
        // Always show the same simple usage line
        tooltip.add(Component.literal("Right-click to gain 30m Creative")
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

}
