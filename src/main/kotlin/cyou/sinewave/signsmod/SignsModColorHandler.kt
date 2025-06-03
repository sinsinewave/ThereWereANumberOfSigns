package cyou.sinewave.signsmod

import cyou.sinewave.signsmod.block.SignsModBlocks
import cyou.sinewave.signsmod.item.SignsModItems
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent

@Suppress("unused")
@EventBusSubscriber(modid = SignsMod.ID, value = [Dist.CLIENT], bus = EventBusSubscriber.Bus.MOD)
object SignsModColorHandler {
    @SubscribeEvent
    fun handleItemTints(event: RegisterColorHandlersEvent.Item) {
        for (item in SignsModItems.REGISTRY.entries) {
            if (item.value() is ITinted) {
                event.register(
                    { stack, tintIndex -> (item.value() as ITinted).getColorRGB(tintIndex) },
                    item.value()
                )
            }
        }
    }

    @SubscribeEvent
    fun handleBlockTints(event: RegisterColorHandlersEvent.Block) {
        for (block in SignsModBlocks.REGISTRY.entries) {
            if (block.value() is ITinted) {
                event.register(
                    { state, level, pos, tintIndex -> (block.value() as ITinted).getColorRGB(tintIndex) },
                    block.value()
                )
            }
        }
    }
}