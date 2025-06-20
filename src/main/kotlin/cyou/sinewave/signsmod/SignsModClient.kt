package cyou.sinewave.signsmod

import cyou.sinewave.signsmod.block.PosterBlockEntityRenderer
import cyou.sinewave.signsmod.block.SignsModBlocks
import cyou.sinewave.signsmod.gui.DesignTableScreen
import cyou.sinewave.signsmod.gui.SignsModMenuTypes
import cyou.sinewave.signsmod.item.PosterItemRenderer
import cyou.sinewave.signsmod.item.SignsModItems
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent

/**
 * Client bus subscriber.
 * Used for client-only events such as BER registration
 */
@Suppress("Unused")
@EventBusSubscriber(modid = SignsMod.ID, value = [Dist.CLIENT], bus = EventBusSubscriber.Bus.MOD)
object SignsModClient {
    @SubscribeEvent
    fun registerEntityRenderers(event: RegisterRenderers) {
        event.registerBlockEntityRenderer( // The block entity type to register the renderer for.
            SignsModBlocks.BlockEntities.POSTER.get(),  // A function of BlockEntityRendererProvider.Context to BlockEntityRenderer.
            ::PosterBlockEntityRenderer
        )
    }

    @SubscribeEvent
    fun registerClientExtensions(event: RegisterClientExtensionsEvent) {
        event.registerItem(
            object : IClientItemExtensions {
                val posterBEWLR = PosterItemRenderer()

                override fun getCustomRenderer(): BlockEntityWithoutLevelRenderer {
                    return posterBEWLR
                }
            },
            *SignsModItems.POSTERS.map { it.value() }.toTypedArray()
        )
    }

    @SubscribeEvent
    fun registerScreens(event: RegisterMenuScreensEvent) {
        event.register(SignsModMenuTypes.DESIGN_TABLE.get(), ::DesignTableScreen)
    }

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