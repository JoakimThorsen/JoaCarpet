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

import com.joacarpet.JoaCarpetMod;
import com.joacarpet.JoaCarpetSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
class ItemMixin {
    @Inject(method = "useOn", at = @At("RETURN"), cancellable = true)
    public void useOn(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir) {
        if (JoaCarpetSettings.blockTickling.equals("off") || !useOnContext.getItemInHand().is(Items.FEATHER)) {
            return;
        }
        Level level = useOnContext.getLevel();
        if (level.isClientSide) {
            return;
        }
        BlockPos clickedPos = useOnContext.getClickedPos();
        BlockPos neighborPos = clickedPos.relative(useOnContext.getClickedFace());
        BlockState neighborBlockState = level.getBlockState(neighborPos);

        JoaCarpetMod.LOGGER.info("NeighborPos: {}", neighborPos);

        if (JoaCarpetSettings.blockTickling.equals("blockupdates") || JoaCarpetSettings.blockTickling.equals("both")) {
            level.neighborChanged(clickedPos, neighborBlockState.getBlock(), neighborPos);
        }
        if (JoaCarpetSettings.blockTickling.equals("shapeupdates") || JoaCarpetSettings.blockTickling.equals("both")) {
            //#if MC >= 11900
            level.neighborShapeChanged(useOnContext.getClickedFace(), neighborBlockState, clickedPos, neighborPos, 2, 512);
            //#else
//$$             BlockState clickedBlockState = level.getBlockState(clickedPos);
//$$             BlockState newState = clickedBlockState.updateShape(useOnContext.getClickedFace(), neighborBlockState, level, clickedPos, neighborPos);
//$$             Block.updateOrDestroy(clickedBlockState, newState, level, clickedPos, 2, 512);
            //#endif
        }
        cir.setReturnValue(InteractionResult.SUCCESS);
    }

}
