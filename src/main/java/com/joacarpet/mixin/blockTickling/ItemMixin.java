package com.joacarpet.mixin.blockTickling;

import com.joacarpet.JoaCarpetSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
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
        BlockPos blockPos = useOnContext.getClickedPos();
        BlockPos neighborPos = blockPos.relative(useOnContext.getClickedFace());
        BlockState neighborBlockState = level.getBlockState(neighborPos);

        if (JoaCarpetSettings.blockTickling.equals("blockupdates") || JoaCarpetSettings.blockTickling.equals("both")) {
            level.neighborChanged(blockPos, neighborBlockState.getBlock(), neighborPos);
        }
        if (JoaCarpetSettings.blockTickling.equals("shapeupdates") || JoaCarpetSettings.blockTickling.equals("both")) {
            level.neighborShapeChanged(useOnContext.getClickedFace(), neighborBlockState, blockPos, neighborPos, 2, 512);
        }
        cir.setReturnValue(InteractionResult.SUCCESS);
    }

}
