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
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;

import static com.joacarpet.InsaneBehaviors.*;

@Mixin(DefaultDispenseItemBehavior.class)
public class DefaultDispenseItemBehaviorMixin {

	@WrapOperation(method = "spawnItem", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(DDD)V"
	))
	private static void setDeltaMovement(
			ItemEntity itemEntity,
			double d, double e, double f,
			Operation<ItemEntity> original,
			Level _level, ItemStack _itemStack, int i, Direction direction, Position _position
	) {
		if (JoaCarpetSettings.insaneBehaviors.equals("off")) {
			original.call(itemEntity, d, e, f);
			return;
		}
		ArrayList<Float> unitList = nextEvenlyDistributedPoint(3);
		Vec3 unitVelocity = new Vec3(unitList.get(0), unitList.get(2), unitList.get(1));

		double gx = 0.1 * unitVelocity.x + 0.2;
		double gz = 0.1 * unitVelocity.z + 0.2;
		Vec3 velocity = switch (JoaCarpetSettings.insaneBehaviors) {
			// net.minecraft.core.dispenser.DefaultDispenseItemBehavior.spawnItem, Line 7
			case "sensible" -> mapUnitVelocityToTriangularDistribution(
					unitVelocity,
					1,
					gx * (double) direction.getStepX(), 0.0172275 * (double) i,
					0.2,                                0.0172275 * (double) i,
					gz * (double) direction.getStepZ(), 0.0172275 * (double) i
			);
			// spawnItem from pre-1.19
			case "extreme" -> mapUnitVelocityToTriangularDistribution(
					unitVelocity,
					8,
					gx * (double) direction.getStepX(), 0.0075 * (double) i,
					0.2,                                0.0075 * (double) i,
					gz * (double) direction.getStepZ(), 0.0075 * (double) i
			);
			default -> throw new IllegalStateException("Unexpected insaneBehaviors value: " + JoaCarpetSettings.insaneBehaviors);
		};
		original.call(itemEntity, velocity.x, velocity.y, velocity.z);
	}
}
