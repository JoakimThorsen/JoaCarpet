package com.joacarpet;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.utils.Translations;
import com.joacarpet.commands.InsaneBehaviorsCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class JoaCarpetMod implements ModInitializer, CarpetExtension {
    public static final Logger LOGGER = LoggerFactory.getLogger("joacarpet");

    @Override
    public void onInitialize() {}

    static {
        CarpetServer.manageExtension(new JoaCarpetMod());
    }

    @Override
    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher,
                                 final CommandBuildContext commandBuildContext) {
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
}
