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

package com.joacarpet;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.utils.Messenger;
import carpet.utils.Translations;
import com.joacarpet.commands.InsaneBehaviorsCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import java.util.Map;



//#if MC >= 11900
import net.minecraft.commands.CommandBuildContext;
//#endif
//#if MC >= 11800
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//#else
//$$ import org.apache.logging.log4j.LogManager;
//$$ import org.apache.logging.log4j.Logger;
//#endif


public class JoaCarpetMod implements ModInitializer, CarpetExtension {
    public static final Logger LOGGER =
    //#if MC >= 11800
            LoggerFactory.getLogger("joacarpet");
    //#else
//$$     LogManager.getLogger("joacarpet");
    //#endif


    @Override
    public void onInitialize() {}

    static {
        CarpetServer.manageExtension(new JoaCarpetMod());
    }

    @Override
    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher
        //#if MC >= 11900
        , final CommandBuildContext commandBuildContext
        //#endif
    ) {
        InsaneBehaviorsCommand.register(dispatcher);
    }

    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(JoaCarpetSettings.class);
    }

    @Override
    public String version() {
        return "joacarpet";
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return Translations.getTranslationFromResourcePath(String.format("assets/joacarpet/lang/%s.json", lang));
    }

    public static void messagePlayerIfExists(CommandSourceStack c, String message) {

        //#if MC >= 11900
        if(c.getPlayer() != null) {
             Messenger.m(c.getPlayer(), message);
        }
        //#else
//$$         if(c.getEntity() instanceof ServerPlayer) {
//$$             Messenger.m((Player) c.getEntity(), message);
//$$         }
        //#endif
    }
}
