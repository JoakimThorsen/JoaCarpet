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

//#if MC >= 11900
import carpet.api.settings.Rule;
import static carpet.api.settings.RuleCategory.*;
//#else
//$$ import carpet.settings.Rule;
//$$ import static carpet.settings.RuleCategory.*;
//#endif

public class JoaCarpetSettings {
    public static final String JOA = "JoaCarpet";

    @Rule(
            //#if MC >= 11900
            categories = {CREATIVE, JOA},
            //#else
//$$             category = {CREATIVE, JOA},
//$$             desc="Makes the random velocities of droppers and projectiles (as well as both the position and velocity of blocks broken by pistons) systematically iterate through the most extreme values possible, and then repeatedly iterate through all the halfway points in between, in a sense attempting every point in a 3d/5d \"grid\" that slowly increases in resolution.\nFor droppers and projectiles, this setting determines whether the max value corresponds to the old gaussian randomness limits (\"extreme\"), or the limits of the triangular randomness introduced in 1.19 (\"sensible\"). Both settings function the same for blocks being broken by pistons.\nFor the `/insanebehaviors <reset/getstate/setstate>` command, see `/carpet commandInsaneBehaviors`.\nDo note that insaneBehaviors works on a global iterator: any triggering event will step through an iteration from all other insaneBehaviors events, too.",
            //#endif
            options = {"extreme", "sensible", "off"}
    )
    public static String insaneBehaviors = "off";

    @Rule(
            //#if MC >= 11900
            categories = {COMMAND, CREATIVE, JOA},
            //#else
//$$             category = {COMMAND, CREATIVE, JOA},
//$$             desc="The command used for the `insaneBehaviors` rule.\n\"reset\" sets the `resolution` and `counter` back to the default values. \"getstate\" and \"setstate\" are used to manually read and write the current iteration state.",
            //#endif
            options = {"true", "ops", "false", "0", "1", "2", "3", "4"}
    )
    public static String commandInsaneBehaviors = "ops";

    @Rule(
            //#if MC >= 11900
            categories = {COMMAND, CREATIVE, JOA},
            //#else
//$$             category = {COMMAND, CREATIVE, JOA},
//$$             desc="Controls who can use the `/blocktickling` command, which lets you send manual block and/or shape updates to blocks using a feather item. Updates are sent from the block in front of the face you're clicking on. Useful if you're working with budded blocks, with /carpet interactionUpdates off, or with intricarpet's /interaction command.",
            //#endif
            options = {"true", "ops", "false", "0", "1", "2", "3", "4"}
    )
    public static String commandBlockTickling = "ops";

    @Rule(
            //#if MC >= 11900
            categories = {SURVIVAL, JOA},
            //#else
//$$             category = {SURVIVAL, JOA},
//$$             desc="Disables enderman griefing.",
            //#endif
            options = {"true", "false"}
    )
    public static String disableEndermanGriefing = "false";

    @Rule(
            //#if MC >= 11900
            categories = {SURVIVAL, JOA},
            //#else
//$$             category = {SURVIVAL, JOA},
//$$             desc="Disables using rockets with elytra.",
            //#endif
            options = {"true", "false"}
    )
    public static String disableElytraRockets = "false";
}
