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

import net.minecraft.world.item.context.UseOnContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public enum BlockTicklingSetting {

    BLOCKUPDATES        ("blockUpdates", "Block Updates", false),
    SHAPEUPDATES        ("shapeUpdates", "Shape Updates", false),
    OVERRIDERIGHTCLICK  ("overrideRightclick", "Override Rightclick", false);

    private static final Map<UUID, Map<BlockTicklingSetting, Boolean>> playerSettingMap = new HashMap<>();
    private static final Map<String, BlockTicklingSetting> fromCommandKey = new HashMap<>();

    private final String commandKey;
    private final String name;
    private final Boolean defaultSetting;
    BlockTicklingSetting(String commandKey, String name, Boolean defaultSetting) {
        this.commandKey = commandKey;
        this.name = name;
        this.defaultSetting = defaultSetting;
    }

    static
    {
        for(BlockTicklingSetting i : values())
        {
            fromCommandKey.put(i.commandKey, i);
        }
    }
    public static BlockTicklingSetting fromCommandKey(String key) {
        return fromCommandKey.get(key);
    }

    public static Iterable<String> commandKeys() {
        return fromCommandKey.keySet();
    }

    public static Boolean get(BlockTicklingSetting setting, UUID uuid) {
        return playerSettingMap
                .getOrDefault(uuid, new HashMap<>())
                .getOrDefault(setting, setting.defaultSetting);
    }

    public static Boolean get(BlockTicklingSetting setting, UseOnContext useOnContext) {
        var player = useOnContext.getPlayer();
        if (player == null)
            return setting.defaultSetting;
        return get(setting, player.getUUID());
    }

    public static void set(BlockTicklingSetting setting, UUID uuid, Boolean bool) {
        playerSettingMap.computeIfAbsent(uuid, k -> new HashMap<>()).put(setting, bool);
    }

    public String getName()
    {
        return name;
    }
}