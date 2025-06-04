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
        // Shaped recipes for tall decals
        for ((idx, item) in SignsModItems.TALL_DECALS.withIndex()) {
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, item.toStack(4))
                .pattern("PPP")
                .pattern("PDP")
                .pattern("PPP")
                .define('P', Items.PAPER)
                .define('D', DyeItem.byColor(DyeColor.entries[idx]))
                .unlockedBy("has_paper", has(Items.PAPER))
                .save(output)
            // Re-dyeing
            ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, item.get())
                .requires(SignsModTags.ItemTags.TALL_DECALS)
                .requires(DyeItem.byColor(DyeColor.entries[idx]))
                .unlockedBy("has_paper", has(Items.PAPER))
                .save(output, "signsmod:${item.id.path}_shapeless")
        }
        // Special no-dye recipe for the white decal
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, SignsModItems.TALL_DECALS[0].toStack(4))
            .pattern("PPP")
            .pattern("P P")
            .pattern("PPP")
            .define('P', Items.PAPER)
            .unlockedBy("has_paper", has(Items.PAPER))
            .save(output,  "signsmod:white_tall_decal_dyeless")

        // Shaped recipes for small decals
        for ((idx, item) in SignsModItems.SMALL_DECALS.withIndex()) {
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, item.toStack(4))
                .pattern(" P ")
                .pattern("PDP")
                .pattern(" P ")
                .define('P', Items.PAPER)
                .define('D', DyeItem.byColor(DyeColor.entries[idx]))
                .unlockedBy("has_paper", has(Items.PAPER))
                .save(output)
            // Re-dyeing
            ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, item.get())
                .requires(SignsModTags.ItemTags.SMALL_DECALS)
                .requires(DyeItem.byColor(DyeColor.entries[idx]))
                .unlockedBy("has_paper", has(Items.PAPER))
                .save(output, "signsmod:${item.id.path}_shapeless")
        }
        // Special no-dye recipe for the white decal
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, SignsModItems.SMALL_DECALS[0].toStack(4))
        .pattern(" P ")
        .pattern("P P")
        .pattern(" P ")
        .define('P', Items.PAPER)
        .unlockedBy("has_paper", has(Items.PAPER))
        .save(output,  "signsmod:white_small_decal_dyeless")
    }
}