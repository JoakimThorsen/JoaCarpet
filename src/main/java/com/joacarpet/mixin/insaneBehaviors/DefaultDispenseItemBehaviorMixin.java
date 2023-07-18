package com.joacarpet.mixin.insaneBehaviors;

import com.joacarpet.JoaCarpetSettings;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;

import static com.joacarpet.InsaneBehaviors.*;

@Mixin(DefaultDispenseItemBehavior.class)
public class DefaultDispenseItemBehaviorMixin {

	@Redirect(method = "spawnItem", at = @At(
			value = "INVOKE",
			target = "net/minecraft/world/entity/item/ItemEntity.setDeltaMovement (DDD)V"
	))
	private static void setDeltaMovement(ItemEntity itemEntity, double d, double e, double f, Level _level, ItemStack _itemStack, int i, Direction direction, Position _position) {
		if (JoaCarpetSettings.insaneBehaviors.equals("off")) {
			itemEntity.setDeltaMovement(d, e, f);
			return;
		}
		ArrayList<Float> unitVelocity = nextEvenlyDistributedPoint(3);
		Vec3 velocity = switch (JoaCarpetSettings.insaneBehaviors) {
			// net.minecraft.core.dispenser.DefaultDispenseItemBehavior.spawnItem, Line 7
			case "sensible" -> mapUnitVelocityToVec3(
					unitVelocity,
					1,
					0.3 * (double) direction.getStepX(), 0.0172275 * (double) i,
					0.2,                                 0.0172275 * (double) i,
					0.3 * (double) direction.getStepZ(), 0.0172275 * (double) i
			);
			// spawnItem from pre-1.19
			case "extreme" -> mapUnitVelocityToVec3(
					unitVelocity,
					8,
					0.3 * (double) direction.getStepX(), 0.0075 * (double) i,
					0.2,                                 0.0075 * (double) i,
					0.3 * (double) direction.getStepZ(), 0.0075 * (double) i
			);
			default -> throw new IllegalStateException("Unexpected value: " + JoaCarpetSettings.insaneBehaviors);
		};
		itemEntity.setDeltaMovement(velocity);
	}

//    @Redirect(method = "spawnItem", expect = 3, at = @At(
//            value = "INVOKE",
//            target = "net/minecraft/util/RandomSource.triangle (DD)D"
//    ))
//    private static double triangle(RandomSource randomSource, double d, double e) {
//        return d + e * (randomSource.nextDouble() - randomSource.nextDouble()) + 1;
//    }
}
