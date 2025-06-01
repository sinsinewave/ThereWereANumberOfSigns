package cyou.sinewave.signsmod.block

import cyou.sinewave.signsmod.SignsMod
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object Blocks {
    val REGISTRY: DeferredRegister.Blocks = DeferredRegister.createBlocks(SignsMod.ID)
    val TALL_DECALS = arrayListOf<DeferredBlock<DecalBlock>>()

    init {
        for (color in DyeColor.entries) {
            TALL_DECALS.add(REGISTRY.register("${color.serializedName}_tall_decal") { ->
                TallDecalBlock(BlockBehaviour.Properties.of().strength(0.1f), color.textureDiffuseColor)
            })
        }
    }
}
