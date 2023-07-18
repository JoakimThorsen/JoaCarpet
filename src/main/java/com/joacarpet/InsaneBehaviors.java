package com.joacarpet;

import carpet.utils.Messenger;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public class InsaneBehaviors {
    private static int counter = 0;
    private static int resolution = 2;

    public static ArrayList<Float> nextEvenlyDistributedPoint(int dimensions) {
        int remainder = counter;
        ArrayList<Float> list = new ArrayList<>();
        for (int i = 0; i < dimensions; i++) {
            list.add((float) (remainder % resolution) / (resolution-1));
            remainder = remainder / resolution;
        }
        counter++;
        if(counter >= Math.pow(resolution, dimensions)) {
            resolution = (resolution-1) *2 +1;
            counter = 0;
        }
        return list;
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
        if(c.getPlayer() != null) {
            Messenger.m(c.getPlayer(), "w Reset.");
        }
        return 1;
    }
    public static int getState(CommandSourceStack c) {
        if(c.getPlayer() != null) {
            Messenger.m(c.getPlayer(), "w Current state: Resolution=" + resolution + ", Counter=" + counter);
        }
        return 1;
    }
    public static int setState(CommandSourceStack c, int _resolution, int _counter) {
        resolution = _resolution;
        counter = _counter;
        if(c.getPlayer() != null) {
            Messenger.m(c.getPlayer(), "w Updated resolution and counter: Resolution=" + resolution + ", Counter=" + counter);
        }
        return 1;
    }
}
