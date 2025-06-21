package cyou.sinewave.signsmod

import cyou.sinewave.signsmod.item.SignsModItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.DyeItem
import net.minecraft.world.item.Items
import java.util.concurrent.CompletableFuture

class SignsModRecipeProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : RecipeProvider(output, registries) {
    override fun buildRecipes(output: RecipeOutput) {
        // Tall decals
        for ((idx, item) in SignsModItems.TALL_DECALS.withIndex()) {
            // Re-dyeing
            ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, item.get())
                .requires(SignsModTags.ItemTags.TALL_DECALS)
                .requires(DyeItem.byColor(DyeColor.entries[idx]))
                .unlockedBy("has_tall_decal", has(SignsModTags.ItemTags.TALL_DECALS))
                .save(output, "signsmod:${item.id.path}_shapeless")
        }

        // Small decals
        for ((idx, item) in SignsModItems.SMALL_DECALS.withIndex()) {
            // Re-dyeing
            ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, item.get())
                .requires(SignsModTags.ItemTags.SMALL_DECALS)
                .requires(DyeItem.byColor(DyeColor.entries[idx]))
                .unlockedBy("has_small_decal", has(SignsModTags.ItemTags.SMALL_DECALS))
                .save(output, "signsmod:${item.id.path}_shapeless")
        }
    }
}