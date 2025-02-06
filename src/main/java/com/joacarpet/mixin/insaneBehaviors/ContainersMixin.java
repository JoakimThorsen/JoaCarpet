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

import com.joacarpet.JoaCarpetSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;

import static com.joacarpet.InsaneBehaviors.mapUnitVelocityToTriangularDistribution;
import static com.joacarpet.InsaneBehaviors.nextEvenlyDistributedPoint;

@Mixin(Containers.class)
public class ContainersMixin {

    @WrapOperation(method = "dropItemStack", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(DDD)V"
    ))
    private static void setDeltaMovement(ItemEntity itemEntity, double d, double e, double f, Operation<Void> original, Level level, double originX, double originY, double originZ, ItemStack itemStack) {
        if (JoaCarpetSettings.insaneBehaviors.equals("off") || JoaCarpetSettings.insaneBehaviorsCartYeetingException.equals("disableContainerContents")) {
            original.call(itemEntity, d, e, f);
            return;
        }
        ArrayList<Float> unitList = nextEvenlyDistributedPoint(6);

        double g = EntityType.ITEM.getWidth();
        double h = 1.0 - g;
        double i = g / 2.0;
        double j = Math.floor(originX) + unitList.get(3) * h + i;
        double k = Math.floor(originY) + unitList.get(4) * h;
        double l = Math.floor(originZ) + unitList.get(5) * h + i;


        itemEntity.setPos(j, k, l);
        Vec3 unitVelocity = new Vec3(unitList.get(0), unitList.get(1), unitList.get(2));

        Vec3 velocity = switch (JoaCarpetSettings.insaneBehaviors) {
            // net.minecraft.world.Containers.dropItemStack, Line 11
            case "sensible" -> mapUnitVelocityToTriangularDistribution(
                    unitVelocity,
                    1,
                    0.0, 0.11485000171139836,
                    0.2, 0.11485000171139836,
                    0.0, 0.11485000171139836
            ); // e.g. [-0.11485000171139836, 0.31485000171139836, 0.0]
            // net.minecraft.world.Containers.dropItemStack from pre-1.19
            case "extreme" -> mapUnitVelocityToTriangularDistribution(
                    unitVelocity,
                    8,
                    0.0, 0.05f,
                    0.2, 0.05f,
                    0.0, 0.05f
            ); // e.g. [-0.4, 0.6, 0.0]
            default -> throw new IllegalStateException("Unexpected insaneBehaviors value: " + JoaCarpetSettings.insaneBehaviors);
        };
        original.call(itemEntity, velocity.x, velocity.y, velocity.z);
    }
}
