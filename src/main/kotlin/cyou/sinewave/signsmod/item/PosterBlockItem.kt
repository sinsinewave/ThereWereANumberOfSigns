package cyou.sinewave.signsmod.item

import net.minecraft.world.item.BannerItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class PosterBlockItem(
    block     : Block,
    properties: Properties,
) : BannerItem(block, block, properties) {
    // Copied from BlockItem, this lets us skip StandingAndWallBlockItem while still inheriting BannerItem
    // It's a bit messy, but lets us avoid a whole lot of mixin fuckery
    override fun getPlacementState(context: BlockPlaceContext): BlockState? {
        val state = this.block.getStateForPlacement(context)
        return if (state != null && this.canPlace(context, state)) state else null
    }

    // See above, same thing here
    override fun registerBlocks(blockToItemMap: MutableMap<Block, Item>, item: Item) {
        blockToItemMap.put(this.block, item)
    }
}