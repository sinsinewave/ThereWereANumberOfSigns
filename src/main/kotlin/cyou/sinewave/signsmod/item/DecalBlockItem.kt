package cyou.sinewave.signsmod.item

import cyou.sinewave.signsmod.ITinted
import cyou.sinewave.signsmod.block.DecalBlock
import cyou.sinewave.signsmod.block.TallDecalBlock
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.context.UseOnContext
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

    // Cycle characters of matching decal blocks even when holding this thing
    // Normally requires empty hand
    override fun useOn(context: UseOnContext): InteractionResult {
        val clickedState = context.level.getBlockState(context.clickedPos)
        if (clickedState.`is`(block)) {
            (clickedState.block as DecalBlock).cycleCharacter(
                clickedState,
                context.clickedPos,
                context.level,
                context.player?.isShiftKeyDown ?: false
            )

            // Also cycle other block on tall decals
            // Arguably this should be handled on the block itself but oh well
            if (clickedState.block is TallDecalBlock) {
                val clickedBlock = clickedState.block as TallDecalBlock
                val halfState = context.level.getBlockState(clickedBlock.getHalfPos(clickedState, context.clickedPos))
                (halfState.block as TallDecalBlock).cycleCharacter(
                    halfState,
                    clickedBlock.getHalfPos(clickedState, context.clickedPos),
                    context.level,
                    context.player?.isShiftKeyDown ?: false
                )
            }
            return InteractionResult.SUCCESS
        }
        return super.useOn(context)
    }
}