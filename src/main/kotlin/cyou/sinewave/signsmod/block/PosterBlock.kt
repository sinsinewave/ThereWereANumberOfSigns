package cyou.sinewave.signsmod.block

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import cyou.sinewave.signsmod.block.property.Surface
import cyou.sinewave.signsmod.util.VoxelShapeUtils
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.AbstractBannerBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SimpleWaterloggedBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

// Unfortunately a lot of this class replicates TallDecalBlock and DecalBlock
// However we cannot inherit those as we are already inheriting AbstractBannerBlock
// Maybe could be refactored into some kind of composition thing if we end up with more cases like this?
class PosterBlock(color: DyeColor, properties: Properties) : AbstractBannerBlock(
    color,
    properties
        .noCollission()
        .instabreak()
        .noOcclusion()
        .mapColor(MapColor.NONE)
), SimpleWaterloggedBlock {
    companion object {
        val SURFACE           : Property<Surface>         = Surface.property
        val HORIZONTAL_FACING : Property<Direction>       = BlockStateProperties.HORIZONTAL_FACING
        val WATERLOGGED       : Property<Boolean>         = BlockStateProperties.WATERLOGGED
        val HALF              : Property<DoubleBlockHalf> = BlockStateProperties.DOUBLE_BLOCK_HALF
        
        // Not entirely sure what this is used for, but it was in BannerBlock so we shall have it too
        val CODEC: MapCodec<PosterBlock> = RecordCodecBuilder.mapCodec<PosterBlock> { codecBuilder: RecordCodecBuilder.Instance<PosterBlock> ->
            codecBuilder.group<DyeColor, Properties>(
                DyeColor.CODEC.fieldOf("color").forGetter<PosterBlock> { obj: PosterBlock -> obj.color },
                propertiesCodec<PosterBlock>()
            ).apply<PosterBlock>(codecBuilder) { color: DyeColor, properties: Properties -> PosterBlock(color, properties) }
        }

        fun byColor(color: DyeColor): Block { return SignsModBlocks.POSTERS.first { it.value().color == color }.value() }
    }
    override fun codec(): MapCodec<out PosterBlock> { return CODEC }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        super.createBlockStateDefinition(builder)
        builder.add(SURFACE, HORIZONTAL_FACING, WATERLOGGED, HALF)
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        val fluidstate = context.level.getFluidState(context.clickedPos)
        // Direction is based on block face if placed on wall, else player facing

        val state = stateDefinition.any()
            .setValue(
                HORIZONTAL_FACING, when(context.clickedFace.axis) {
                Direction.Axis.Y -> context.horizontalDirection
                else             -> context.clickedFace.opposite
            })
            .setValue(
                SURFACE, when(context.clickedFace) {
                Direction.DOWN -> Surface.CEILING
                Direction.UP   -> Surface.FLOOR
                else           -> Surface.WALL
            })
            .setValue(WATERLOGGED, fluidstate.type == Fluids.WATER)
            .setValue(HALF, DoubleBlockHalf.LOWER)

        return if (context.level.getBlockState(getHalfPos(state, context.clickedPos)).canBeReplaced()) {
            state
        }
        else {
            null
        }
    }

    override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
        level.setBlockAndUpdate(
            getHalfPos(state, pos),
            state
                .setValue(HALF, DoubleBlockHalf.UPPER)
                .setValue(WATERLOGGED, (level.getFluidState(getHalfPos(state, pos)).type == Fluids.WATER))
        )
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
        // Checked for adjacent position as well since this is a double block
        return (level
            .getBlockState(pos.relative(offset))
            .isFaceSturdy(level, pos.relative(offset), offset.opposite)
        ) && (level
            .getBlockState(getHalfPos(state, pos).relative(offset))
            .isFaceSturdy(level, getHalfPos(state, pos).relative(offset), offset.opposite)
        )
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

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        val ends: Pair<Double, Double> = if (state.getValue(HALF) == DoubleBlockHalf.LOWER) { Pair(2.0, 16.0) } else { Pair(0.0, 14.0) }
        return when (state.getValue(SURFACE)) {
            Surface.FLOOR -> VoxelShapeUtils.horizontalRotatedBox(
                1.0, 0.0, ends.first,
                15.0, 1.0, ends.second,
                state.getValue(HORIZONTAL_FACING).opposite
            )
            Surface.WALL -> VoxelShapeUtils.horizontalRotatedBox(
                1.0, ends.first, 0.0,
                15.0, ends.second, 1.0,
                state.getValue(HORIZONTAL_FACING)
            )
            Surface.CEILING -> VoxelShapeUtils.horizontalRotatedBox(
                1.0, 15.0, ends.first,
                15.0, 16.0, ends.second,
                state.getValue(HORIZONTAL_FACING)
            )
        }
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
        return if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            PosterBlockEntity(pos, state)
        }
        else { null }
    }
}