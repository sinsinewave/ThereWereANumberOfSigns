package cyou.sinewave.signsmod.block

import cyou.sinewave.signsmod.SignsMod
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object SignsModBlocks {
    val REGISTRY: DeferredRegister.Blocks = DeferredRegister.createBlocks(SignsMod.ID)
    val TALL_DECALS = arrayListOf<DeferredBlock<DecalBlock>>()
    val SMALL_DECALS = arrayListOf<DeferredBlock<DecalBlock>>()

    init {
        // Generate decal blocks for each dye colour
        for (color in DyeColor.entries) {
            TALL_DECALS.add(REGISTRY.register("${color.serializedName}_tall_decal") { ->
                TallDecalBlock(BlockBehaviour.Properties.of().strength(0.1f), color.textureDiffuseColor)
            })
            SMALL_DECALS.add(REGISTRY.register("${color.serializedName}_small_decal") { ->
                DecalBlock(BlockBehaviour.Properties.of().strength(0.1f), color.textureDiffuseColor)
            })
        }
    }
}
