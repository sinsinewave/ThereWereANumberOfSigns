package cyou.sinewave.signsmod.item

import cyou.sinewave.signsmod.SignsMod
import cyou.sinewave.signsmod.block.SignsModBlocks
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister


object SignsModItems {
    val REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(SignsMod.ID)
    val CREATIVE_TABS: DeferredRegister<CreativeModeTab> = DeferredRegister.create(
        Registries.CREATIVE_MODE_TAB,
        SignsMod.ID
    )
    val TALL_DECALS = arrayListOf<DeferredItem<BlockItem>>()

    init {
        // Iterate through tall decal blocks and pick dye colour by index
        // Blocks are inserted into their corresponding array by DyeColor entry order
        for ((idx, block) in SignsModBlocks.TALL_DECALS.withIndex()) {
            TALL_DECALS.add(REGISTRY.register(block.id.path) { ->
                DecalBlockItem(
                    block.value(),
                    Item.Properties(),
                    DyeColor.entries[idx].textureDiffuseColor
                )
            })
        }

        // Register creative tab
        CREATIVE_TABS.register("signsmod_group") { ->
            CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.${SignsMod.ID}"))
                .icon { ItemStack(TALL_DECALS.last().asItem()) }
                .displayItems { params, output ->
                    for (item in TALL_DECALS) {
                        output.accept(item.get())
                    }
                }
                .build()
        }
    }
}