package cyou.sinewave.signsmod

import cyou.sinewave.signsmod.block.SignsModBlocks
import cyou.sinewave.signsmod.block.TallDecalBlock
import cyou.sinewave.signsmod.block.property.DecalCharacter
import cyou.sinewave.signsmod.block.property.Surface
import net.minecraft.core.Direction
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

class SignsModBlockStateProvider(
    output    : PackOutput,
    fileHelper: ExistingFileHelper
) : BlockStateProvider(output, SignsMod.ID, fileHelper) {

    override fun registerStatesAndModels() {
        // Regular decals
        // Should probably make this less horrible at some point
        for (block in SignsModBlocks.TALL_DECALS + SignsModBlocks.SMALL_DECALS) {
            val prefix = if (block.id.path.contains("tall_decal")) { "tall_decal" }
            else if (block.id.path.contains("small_decal")) { "small_decal" }
            else { /* Not a decal we care about, skip */ continue }

            //val stateBuilder = getMultipartBuilder(block.get())
            for (character in DecalCharacter.entries) {
                for (half in DoubleBlockHalf.entries) {
                    val isDouble = block.value() is TallDecalBlock
                    // This is dumb but this is also in datagen, so I don't care
                    // If we're doing a single block decal, just break on the second half
                    if (!isDouble && half == DoubleBlockHalf.entries.last()) { break }

                    var suffix = if (isDouble) {"_${half.serializedName}"} else {""}

                    if (character.isTaller && (!isDouble || half == DoubleBlockHalf.UPPER)) {
                        suffix += "_accented"
                    }

                    val charModel = models().withExistingParent(
                        "${prefix}_${character.serialName}" + suffix,
                        this.mcLoc("signsmod:block/${prefix}" + suffix)
                    )
                        .texture("particle", mcLoc("block/${block.value().color.serializedName}_concrete"))
                        .texture("0", "signsmod:block/${prefix}_${character.serialName}")

                    for (facing in Direction.entries) {
                        for (surface in Surface.entries) {
                            if (facing == Direction.DOWN || facing == Direction.UP) { continue }

                            var state = getVariantBuilder(block.get()).partialState()
                                .with(DecalCharacter.property, character)
                                .with(BlockStateProperties.HORIZONTAL_FACING, facing)
                                .with(Surface.property, surface)

                            if (isDouble) {
                                state = state.with(BlockStateProperties.DOUBLE_BLOCK_HALF, half)
                            }

                            state.modelForState()
                                .rotationY(
                                    when (facing) {
                                        Direction.NORTH -> 0
                                        Direction.EAST -> 90
                                        Direction.SOUTH -> 180
                                        Direction.WEST -> 270
                                        else -> 0
                                    }
                                )
                                .rotationX(
                                    when (surface) {
                                        Surface.WALL -> 0
                                        Surface.FLOOR -> 90
                                        Surface.CEILING -> 270
                                    }
                                )
                                .modelFile(charModel)
                                .addModel()
                        }
                    }
                }
            }
        }

        // Posters
        for (block in SignsModBlocks.POSTERS) {
            val stateBuilder = getVariantBuilder(block.value())
            stateBuilder.partialState().modelForState().modelFile(
                models()
                    .withExistingParent(block.id.path, mcLoc("block/air"))
                    .texture("particle", mcLoc("block/${block.value().color.serializedName}_concrete"))
            ).addModel()
        }
    }
}