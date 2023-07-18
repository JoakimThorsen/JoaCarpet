package com.joacarpet.mixin.blockTickling;

import com.joacarpet.JoaCarpetSettings;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method="isSecondaryUseActive", at = @At("HEAD"), cancellable = true)
    public void isSecondaryUseActive(CallbackInfoReturnable<Boolean> cir) {
        if (JoaCarpetSettings.blockTickling.equals("off") && this.getMainHandItem().is(Items.FEATHER)) {
            cir.setReturnValue(true);
        }
    }
}
