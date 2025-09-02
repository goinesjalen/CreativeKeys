package com.tatostv.creativekeys.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.tatostv.creativekeys.CreativeKeys;
import com.tatostv.creativekeys.item.CreativeKeyItem;
import com.tatostv.creativekeys.network.NetworkMessages;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

import java.util.Collection;

public class CreativeKeysCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("creativekeys")
                .requires(src -> src.hasPermission(2));

        // /creativekeys give <targets>
        root.then(Commands.literal("give")
                .then(Commands.argument("targets", EntityArgument.players())
                        .executes(ctx -> {
                            Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
                            for (ServerPlayer sp : targets) {
                                sp.addItem(CreativeKeys.CREATIVE_KEY.get().getDefaultInstance());
                                sp.sendSystemMessage(Component.literal("You received a Creative Key!"));
                            }
                            ctx.getSource().sendSuccess(() -> Component.literal("Creative Key given."), true);
                            return 1;
                        })));

        // /creativekeys pause <targets>
        root.then(Commands.literal("pause")
                .then(Commands.argument("targets", EntityArgument.players())
                        .executes(ctx -> {
                            Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
                            for (ServerPlayer sp : targets) {
                                long expires = sp.getPersistentData().getLong(CreativeKeyItem.NBT_EXPIRES);
                                if (expires > 0) {
                                    long now = sp.serverLevel().getGameTime();
                                    long remaining = expires - now;
                                    sp.getPersistentData().putLong("creative_keys:paused_remaining", remaining);
                                    sp.getPersistentData().putLong(CreativeKeyItem.NBT_EXPIRES, 0);
                                    NetworkMessages.sendExpires(sp, 0L);
                                    NetworkMessages.sendPausedRemaining(sp, remaining);
                                    sp.sendSystemMessage(Component.literal("Creative timer paused."));
                                } else {
                                    sp.sendSystemMessage(Component.literal("No active Creative timer to pause."));
                                }
                            }
                            return 1;
                        })));

        // /creativekeys reset <targets>
        root.then(Commands.literal("reset")
                .then(Commands.argument("targets", EntityArgument.players())
                        .executes(ctx -> {
                            Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
                            for (ServerPlayer sp : targets) {
                                sp.getPersistentData().remove(CreativeKeyItem.NBT_EXPIRES);
                                sp.getPersistentData().remove("creative_keys:paused_remaining");
                                NetworkMessages.sendExpires(sp, 0L);
                                NetworkMessages.sendPausedRemaining(sp, 0L);
                                sp.sendSystemMessage(Component.literal("Creative timer reset."));
                            }
                            return 1;
                        })));

        // /creativekeys resume <targets>
        root.then(Commands.literal("resume")
                .then(Commands.argument("targets", EntityArgument.players())
                        .executes(ctx -> {
                            Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
                            for (ServerPlayer sp : targets) {
                                long remaining = sp.getPersistentData().getLong("creative_keys:paused_remaining");
                                if (remaining > 0) {
                                    long now = sp.serverLevel().getGameTime();
                                    long newExpires = now + remaining;
                                    sp.getPersistentData().putLong(CreativeKeyItem.NBT_EXPIRES, newExpires);
                                    sp.getPersistentData().remove("creative_keys:paused_remaining");
                                    sp.setGameMode(GameType.CREATIVE);
                                    NetworkMessages.sendExpires(sp, newExpires);
                                    NetworkMessages.sendPausedRemaining(sp, 0L);
                                    sp.sendSystemMessage(Component.literal("Creative timer resumed."));
                                } else {
                                    sp.sendSystemMessage(Component.literal("No paused timer to resume."));
                                }
                            }
                            return 1;
                        })));

        // /creativekeys add <targets> <seconds>
        root.then(Commands.literal("add")
                .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.argument("seconds", IntegerArgumentType.integer(1))
                                .executes(ctx -> {
                                    Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
                                    int seconds = IntegerArgumentType.getInteger(ctx, "seconds");
                                    long delta = Math.max(1, seconds) * 20L;
                                    for (ServerPlayer sp : targets) {
                                        long paused = sp.getPersistentData().getLong("creative_keys:paused_remaining");
                                        if (paused > 0) {
                                            long newPaused = paused + delta;
                                            sp.getPersistentData().putLong("creative_keys:paused_remaining", newPaused);
                                            NetworkMessages.sendPausedRemaining(sp, newPaused);
                                            NetworkMessages.sendExpires(sp, 0L);
                                        } else {
                                            long now = sp.serverLevel().getGameTime();
                                            long expires = sp.getPersistentData().getLong(CreativeKeyItem.NBT_EXPIRES);
                                            if (expires > now) {
                                                long newExpires = expires + delta;
                                                sp.getPersistentData().putLong(CreativeKeyItem.NBT_EXPIRES, newExpires);
                                                NetworkMessages.sendExpires(sp, newExpires);
                                                sp.sendSystemMessage(Component.literal(
                                                        "Creative time increased by " + seconds + "s."));
                                            } else {
                                                sp.sendSystemMessage(Component.literal(
                                                        "No active or paused timer to add to."));
                                            }
                                        }
                                    }
                                    return 1;
                                }))));

        // /creativekeys subtract <targets> <seconds>
        root.then(Commands.literal("subtract")
                .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.argument("seconds", IntegerArgumentType.integer(1))
                                .executes(ctx -> {
                                    Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
                                    int seconds = IntegerArgumentType.getInteger(ctx, "seconds");
                                    long delta = Math.max(1, seconds) * 20L;
                                    for (ServerPlayer sp : targets) {
                                        long paused = sp.getPersistentData().getLong("creative_keys:paused_remaining");
                                        if (paused > 0) {
                                            long newPaused = Math.max(0L, paused - delta);
                                            if (newPaused == 0L) {
                                                sp.getPersistentData().remove("creative_keys:paused_remaining");
                                            } else {
                                                sp.getPersistentData().putLong("creative_keys:paused_remaining",
                                                        newPaused);
                                            }
                                            NetworkMessages.sendPausedRemaining(sp, newPaused);
                                            NetworkMessages.sendExpires(sp, 0L);
                                            sp.sendSystemMessage(Component.literal(
                                                    "Creative paused time decreased by " + seconds + "s."));
                                        } else {
                                            long now = sp.serverLevel().getGameTime();
                                            long expires = sp.getPersistentData().getLong(CreativeKeyItem.NBT_EXPIRES);
                                            long newExpires = Math.max(0L, expires - delta);
                                            if (expires > now && newExpires <= now) {
                                                // expire immediately
                                                int prevId = sp.getPersistentData()
                                                        .getInt(CreativeKeyItem.NBT_PREV_GAMEMODE);
                                                GameType target = GameType.byId(prevId);
                                                if (target == null)
                                                    target = GameType.SURVIVAL;
                                                sp.getPersistentData().remove(CreativeKeyItem.NBT_EXPIRES);
                                                sp.getPersistentData().remove(CreativeKeyItem.NBT_PREV_GAMEMODE);
                                                sp.gameMode.changeGameModeForPlayer(target);
                                                NetworkMessages.sendExpires(sp, 0L);
                                                sp.sendSystemMessage(Component.literal("Creative expired."));
                                            } else if (expires > now) {
                                                sp.getPersistentData().putLong(CreativeKeyItem.NBT_EXPIRES, newExpires);
                                                NetworkMessages.sendExpires(sp, newExpires);
                                                sp.sendSystemMessage(Component.literal(
                                                        "Creative time decreased by " + seconds + "s."));
                                            } else {
                                                sp.sendSystemMessage(Component.literal(
                                                        "No active or paused timer to subtract from."));
                                            }
                                        }
                                    }
                                    return 1;
                                }))));

        dispatcher.register(root);
    }
}
