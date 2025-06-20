package cyou.sinewave.signsmod.item

import com.mojang.blaze3d.vertex.PoseStack
import cyou.sinewave.signsmod.block.PosterBlockEntity
import cyou.sinewave.signsmod.block.SignsModBlocks
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack

class PosterItemRenderer() : BlockEntityWithoutLevelRenderer(
    Minecraft.getInstance().blockEntityRenderDispatcher,
    Minecraft.getInstance().entityModels
) {
    private val poster = PosterBlockEntity(BlockPos.ZERO, SignsModBlocks.POSTERS.first().value().defaultBlockState())

    override fun renderByItem(
        stack: ItemStack,
        displayContext: ItemDisplayContext,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        poster.fromItem(stack, (stack.item as PosterBlockItem).color)

        poseStack.pushPose()

        poseStack.translate(0.25, 0.375, 0.35)
        poseStack.scale(0.5f, 0.5f, 150.0f)
        Minecraft.getInstance().blockEntityRenderDispatcher.renderItem(poster, poseStack, bufferSource, packedLight, packedOverlay)

        poseStack.popPose()
    }
}