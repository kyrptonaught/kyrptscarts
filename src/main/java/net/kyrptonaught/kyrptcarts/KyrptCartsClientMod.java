package net.kyrptonaught.kyrptcarts;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.impl.client.renderer.registry.EntityModelLayerImpl;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MinecartEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.MinecartEntityModel;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;

public class KyrptCartsClientMod implements ClientModInitializer {
    public static final EntityModelLayer KYRPTCARTENTITYLAYER = new EntityModelLayer(KyrptCartsMod.cart_ID, "main");

    @Override
    public void onInitializeClient() {
        EntityModelLayerImpl.PROVIDERS.put(KYRPTCARTENTITYLAYER, MinecartEntityModel::getTexturedModelData);
        //EntityRendererRegistry.INSTANCE.register(KyrptCartsMod.cart_entity_type,KyrptCartEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(KyrptCartsMod.cart_entity_type, (EntityRendererFactory<AbstractMinecartEntity>) ctx -> new MinecartEntityRenderer<>(ctx, EntityModelLayers.MINECART));
    }
}
