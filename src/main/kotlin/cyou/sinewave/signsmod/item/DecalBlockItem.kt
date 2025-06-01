package cyou.sinewave.signsmod.item

import net.minecraft.client.color.item.ItemColor
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

class DecalBlockItem(
    block     : Block,
    properties: Properties,
    val color : Int
) : BlockItem(block, properties), ItemColor {
    override fun getColor(stack: ItemStack, tintIndex: Int): Int {
        return if (tintIndex == 1) {
            color
        }
        else {
            0xffffff
        }
    }
}