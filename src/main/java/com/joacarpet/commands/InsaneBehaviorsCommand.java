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

import com.joacarpet.InsaneBehaviors;
import com.joacarpet.JoaCarpetMod;
import com.joacarpet.JoaCarpetSettings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import java.util.Objects;

//#if MC >= 11900
//$$ import carpet.utils.CommandHelper;
//#else
import carpet.settings.SettingsManager;
//#endif

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class InsaneBehaviorsCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                literal("insanebehaviors")
                        .requires(c -> !Objects.equals(JoaCarpetSettings.insaneBehaviors, "normal") &&
                                //#if MC >= 11900
//$$                                 CommandHelper.canUseCommand(c, JoaCarpetSettings.commandInsaneBehaviors))
                                //#else
                                SettingsManager.canUseCommand(c, JoaCarpetSettings.commandInsaneBehaviors))
                                //#endif


                        .executes(c -> 1)
                        .then(
                                literal("reset")
                                        .executes(c -> InsaneBehaviors.resetCounterAndResolution(c.getSource()))
                        )
                        .then(
                                literal("getstate")
                                        .executes(c -> InsaneBehaviors.getState(c.getSource()))
                        )
                        .then(
                                literal("setstate")
                                        .then(argument("resolution", IntegerArgumentType.integer(2))
                                        .then(argument("counter", IntegerArgumentType.integer(0))
                                        .executes(c -> InsaneBehaviors.setState(c.getSource(), IntegerArgumentType.getInteger(c, "resolution"), IntegerArgumentType.getInteger(c, "counter")))
                        ))));

        JoaCarpetMod.LOGGER.info("insanebehaviors command registered");
    }
}
