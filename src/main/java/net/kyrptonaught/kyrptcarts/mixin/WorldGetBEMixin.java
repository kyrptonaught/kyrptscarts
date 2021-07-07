package net.kyrptonaught.kyrptcarts.mixin;

import net.kyrptonaught.kyrptcarts.BlockPosWBE;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class WorldGetBEMixin {

    @Inject(method = "Lnet/minecraft/world/World;getBlockEntity(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/entity/BlockEntity;", at = @At("HEAD"), cancellable = true)
    public void replaceBE(BlockPos pos, CallbackInfoReturnable<BlockEntity> cir) {
        if (pos instanceof BlockPosWBE)
            cir.setReturnValue(((BlockPosWBE) pos).getStoredCart().blockEntity);
    }

    @Inject(method = "getBlockState", at = @At("HEAD"), cancellable = true)
    public void getState(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        if (pos instanceof BlockPosWBE)
            cir.setReturnValue(((BlockPosWBE) pos).getStoredCart().getContainedBlock());
    }

    @Inject(method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z", at = @At("HEAD"), cancellable = true)
    public void cancelPlace(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
        if (pos instanceof BlockPosWBE) {
            System.out.println("canceled place at: " + pos);
            cir.cancel();
        }
    }
}
