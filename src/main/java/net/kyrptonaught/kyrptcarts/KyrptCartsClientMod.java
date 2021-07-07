package net.kyrptonaught.kyrptcarts;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.impl.client.renderer.registry.EntityModelLayerImpl;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.MinecartEntityModel;

public class KyrptCartsClientMod implements ClientModInitializer {
    public static final EntityModelLayer LINKEDCHESTMODELLAYER =  new EntityModelLayer(KyrptCartsMod.cart_ID,"main");
    @Override
    public void onInitializeClient() {
       // EntityModelLayerImpl.PROVIDERS.put(LINKEDCHESTMODELLAYER, MinecartEntityModel::getTexturedModelData);
       // EntityRendererRegistry.INSTANCE.register(KyrptCartsMod.cart_entity_type,KyrptCartEntityRenderer::new);
    }
}
