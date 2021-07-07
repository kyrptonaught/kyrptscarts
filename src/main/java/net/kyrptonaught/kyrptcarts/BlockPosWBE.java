package net.kyrptonaught.kyrptcarts;


import net.minecraft.util.math.BlockPos;

public class BlockPosWBE extends BlockPos {
    KyrptCartEntity inCart;

    public BlockPosWBE(BlockPos pos, KyrptCartEntity inCart) {
        super(pos.getX(), pos.getY(), pos.getZ());
        this.inCart = inCart;
    }

    public KyrptCartEntity getStoredCart() {
        return inCart;
    }
}
