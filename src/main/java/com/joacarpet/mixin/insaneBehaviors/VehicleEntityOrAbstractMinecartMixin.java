/*
 * This file is part of the JoaCarpet project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Joa and contributors
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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
//#if MC >= 12003
import net.minecraft.world.entity.vehicle.VehicleEntity;
//#else
//$$ import net.minecraft.world.entity.vehicle.AbstractMinecart;
//#endif
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;

//#if MC >= 12003
@Mixin(VehicleEntity.class)
public class VehicleEntityOrAbstractMinecartMixin {
    @WrapOperation(
            method = "destroy(Lnet/minecraft/world/item/Item;)V",
            at = @At(
                    value = "INVOKE",
                   target = "Lnet/minecraft/world/entity/vehicle/VehicleEntity;spawnAtLocation(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;"
            )
    )
//#else
//$$ @Mixin(AbstractMinecart.class)
//$$ public class VehicleEntityOrAbstractMinecartMixin {
//$$    @WrapOperation(
//$$            method = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;destroy(Lnet/minecraft/world/damagesource/DamageSource;)V",
//$$            at = @At(
//$$                    value = "INVOKE",
//$$                    target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;spawnAtLocation(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;"
//$$            )
//$$    )
//#endif
    private ItemEntity spawnAtLocation(
            //#if MC >= 12003
            VehicleEntity
            //#else
//$$             AbstractMinecart
            //#endif
            instance, ItemStack itemStack, Operation<ItemEntity> original) {
        if (JoaCarpetSettings.insaneBehaviors.equals("off") || JoaCarpetSettings.insaneBehaviorsCartYeetingException.equals("disableVehicleItem")) {
            return original.call(instance, itemStack);
        }
        ArrayList<Float> unitValueList = InsaneBehaviors.nextEvenlyDistributedPoint(2);
        Vec3 velocity = new Vec3(
                unitValueList.get(0) * 0.2 - 0.1,
                0.2,
                unitValueList.get(1) * 0.2 - 0.1
        );
        //#if MC >= 11800
        Level level = instance.level();
        //#else
//$$         Level level = instance.getCommandSenderWorld();
        //#endif
        ItemEntity itemEntity = new ItemEntity(level, instance.getX(), instance.getY(), instance.getZ(), itemStack, velocity.x, velocity.y, velocity.z);
        itemEntity.setDefaultPickUpDelay();
        level.addFreshEntity(itemEntity);
        return itemEntity;
    }

}
