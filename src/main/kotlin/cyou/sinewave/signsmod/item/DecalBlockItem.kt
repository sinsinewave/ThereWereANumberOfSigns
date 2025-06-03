package cyou.sinewave.signsmod.item

import cyou.sinewave.signsmod.ITinted
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.block.Block

class DecalBlockItem(
    block     : Block,
    properties: Properties,
    val color : Int
) : BlockItem(block, properties), ITinted {
    override fun getColorRGB(idx: Int): Int {
        return if (idx == 1) { color }
        else { 0xffffff }
    }
}