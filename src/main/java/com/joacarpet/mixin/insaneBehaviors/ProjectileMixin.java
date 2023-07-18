package com.joacarpet.mixin.insaneBehaviors;

import com.joacarpet.InsaneBehaviors;
import com.joacarpet.JoaCarpetSettings;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;

import static com.joacarpet.InsaneBehaviors.mapUnitVelocityToVec3;

@Mixin(Projectile.class)
public class ProjectileMixin {
    @Redirect(method = "shoot", at = @At(
            value = "INVOKE",
            target = "net/minecraft/world/phys/Vec3.add (DDD)Lnet/minecraft/world/phys/Vec3;"
    ))
    private Vec3 add(Vec3 vec3, double d, double e, double f, double deltaMovementX, double deltaMovementY, double deltaMovementZ, float deltaMovementMultiplier, float divergence) {
        if (JoaCarpetSettings.insaneBehaviors.equals("off")) {
            return new Vec3(vec3.x + d, vec3.y + e, vec3.z + f);
        }

        ArrayList<Float> unitVelocity = InsaneBehaviors.nextEvenlyDistributedPoint(3);
        Vec3 velocity = switch (JoaCarpetSettings.insaneBehaviors) {
            // net.minecraft.world.entity.projectile.Projectile.shoot, Line 2
            case "sensible" -> mapUnitVelocityToVec3(
                    unitVelocity,
                    1,
                    0.0, 0.0172275 * (double)divergence,
                    0.0, 0.0172275 * (double)divergence,
                    0.0, 0.0172275 * (double)divergence
            );
            // net.minecraft.world.entity.projectile.Projectile.shoot pre-1.19
            case "extreme" -> mapUnitVelocityToVec3(
                    unitVelocity,
                    8,
                    0.0, (double)0.0075F * (double)divergence,
                    0.0, (double)0.0075F * (double)divergence,
                    0.0, (double)0.0075F * (double)divergence
            );
            default -> throw new IllegalStateException("Unexpected value: " + JoaCarpetSettings.insaneBehaviors);
        };
        return new Vec3(vec3.x + velocity.x, vec3.y + velocity.y, vec3.z + velocity.z);
    }

}
