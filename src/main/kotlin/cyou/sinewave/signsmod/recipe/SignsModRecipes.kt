package cyou.sinewave.signsmod.recipe

import cyou.sinewave.signsmod.SignsMod
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer
import net.neoforged.neoforge.registries.DeferredRegister

object SignsModRecipes {
    val RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, SignsMod.ID)

    val POSTER_BANNER_DUPLICATE_SERIALIZER = RECIPE_SERIALIZERS.register("poster_banner_duplicate") { _ -> SimpleCraftingRecipeSerializer(::PosterBannerDuplicateRecipe) }
}