package net.kyrptonaught.kyrptcarts.mixin;

import net.kyrptonaught.kyrptcarts.BlockPosWBE;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class WorldGetBEMixin {

    @Unique
    @Inject(method = "Lnet/minecraft/world/World;getBlockEntity(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/entity/BlockEntity;", at = @At("HEAD"), cancellable = true)
    public void spoofWCartBE(BlockPos pos, CallbackInfoReturnable<BlockEntity> cir) {
        if (pos instanceof BlockPosWBE)
            cir.setReturnValue(((BlockPosWBE) pos).getStoredCart().blockEntity);
    }

    @Unique
    @Inject(method = "getBlockState", at = @At("HEAD"), cancellable = true)
    public void spoofWCartState(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        if (pos instanceof BlockPosWBE)
            cir.setReturnValue(((BlockPosWBE) pos).getStoredCart().getContainedBlock());
    }

    @Unique
    @Inject(method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z", at = @At("HEAD"), cancellable = true)
    public void copyStateUpdateToCart(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
        if (pos instanceof BlockPosWBE) {
            ((BlockPosWBE) pos).getStoredCart().setCustomBlock(state);
            cir.cancel();
        }
    }
}
