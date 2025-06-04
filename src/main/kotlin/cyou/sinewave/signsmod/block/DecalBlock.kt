package cyou.sinewave.signsmod.block

import cyou.sinewave.signsmod.ITinted
import cyou.sinewave.signsmod.block.property.DecalCharacter
import cyou.sinewave.signsmod.block.property.Surface
import cyou.sinewave.signsmod.util.VoxelShapeUtils
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SimpleWaterloggedBlock
import net.minecraft.world.level.block.TransparentBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

/**
 * Decal block, essentially a print placed on a surface
 * Generally implemented in practice as a very flat block
 */
open class DecalBlock(properties: Properties, val color: Int) : TransparentBlock (
    properties
        .noCollission()
        .instabreak()
        .noOcclusion()
        .mapColor(MapColor.NONE)
), ITinted, SimpleWaterloggedBlock {
    companion object {
        val DECAL_CHARACTER   : Property<DecalCharacter> = DecalCharacter.property
        val SURFACE           : Property<Surface>        = Surface.property
        val HORIZONTAL_FACING : Property<Direction>      = BlockStateProperties.HORIZONTAL_FACING
        val WATERLOGGED       : Property<Boolean>        = BlockStateProperties.WATERLOGGED
    }

    init {
        // Default to 0, player will cycle to desired character once placed
        registerDefaultState(
            stateDefinition.any()
                .setValue(HORIZONTAL_FACING, Direction.NORTH)
                .setValue(SURFACE, Surface.WALL)
                .setValue(DECAL_CHARACTER, DecalCharacter.ZERO)
                .setValue(WATERLOGGED, false)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        builder.add(HORIZONTAL_FACING, SURFACE, DECAL_CHARACTER, WATERLOGGED)
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        val fluidstate = context.level.getFluidState(context.clickedPos)
        // Direction is based on block face if placed on wall, else player facing
        return stateDefinition.any()
            .setValue(HORIZONTAL_FACING, when(context.clickedFace.axis) {
                Direction.Axis.Y -> context.horizontalDirection
                else             -> context.clickedFace.opposite
            })
            .setValue(SURFACE, when(context.clickedFace) {
                Direction.DOWN -> Surface.CEILING
                Direction.UP   -> Surface.FLOOR
                else           -> Surface.WALL
            })
            .setValue(WATERLOGGED, fluidstate.type == Fluids.WATER)
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return when (state.getValue(SURFACE)) {
            Surface.FLOOR -> VoxelShapeUtils.horizontalRotatedBox(
                3.0, 0.0, 1.0,
                13.0, 1.0, 15.0,
                state.getValue(HORIZONTAL_FACING).opposite
            )
            Surface.WALL -> VoxelShapeUtils.horizontalRotatedBox(
                3.0, 1.0, 0.0,
                13.0, 15.0, 1.0,
                state.getValue(HORIZONTAL_FACING)
            )
            Surface.CEILING -> VoxelShapeUtils.horizontalRotatedBox(
                3.0, 15.0, 1.0,
                13.0, 16.0, 15.0,
                state.getValue(HORIZONTAL_FACING)
            )
        }
    }

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
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
        if (!canSurvive(state, level, pos)) {
            level.removeBlock(pos, false)
        }
    }

    override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
        // Find block this block is mounted on
        val offset = if (state.getValue(SURFACE) == Surface.CEILING) {
            Direction.UP
        }
        else if (state.getValue(SURFACE) == Surface.FLOOR) {
            Direction.DOWN
        }
        else {
            state.getValue(HORIZONTAL_FACING)
        }

        // Can survive if mounting face is "sturdy", that is in practice, a full face
        return level
            .getBlockState(pos.relative(offset))
            .isFaceSturdy(level, pos.relative(offset), offset.opposite)
    }

    /**
     * Cycle the shown decal character forwards
     * TODO: Potentially add a direction boolean
     * @param   state   This block's state
     * @param   pos     This block's position
     * @param   level   The level this block is in
     */
    fun cycleCharacter(state: BlockState, pos: BlockPos, level: Level) {
        // Set character to next one on use, or first one if currently on last
        level.setBlockAndUpdate(pos, state.setValue(
            DecalCharacter.property,
            if (state.getValue(DecalCharacter.property) < DecalCharacter.entries.last()) {
                DecalCharacter.entries[state.getValue(DecalCharacter.property).ordinal + 1 ]
            }
            else {
                DecalCharacter.entries.first()
            }
        ))
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        level: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        // Update water
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level))
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos)
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.getValue(WATERLOGGED)) { Fluids.WATER.getSource(false) } else { super.getFluidState(state) }
    }

    override fun getColorRGB(idx: Int): Int {
        return if (idx == 1) { this.color }
        else { 0xffffff }
    }
}