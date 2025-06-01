package cyou.sinewave.signsmod.item

import cyou.sinewave.signsmod.SignsMod
import cyou.sinewave.signsmod.block.Blocks
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister


object Items {
    val REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(SignsMod.ID)
    val TALL_DECALS = arrayListOf<DeferredItem<BlockItem>>()

    init {
        // Iterate through tall decal blocks and pick dye colour by index
        // Blocks are inserted into their corresponding array by DyeColor entry order
        for ((idx, block) in Blocks.TALL_DECALS.withIndex()) {
            TALL_DECALS.add(REGISTRY.register(block.id.path) { ->
                DecalBlockItem(
                    block.value(),
                    Item.Properties(),
                    DyeColor.entries[idx].textureDiffuseColor
                )
            })
        }
    }
}