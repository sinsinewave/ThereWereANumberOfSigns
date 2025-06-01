package cyou.sinewave.signsmod.item

import cyou.sinewave.signsmod.SignsMod
import cyou.sinewave.signsmod.block.Blocks
import net.minecraft.world.item.BlockItem
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister


object Items {
    val REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(SignsMod.ID)

    val TALL_DECAL: DeferredItem<BlockItem> = REGISTRY.registerSimpleBlockItem(Blocks.TALL_DECAL)
}