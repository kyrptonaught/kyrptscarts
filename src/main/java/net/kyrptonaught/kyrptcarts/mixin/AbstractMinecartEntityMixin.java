package net.kyrptonaught.kyrptcarts.mixin;

import net.kyrptonaught.kyrptcarts.KyrptCartEntity;
import net.kyrptonaught.kyrptcarts.KyrptCartsMod;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin {

    @Shadow
    public abstract AbstractMinecartEntity.Type getMinecartType();

    @Inject(method = "create", at = @At("HEAD"), cancellable = true)
    private static void addKyrptCartItem(World world, double x, double y, double z, AbstractMinecartEntity.Type type, CallbackInfoReturnable<AbstractMinecartEntity> cir) {
        if (type == KyrptCartsMod.KYRPT_CART_TYPE)
            cir.setReturnValue(new KyrptCartEntity(world, x, y, z));
    }

    @Inject(method = "getPickBlockStack", at = @At("HEAD"), cancellable = true)
    private void addKyrptCartToPick(CallbackInfoReturnable<ItemStack> cir) {
        if (this.getMinecartType() == KyrptCartsMod.KYRPT_CART_TYPE)
            cir.setReturnValue(new ItemStack(KyrptCartsMod.KYRPT_CART_ITEM));
    }
}
