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

package com.joacarpet.mixin.insaneBehaviors;

import com.joacarpet.InsaneBehaviors;
import com.joacarpet.JoaCarpetSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;

import static com.joacarpet.InsaneBehaviors.mapUnitVelocityToVec3;

@Mixin(Projectile.class)
public class ProjectileMixin {
    @WrapOperation(
            //#if MC >= 12100
            method = "getMovementToShoot"
            //#else
//$$             method = "shoot"
            //#endif

            , at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;"
    ))
    private Vec3 add(Vec3 vec3, double d, double e, double f, Operation<Vec3> original, double deltaMovementX, double deltaMovementY, double deltaMovementZ, float deltaMovementMultiplier, float divergence) {
        if (JoaCarpetSettings.insaneBehaviors.equals("off")) {
            return original.call(vec3, d, e, f);
        }

        ArrayList<Float> unitVelocity = InsaneBehaviors.nextEvenlyDistributedPoint(3);
        Vec3 velocity = switch (JoaCarpetSettings.insaneBehaviors) {
            // net.minecraft.world.entity.projectile.Projectile.shoot, Line 2
            case "sensible" -> mapUnitVelocityToVec3(
                    unitVelocity,
                    1,
                    0.0, 0.0172275 * (double)divergence,
                    0.0, 0.0172275 * (double)divergence,
                    0.0, 0.0172275 * (double)divergence
            );
            // net.minecraft.world.entity.projectile.Projectile.shoot pre-1.19
            case "extreme" -> mapUnitVelocityToVec3(
                    unitVelocity,
                    8,
                    0.0, (double)0.0075F * (double)divergence,
                    0.0, (double)0.0075F * (double)divergence,
                    0.0, (double)0.0075F * (double)divergence
            );
            default -> throw new IllegalStateException("Unexpected insaneBehaviors value: " + JoaCarpetSettings.insaneBehaviors);
        };
        return original.call(vec3, vec3.x + velocity.x, vec3.y + velocity.y, vec3.z + velocity.z);
    }

}
