package net.kyrptonaught.kyrptcarts;


import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.MinecartItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class KyrptCartsMod implements ModInitializer {
    public static final String MOD_ID = "kyrptcarts";
    public static AbstractMinecartEntity.Type KYRPT_CART_TYPE;
    public static Identifier cart_ID = new Identifier(MOD_ID, "kyrptcart");
    public static EntityType<KyrptCartEntity> cart_entity_type;
    public static MinecartItem KYRPT_CART_ITEM;

    @Override
    public void onInitialize() {
        KYRPT_CART_TYPE = ClassTinkerers.getEnum(AbstractMinecartEntity.Type.class, "KYRPT_CART");
        cart_entity_type = FabricEntityTypeBuilder.<KyrptCartEntity>create(SpawnGroup.MISC, KyrptCartEntity::new).dimensions(EntityDimensions.fixed(.98f, .7F)).trackRangeBlocks(8).build();
        Registry.register(Registry.ENTITY_TYPE, cart_ID, cart_entity_type);
        KYRPT_CART_ITEM = new MinecartItem(KYRPT_CART_TYPE, (new Item.Settings()).maxCount(1).group(ItemGroup.TRANSPORTATION));
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "kyrptcart"), KYRPT_CART_ITEM);
    }
}