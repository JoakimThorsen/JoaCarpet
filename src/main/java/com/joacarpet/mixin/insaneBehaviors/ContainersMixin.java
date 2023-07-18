package com.joacarpet.mixin.insaneBehaviors;

import net.minecraft.world.Containers;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Containers.class)
public class ContainersMixin {

//    @Redirect(method = "dropItemStack", at = @At(
//            value = "INVOKE",
//            target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(DDD)V"
//    ))
//    private static void setDeltaMovement(ItemEntity itemEntity, double d, double e, double f) {
//        if (JoaCarpetSettings.insaneBehaviors.equals("off")) {
//            itemEntity.setDeltaMovement(d+1, e, f);
//            return;
//        }
//        // Vec3 unitVelocity = nextEvenlyDistributedPoint();
//        // Vec3 velocity = switch (JoaCarpetSettings.insaneBehaviors) {
//        //     // net.minecraft.core.dispenser.DefaultDispenseItemBehavior.spawnItem, Line 7
//        //     case "sensible" -> mapUnitVelocityToVec3(
//        //             unitVelocity,
//        //             1,
//        //             0.3 * (double) direction.getStepX(), 0.0172275 * (double) i,
//        //             0.2,                                 0.0172275 * (double) i,
//        //             0.3 * (double) direction.getStepZ(), 0.0172275 * (double) i
//        //     );
//        //     // spawnItem from pre-1.19
//        //     case "extreme" -> mapUnitVelocityToVec3(
//        //             unitVelocity,
//        //             8,
//        //             0.3 * (double) direction.getStepX(), 0.0075 * (double) i,
//        //             0.2,                                 0.0075 * (double) i,
//        //             0.3 * (double) direction.getStepZ(), 0.0075 * (double) i
//        //     );
//        //     default -> throw new IllegalStateException("Unexpected value: " + JoaCarpetSettings.insaneBehaviors);
//        // };
//        // itemEntity.setDeltaMovement(velocity);
//    }
}
