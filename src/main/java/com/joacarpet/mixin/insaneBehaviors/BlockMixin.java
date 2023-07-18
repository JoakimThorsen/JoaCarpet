package com.joacarpet.mixin.insaneBehaviors;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Supplier;

@Mixin(Block.class)
public interface BlockMixin {
    @Invoker("popResource")
    static void popResourceInvoker(Level level, Supplier<ItemEntity> supplier, ItemStack itemStack) {
        throw new AssertionError();
    }
}
