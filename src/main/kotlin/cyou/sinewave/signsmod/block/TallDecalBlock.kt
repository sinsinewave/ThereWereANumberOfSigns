package cyou.sinewave.signsmod.block

import cyou.sinewave.signsmod.block.property.Surface
import cyou.sinewave.signsmod.util.VoxelShapeUtils
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class TallDecalBlock(properties: Properties, color: Int) : DecalBlock(properties, color) {
    companion object {
        val HALF: Property<DoubleBlockHalf> = BlockStateProperties.DOUBLE_BLOCK_HALF
    }

    init {
        registerDefaultState(super.defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER))
    }

    /**
     * Retrieve the position of this block's upper half if lower, or vice versa
     * @param   state   This block's state
     * @param   pos     This block's position
     * @return          The position of this block's pair
     */
    fun getHalfPos(state: BlockState, pos: BlockPos): BlockPos {
        return if (
            (state.getValue(SURFACE) == Surface.CEILING && state.getValue(HALF) == DoubleBlockHalf.LOWER)
        ||  (state.getValue(SURFACE) == Surface.FLOOR   && state.getValue(HALF) == DoubleBlockHalf.UPPER)
        ) {
            pos.relative(state.getValue(HORIZONTAL_FACING).opposite)
        }
        else if (
            (state.getValue(SURFACE) == Surface.CEILING && state.getValue(HALF) == DoubleBlockHalf.UPPER)
        ||  (state.getValue(SURFACE) == Surface.FLOOR   && state.getValue(HALF) == DoubleBlockHalf.LOWER)
        ) {
            pos.relative(state.getValue(HORIZONTAL_FACING))
        }
        else if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            pos.above()
        }
        else {
            pos.below()
        }
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        val ends: Pair<Double, Double> = if (state.getValue(HALF) == DoubleBlockHalf.LOWER) { Pair(5.0, 16.0) } else { Pair(0.0, 11.0) }
        return when (state.getValue(SURFACE)) {
            Surface.FLOOR -> VoxelShapeUtils.horizontalRotatedBox(
                2.0, 0.0, ends.first,
                14.0, 1.0, ends.second,
                state.getValue(HORIZONTAL_FACING).opposite
            )
            Surface.WALL -> VoxelShapeUtils.horizontalRotatedBox(
                2.0, ends.first, 0.0,
                14.0, ends.second, 1.0,
                state.getValue(HORIZONTAL_FACING)
            )
            Surface.CEILING -> VoxelShapeUtils.horizontalRotatedBox(
                2.0, 15.0, ends.first,
                14.0, 16.0, ends.second,
                state.getValue(HORIZONTAL_FACING)
            )
        }
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        super.createBlockStateDefinition(builder)
        builder.add(HALF)
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        val state = super.getStateForPlacement(context)?.setValue(HALF, DoubleBlockHalf.LOWER)!!
        return if (context.level.getBlockState(getHalfPos(state, context.clickedPos)).canBeReplaced()) {
            state
        }
        else {
            null
        }
    }

    override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
        level.setBlockAndUpdate(getHalfPos(state, pos), state.setValue(HALF, DoubleBlockHalf.UPPER))
    }

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        val halfBlockState = level.getBlockState(getHalfPos(state, pos))
        (halfBlockState.block as TallDecalBlock).cycleCharacter(halfBlockState, getHalfPos(state, pos), level)
        cycleCharacter(state, pos, level)
        return InteractionResult.SUCCESS
    }

    override fun neighborChanged(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        neighborBlock: Block,
        neighborPos: BlockPos,
        movedByPiston: Boolean
    ) {
        if (!level.getBlockState(getHalfPos(state, pos)).`is`(this)) {
            level.removeBlock(pos, false)
        }
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston)
    }

    override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
        // Check that both halves can survive
        return super.canSurvive(state, level, pos)
            && super.canSurvive(state, level, getHalfPos(state, pos))
    }
}