package cyou.sinewave.signsmod

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.data.event.GatherDataEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(SignsMod.ID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
object SignsModData {
    val LOGGER: Logger = LogManager.getLogger("${SignsMod.ID} Data Generation")

    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        event.generator.addProvider(
            event.includeClient(),
            SignsModBlockStateProvider(event.generator.packOutput, event.existingFileHelper)
        )
        event.generator.addProvider(
            event.includeClient(),
            SignsModItemModelProvider(event.generator.packOutput, event.existingFileHelper)
        )
        event.generator.addProvider(
            event.includeServer(),
            SignsModLootTableProvider(event.generator.packOutput, event.lookupProvider)
        )
    }
}