package com.example.wingsmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class CosmeticWingsMod implements ClientModInitializer {
    public static class Config {
        public static boolean showAngelWings = true;
        public static boolean showDemonWings = false;
        public static boolean showHalo = true;
        public static boolean showHorns = true;
        public static boolean enableParticles = true;
    }

    @Override
    public void onInitializeClient() {
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if (entityRenderer instanceof PlayerEntityRenderer playerRenderer) {
                registrationHelper.register(new MasterCosmeticRenderer(playerRenderer));
            }
        });
    }

    public static class MasterCosmeticRenderer extends FeatureRenderer<PlayerEntity, PlayerEntityModel<PlayerEntity>> {
        public MasterCosmeticRenderer(FeatureRendererContext<PlayerEntity, PlayerEntityModel<PlayerEntity>> context) { super(context); }

        @Override
        public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                           PlayerEntity player, float limbAngle, float limbDistance,
                           float tickDelta, float animationProgress, float headYaw, float headPitch) {
            if (player.isInvisible()) return;

            matrices.push();
            this.getContextModel().head.rotate(matrices);
            if (Config.showHalo) renderHalo(matrices, player, tickDelta);
            if (Config.showHorns) renderHorns(matrices);
            matrices.pop();

            matrices.push();
            this.getContextModel().body.rotate(matrices);
            if (Config.showAngelWings || Config.showDemonWings) renderWings(matrices, player, tickDelta);
            matrices.pop();
        }

        private void renderHalo(MatrixStack matrices, PlayerEntity player, float tickDelta) {
            matrices.push();
            matrices.translate(0.0D, -0.45D, 0.0D);
            float rot = (player.age + tickDelta) * 2.0F;
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rot));
            matrices.pop();
        }

        private void renderHorns(MatrixStack matrices) {
            matrices.push();
            matrices.translate(0.0D, -0.2D, -0.1D);
            matrices.pop();
        }

        private void renderWings(MatrixStack matrices, PlayerEntity player, float tickDelta) {
            matrices.push();
            matrices.translate(0.0D, 0.1D, 0.15D);
            float time = (player.age + tickDelta) * 0.1F;
            float flap = MathHelper.sin(time) * 0.2F;

            if (player.isFallFlying()) {
                flap = MathHelper.sin(time * 2.5F) * 0.7F + 0.4F;
                if (Config.enableParticles && player.getRandom().nextInt(3) == 0) {
                    player.getWorld().addParticle(ParticleTypes.CLOUD, player.getX(), player.getY() + 1.0, player.getZ(), 0.0, -0.1, 0.0);
                }
            }

            for (int i = 0; i < 2; i++) {
                matrices.push();
                float side = (i == 0) ? 1.0F : -1.0F;
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(side * (30.0F + (float) Math.toDegrees(flap))));
                matrices.pop();
            }
            matrices.pop();
        }
    }
}
