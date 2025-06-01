package cyou.sinewave.signsmod

import cyou.sinewave.signsmod.block.Blocks
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
        val decalColours = arrayListOf(
            "white", "light_gray", "gray", "black", "brown", "red", "orange", "yellow",
            "lime", "green", "cyan", "light_blue", "blue", "purple", "magenta", "pink"
        )

        val stateBuilder = getMultipartBuilder(Blocks.TALL_DECAL.get())
        for (character in DecalCharacter.entries) {
            for (half in DoubleBlockHalf.entries) {
                val charModel = models().withExistingParent(
                    "tall_decal_${character.serialName}_$half",
                    this.mcLoc("signsmod:block/tall_decal_${half.serializedName}")
                ).texture("0", "signsmod:block/tall_decal_${character.serialName}")

                for (facing in Direction.entries) {
                    if (facing == Direction.DOWN || facing == Direction.UP) {
                        continue
                    }
                    for (surface in Surface.entries) {
                        stateBuilder.part()
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
                            .condition(BlockStateProperties.DOUBLE_BLOCK_HALF, half)
                            .end()
                    }
                }
            }
        }
    }
}