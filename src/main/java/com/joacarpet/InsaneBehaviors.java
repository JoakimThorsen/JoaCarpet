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

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public class InsaneBehaviors {
    private static int counter = 0;
    private static int resolution = 2;

    public static ArrayList<Float> nextEvenlyDistributedPoint(int dimensions) {
        while (true) {
            int remainder = counter;
            ArrayList<Float> list = new ArrayList<>();

            for (int i = 0; i < dimensions; i++) {
                list.add((float) (remainder % resolution) / (resolution-1));
                remainder = remainder / resolution;
            }

            // Remove previously visited points
            var visited = false;
            float previousStepSize = 1 / ((float) (resolution - 1) / 2);
            if (JoaCarpetSettings.insaneBehaviorsSkipVisitedPoints.equals("true")
                    && resolution != 2
                    && list.stream().allMatch(i -> i % previousStepSize == 0)) {
                visited = true;
            }

            if (!JoaCarpetSettings.insaneBehaviorsIncrement.equals("freeze")) {
                counter++;
                if (counter >= Math.pow(resolution, dimensions)) {
                    if (JoaCarpetSettings.insaneBehaviorsIncrement.equals("normal"))
                        resolution = (resolution - 1) * 2 + 1;
                    counter = 0;
                }
            }

            if (visited) continue;
            return list;
        }
    }

    public static Vec3 mapUnitVelocityToVec3(ArrayList<Float> velocity, int factor, double xCenter, double xVariance, double yCenter, double yVariance, double zCenter, double zVariance) {
        return new Vec3(
                xCenter + (velocity.get(0) -0.5) * 2 * xVariance * factor,
                yCenter + (velocity.get(2) -0.5) * 2 * yVariance * factor,
                zCenter + (velocity.get(1) -0.5) * 2 * zVariance * factor
        );
    }

    public static int resetCounterAndResolution(CommandSourceStack c) {
        counter = 0;
        resolution = 2;
        JoaCarpetMod.messagePlayerIfExists(c, "w Reset.");
        return 1;
    }
    public static int getState(CommandSourceStack c) {
        JoaCarpetMod.messagePlayerIfExists(c, "w Current state: Resolution=" + resolution + ", Counter=" + counter);
        return 1;
    }
    public static int setState(CommandSourceStack c, int _resolution, int _counter) {
        resolution = _resolution;
        counter = _counter;
        JoaCarpetMod.messagePlayerIfExists(c, "w Updated resolution and counter: Resolution=" + resolution + ", Counter=" + counter);
        return 1;
    }
}
