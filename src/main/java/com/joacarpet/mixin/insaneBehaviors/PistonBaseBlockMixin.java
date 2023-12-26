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
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;

import static com.joacarpet.InsaneBehaviors.nextEvenlyDistributedPoint;

@Mixin(PistonBaseBlock.class)
public class PistonBaseBlockMixin {
    @Redirect(method = "moveBlocks", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/piston/PistonBaseBlock;dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;)V"
    ))
    private void dropResources(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos, @Nullable BlockEntity blockEntity) {
        // net.minecraft.world.level.block.Block.dropResources(net.minecraft.world.level.block.state.BlockState, net.minecraft.world.level.LevelAccessor, net.minecraft.core.BlockPos, net.minecraft.world.level.block.entity.BlockEntity)
        if (levelAccessor instanceof ServerLevel) {
            Level level = (ServerLevel) levelAccessor;
            Block.getDrops(blockState, (ServerLevel)levelAccessor, blockPos, blockEntity).forEach(itemStack -> {
                if (JoaCarpetSettings.insaneBehaviors.equals("off")) {
                    Block.popResource(level, blockPos, itemStack);
                } else {
                    customPopResource(level, blockPos, itemStack);
                }
            });
            //#if MC >= 11900
            blockState.spawnAfterBreak((ServerLevel)levelAccessor, blockPos, ItemStack.EMPTY, true);
            //#else
//$$             blockState.spawnAfterBreak((ServerLevel)levelAccessor, blockPos, ItemStack.EMPTY);
            //#endif
        }
    }

    @Unique
    private static void customPopResource(Level level, BlockPos blockPos, ItemStack itemStack) {
        // net.minecraft.world.level.block.Block.popResource(net.minecraft.world.level.Level, net.minecraft.core.BlockPos, net.minecraft.world.item.ItemStack)
        ArrayList<Float> unitValueList = nextEvenlyDistributedPoint(5);
        double velX = unitValueList.get(0) * 0.2 - 0.1;
        double velZ = unitValueList.get(1) * 0.2 - 0.1;
        double d = (double)EntityType.ITEM.getHeight() / 2.0;
        double posX = (double)blockPos.getX() + 0.5 + unitValueList.get(2) * 0.5 - 0.25;
        double posZ = (double)blockPos.getZ() + 0.5 + unitValueList.get(3) * 0.5 - 0.25;
        double posY = (double)blockPos.getY() + 0.5 + unitValueList.get(4) * 0.5 - 0.25 - d;
        BlockMixin.popResourceInvoker(level, () -> new ItemEntity(level, posX, posY, posZ, itemStack, velX, 0.2, velZ), itemStack);
    }
}
