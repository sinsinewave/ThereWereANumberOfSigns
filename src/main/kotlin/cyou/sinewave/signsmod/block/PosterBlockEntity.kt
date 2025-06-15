package cyou.sinewave.signsmod.block

import net.minecraft.core.BlockPos
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BannerBlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class PosterBlockEntity(pos: BlockPos, blockState: BlockState) : BannerBlockEntity(pos, blockState) {
    // AbstractBannerBlockEntity sets this to banner, override with our own BE
    // Probably not the world's neatest solution, but it does the job
    override fun getType(): BlockEntityType<*> { return SignsModBlocks.BlockEntities.POSTER.get() }

    override fun getItem(): ItemStack {
        val stack = ItemStack(PosterBlock.byColor(this.baseColor))
        stack.applyComponents(this.collectComponents())
        return stack
    }

    override fun fromItem(stack: ItemStack, color: DyeColor) {
        super.fromItem(stack, color)
    }
}