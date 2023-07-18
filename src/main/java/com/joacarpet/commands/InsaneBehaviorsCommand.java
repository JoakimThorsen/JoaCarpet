package com.joacarpet.commands;

import carpet.utils.CommandHelper;
import com.joacarpet.InsaneBehaviors;
import com.joacarpet.JoaCarpetMod;
import com.joacarpet.JoaCarpetSettings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;

import java.util.Objects;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class InsaneBehaviorsCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                literal("insanebehaviors")
                        .requires(c -> !Objects.equals(JoaCarpetSettings.insaneBehaviors, "normal") &&
                                CommandHelper.canUseCommand(c, JoaCarpetSettings.commandInsaneBehaviors))
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
