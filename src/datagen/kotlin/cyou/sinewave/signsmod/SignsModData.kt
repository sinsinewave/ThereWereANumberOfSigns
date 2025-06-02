package cyou.sinewave.signsmod

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.data.event.GatherDataEvent

@Mod(SignsMod.ID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
object SignsModData {
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
        event.generator.addProvider(
            event.includeServer(),
            SignsModItemTagProvider(event.generator.packOutput, event.lookupProvider, event.existingFileHelper)
        )
        event.generator.addProvider(
            event.includeServer(),
            SignsModRecipeProvider(event.generator.packOutput, event.lookupProvider)
        )
    }
}