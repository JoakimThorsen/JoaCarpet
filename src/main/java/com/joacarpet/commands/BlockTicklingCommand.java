/*
 * This file is part of the JoaCarpet project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Joa and contributors
 *
 * JoaCarpet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JoaCarpet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JoaCarpet.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.joacarpet.commands;

import com.joacarpet.BlockTicklingSetting;
import com.joacarpet.JoaCarpetSettings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import carpet.utils.Messenger;
import carpet.settings.SettingsManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.SharedSuggestionProvider.suggest;

public class BlockTicklingCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(literal("blocktickling")
                .requires((player) -> SettingsManager.canUseCommand(player, JoaCarpetSettings.commandBlockTickling))
                .then(argument("BlockTicklingSetting", StringArgumentType.word())
                        .suggests((c, b) -> suggest(BlockTicklingSetting.commandKeys(), b))
                        .then(argument("value", BoolArgumentType.bool())
                                .executes(BlockTicklingCommand::setBlockTicklingSetting)
                        )
                        .executes(BlockTicklingCommand::getBlockTicklingSetting)
                )
        );
    }

    private static String k(BlockTicklingSetting blockTicklingSetting)
    {
        return "c " + blockTicklingSetting.getName();
    }

    private static String v(boolean value)
    {
        return (value ? "l " : "r ") + value;
    }

    private static int setBlockTicklingSetting(CommandContext<CommandSourceStack> c)
    {
        try
        {
            ServerPlayer player = c.getSource().getPlayerOrException();
            BlockTicklingSetting i = BlockTicklingSetting.fromCommandKey(getString(c, "BlockTicklingSetting"));
            boolean value = getBool(c, "value");
            BlockTicklingSetting.set(i, player.getUUID(), value);
            Messenger.m(c.getSource(), "g BlockTickling setting ", k(i), "g  set to ", v(value));
            return 0;
        }
        catch(CommandSyntaxException e)
        {
            Messenger.m(c.getSource(), "r BlockTickling command must be executed by a player");
            return 1;
        }
    }

    private static int getBlockTicklingSetting(CommandContext<CommandSourceStack> c)
    {
        try
        {
            ServerPlayer player = c.getSource().getPlayerOrException();
            BlockTicklingSetting i = BlockTicklingSetting.fromCommandKey(getString(c, "BlockTicklingSetting"));
            Messenger.m(c.getSource(), "g BlockTickling setting ", k(i), "g  is currently set to ", v(BlockTicklingSetting.get(i, player.getUUID())));
            return 0;
        }
        catch(CommandSyntaxException e)
        {
            Messenger.m(c.getSource(), "r BlockTickling command must be executed by a player");
            return 1;
        }
    }
}
