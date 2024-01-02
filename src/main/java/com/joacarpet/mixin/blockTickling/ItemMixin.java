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

package com.joacarpet.mixin.blockTickling;

import com.joacarpet.BlockTicklingSetting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC < 11900
//$$ import net.minecraft.world.level.block.Block;
//#endif

@Mixin(Item.class)
class ItemMixin {

    @Unique
    private static final int mysteryFlag = 2;
    @Unique
    private static final int mysteryNumber = 512;

    @Inject(method = "useOn", at = @At("RETURN"), cancellable = true)
    public void useOn(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir) {
        if (!(BlockTicklingSetting.get(BlockTicklingSetting.BLOCKUPDATES, useOnContext)
        || BlockTicklingSetting.get(BlockTicklingSetting.SHAPEUPDATES, useOnContext))
        || !useOnContext.getItemInHand().is(Items.FEATHER)) {
            return;
        }
        Level level = useOnContext.getLevel();
        if (level.isClientSide) {
            return;
        }
        BlockPos clickedPos = useOnContext.getClickedPos();
        BlockPos neighborPos = clickedPos.relative(useOnContext.getClickedFace());
        BlockState neighborBlockState = level.getBlockState(neighborPos);

        if (BlockTicklingSetting.get(BlockTicklingSetting.BLOCKUPDATES, useOnContext)) {
            level.neighborChanged(clickedPos, neighborBlockState.getBlock(), neighborPos);
        }
        if (BlockTicklingSetting.get(BlockTicklingSetting.SHAPEUPDATES, useOnContext)) {
            //#if MC >= 11900
            level.neighborShapeChanged(useOnContext.getClickedFace(), neighborBlockState, clickedPos, neighborPos, mysteryFlag, mysteryNumber);
            //#else
//$$             BlockState clickedBlockState = level.getBlockState(clickedPos);
//$$             BlockState newState = clickedBlockState.updateShape(useOnContext.getClickedFace(), neighborBlockState, level, clickedPos, neighborPos);
//$$             Block.updateOrDestroy(clickedBlockState, newState, level, clickedPos, mysteryFlag, mysteryNumber);
            //#endif
        }
        cir.setReturnValue(InteractionResult.SUCCESS);
    }

}
