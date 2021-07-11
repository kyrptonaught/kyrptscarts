package net.kyrptonaught.kyrptcarts;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MinecartEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class KyrptCartEntityRenderer extends MinecartEntityRenderer<KyrptCartEntity> {
    public KyrptCartEntityRenderer(EntityRendererFactory.Context context) {
        super(context, KyrptCartsClientMod.KYRPTCARTENTITYLAYER);
    }

    @Override
    protected void renderBlock(KyrptCartEntity kyrptCartEntity, float f, BlockState blockState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        if (kyrptCartEntity.getBlockEntity() != null)
            MinecraftClient.getInstance().getBlockEntityRenderDispatcher().render(kyrptCartEntity.getBlockEntity(), f, matrixStack, vertexConsumerProvider);
        MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(blockState, matrixStack, vertexConsumerProvider, i, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
    }
}