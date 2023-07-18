package com.joacarpet;

import carpet.api.settings.Rule;

import static carpet.api.settings.RuleCategory.COMMAND;
import static carpet.api.settings.RuleCategory.CREATIVE;

public class JoaCarpetSettings {
    public static final String JOA = "JoaCarpet";

    @Rule(categories = {CREATIVE, JOA}, options = {"extreme", "sensible", "off"})
    public static String insaneBehaviors = "off";

    @Rule(categories = {COMMAND, CREATIVE, JOA}, options = {"true", "ops", "false", "0", "1", "2", "3", "4"})
    public static String commandInsaneBehaviors = "ops";

    @Rule(categories = {CREATIVE, JOA}, options = {"off", "blockupdates", "shapeupdates", "both"})
    public static String blockTickling = "off";
}
