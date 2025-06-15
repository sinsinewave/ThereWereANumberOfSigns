package cyou.sinewave.signsmod

import cyou.sinewave.signsmod.block.PosterBlockEntityRenderer
import cyou.sinewave.signsmod.block.SignsModBlocks
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers

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
}