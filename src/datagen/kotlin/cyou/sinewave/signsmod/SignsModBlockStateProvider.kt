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

            val stateBuilder = getMultipartBuilder(block.get())
            for (character in DecalCharacter.entries) {
                for (half in DoubleBlockHalf.entries) {
                    val isDouble = block.value() is TallDecalBlock
                    // This is dumb but this is also in datagen, so I don't care
                    // If we're doing a single block decal, just break on the second half
                    if (!isDouble && half == DoubleBlockHalf.entries.last()) { break }

                    val charModel = models().withExistingParent(
                        "${prefix}_${character.serialName}" + if (isDouble) {"_${half.serializedName}"} else { "" },
                        this.mcLoc("signsmod:block/${prefix}" + if (isDouble) {"_${half.serializedName}"} else { "" })
                    )
                        .texture("particle", mcLoc("block/${block.value().color.serializedName}_concrete"))
                        .texture("0", "signsmod:block/${prefix}_${character.serialName}")


                    for (facing in Direction.entries) {
                        if (facing == Direction.DOWN || facing == Direction.UP) {
                            continue
                        }
                        for (surface in Surface.entries) {
                            val state = stateBuilder.part()
                                .modelFile(charModel)
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
                                .addModel()
                                .condition(BlockStateProperties.HORIZONTAL_FACING, facing)
                                .condition(Surface.property, surface)
                                .condition(DecalCharacter.property, character)
                            // Add half if block is tall
                            if (isDouble) {
                                state.condition(BlockStateProperties.DOUBLE_BLOCK_HALF, half)
                            }
                            state.end()
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