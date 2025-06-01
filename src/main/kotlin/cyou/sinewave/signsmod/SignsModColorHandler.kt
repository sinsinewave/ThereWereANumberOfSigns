package cyou.sinewave.signsmod

import cyou.sinewave.signsmod.block.Blocks
import cyou.sinewave.signsmod.item.Items
import net.minecraft.client.color.block.BlockColor
import net.minecraft.client.color.item.ItemColor
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent

@Suppress("unused")
@EventBusSubscriber(modid = SignsMod.ID, value = [Dist.CLIENT], bus = EventBusSubscriber.Bus.MOD)
object SignsModColorHandler {
    @SubscribeEvent
    fun handleItemTints(event: RegisterColorHandlersEvent.Item) {
        for (item in Items.REGISTRY.entries) {
            if (item.value() is ItemColor) {
                event.register(item.value() as ItemColor, item.value())
            }
        }
    }

    @SubscribeEvent
    fun handleBlockTints(event: RegisterColorHandlersEvent.Block) {
        for (block in Blocks.REGISTRY.entries) {
            if (block.value() is BlockColor) {
                event.register(block.value() as BlockColor, block.value())
            }
        }
    }
}