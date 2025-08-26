package cyou.sinewave.signsmod

import cyou.sinewave.signsmod.block.SignsModBlocks
import cyou.sinewave.signsmod.gui.SignsModMenuTypes
import cyou.sinewave.signsmod.item.SignsModItems
import cyou.sinewave.signsmod.recipe.SignsModRecipes
import net.minecraft.client.Minecraft
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.runForDist

/**
 * Main mod class.
 *
 * Primarily interface code to init other areas
 */
@Mod(SignsMod.ID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
object SignsMod {
    const val ID = "signsmod"

    val LOGGER: Logger = LogManager.getLogger(ID)

    init {
        LOGGER.log(Level.INFO, "There were no signs, now there are")

        // Register the KDeferredRegister to the mod-specific event bus
        SignsModBlocks.REGISTRY.register(MOD_BUS)
        SignsModBlocks.BE_TYPES.register(MOD_BUS)
        SignsModItems.REGISTRY.register(MOD_BUS)
        SignsModMenuTypes.REGISTRY.register(MOD_BUS)
        SignsModItems.CREATIVE_TABS.register(MOD_BUS)
        SignsModRecipes.RECIPE_SERIALIZERS.register(MOD_BUS)

        runForDist(
            clientTarget = {
                MOD_BUS.addListener(::onClientSetup)
                Minecraft.getInstance()
            },
            serverTarget = {
                MOD_BUS.addListener(::onServerSetup)
            }
        )
    }

    @Suppress("unused")
    private fun onClientSetup(event: FMLClientSetupEvent) {
        LOGGER.log(Level.INFO, "Initializing client...")
    }

    @Suppress("unused")
    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
        LOGGER.log(Level.INFO, "Server starting...")
    }

    @Suppress("unused")
    @SubscribeEvent
    fun onCommonSetup(event: FMLCommonSetupEvent) {
        LOGGER.log(Level.INFO, "Hello! This is working!")
    }
}
