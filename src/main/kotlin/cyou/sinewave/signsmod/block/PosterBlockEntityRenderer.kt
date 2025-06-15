package cyou.sinewave.signsmod.block

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.Sheets
import net.minecraft.client.renderer.blockentity.BannerRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.resources.model.ModelBakery
import net.minecraft.world.level.block.entity.BannerBlockEntity
import net.minecraft.world.phys.AABB
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class PosterBlockEntityRenderer(context: BlockEntityRendererProvider.Context): BannerRenderer(context) {
    private val flag: ModelPart = context.bakeLayer(ModelLayers.BANNER).getChild("flag")

    override fun render(
        blockEntity: BannerBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        poseStack.pushPose()

        // Fix alignment
        poseStack.translate(0.5, 0.1666667, 0.001)
        // Flatten the model
        // This wastes 4 quads because we're just squishing the 3D banner model
        // However the performance impact should be minimal enough to just not matter
        poseStack.scale(0.6666667f,0.6666667f, 0.001f)

        val vertexConsumer = ModelBakery.BANNER_BASE.buffer(bufferSource, RenderType::entitySolid)
        // Render base layer
        flag.render(poseStack, vertexConsumer, packedLight, packedOverlay, blockEntity.baseColor.textureDiffuseColor)

        // Render pattern layers
        for (layer in blockEntity.patterns.layers) {
            val material = Sheets.getBannerMaterial(layer.pattern)
            flag.render(
                poseStack,
                material.buffer(bufferSource, RenderType::entityNoOutline),
                packedLight,
                packedOverlay,
                layer.color.textureDiffuseColor
            )
        }
        poseStack.popPose()
    }

    // Prevent clipping when root block is outside FoV
    override fun getRenderBoundingBox(blockEntity: BannerBlockEntity): AABB {
        val pos = blockEntity.blockPos
        return AABB.encapsulatingFullBlocks(pos, if (blockEntity.blockState.block is PosterBlock) pos.above() else pos.below())
    }
}