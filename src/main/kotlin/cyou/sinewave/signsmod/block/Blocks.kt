package cyou.sinewave.signsmod.block

import cyou.sinewave.signsmod.SignsMod
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object Blocks {
    val REGISTRY: DeferredRegister.Blocks = DeferredRegister.createBlocks(SignsMod.ID)

    val TALL_DECAL: DeferredBlock<DecalBlock> = REGISTRY.register("tall_decal") { ->
        TallDecalBlock(BlockBehaviour.Properties.of().strength(0.1f))
    }
}
