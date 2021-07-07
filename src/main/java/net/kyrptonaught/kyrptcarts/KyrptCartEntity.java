package net.kyrptonaught.kyrptcarts;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.world.World;

public class KyrptCartEntity extends AbstractMinecartEntity {
    protected KyrptCartEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    protected KyrptCartEntity(World world) {
        this(EntityType.MINECART, world);

    }

    @Override
    public Type getMinecartType() {
        return KyrptCartsMod.KYRPT_CART_TYPE;
    }
}
