package cyou.sinewave.signsmod.gui

import cyou.sinewave.signsmod.SignsMod
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object SignsModMenuTypes {
    val REGISTRY: DeferredRegister<MenuType<*>?> = DeferredRegister.create(BuiltInRegistries.MENU, SignsMod.ID)

    val DESIGN_TABLE: Supplier<MenuType<DesignTableMenu>> = REGISTRY.register("design_table_menu") { -> MenuType(::DesignTableMenu, FeatureFlags.DEFAULT_FLAGS) }
}