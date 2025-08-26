package cyou.sinewave.signsmod.recipe

import cyou.sinewave.signsmod.item.PosterBlockItem
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.BannerItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.CustomRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

class PosterBannerDuplicateRecipe(category: CraftingBookCategory) : CustomRecipe(category) {
    override fun matches(
        input: CraftingInput,
        level: Level
    ): Boolean {
        // Local variables which need init checks, so sadly can't use lateinit
        var sourceStack: ItemStack? = null
        var targetStack: ItemStack? = null

        for (i in 0..<input.size()) {
            val currentStack = input.getItem(i)

            if (currentStack.item is BannerItem) {
                // If layers are zero, we're looking at the target item
                if (currentStack.get(DataComponents.BANNER_PATTERNS)!!.layers.isEmpty()) {
                    if (targetStack == null) {
                        targetStack = currentStack
                    }
                    // We already found a target item; there can only be one with no layers
                    else {
                        return false
                    }
                }

                // If there are patterns, then this is the source item
                else {
                    if (sourceStack == null) {
                        sourceStack = currentStack
                    }
                    // Again, only one target item is allowed
                    else {
                        return false
                    }
                }
            }
        }

        // Verify that we indeed did find both the target and source, return false if not
        if (sourceStack == null || targetStack == null) {
            return false
        }

        // Check that at least one of the items is a poster; if both are banners that is handled by vanilla banner duplication
        if (sourceStack.item !is PosterBlockItem && targetStack.item !is PosterBlockItem) {
            return false
        }

        // Check that colours match
        if ((sourceStack.item as BannerItem).color != (targetStack.item as BannerItem).color) {
            return false
        }

        // All checks passed
        return true
    }

    override fun assemble(
        input: CraftingInput,
        registries: HolderLookup.Provider
    ): ItemStack {
        lateinit var sourceStack: ItemStack
        lateinit var targetStack: ItemStack

        for (i in 0..<input.size()) {
            // We know the recipe matched, no need to do checking here
            val currentStack = input.getItem(i)
            if (currentStack.get(DataComponents.BANNER_PATTERNS)!!.layers.isEmpty()) {
                targetStack = currentStack
            }
            else {
                sourceStack = currentStack
            }
        }

        val resultStack = targetStack.copyWithCount(1)
        resultStack.set(DataComponents.BANNER_PATTERNS, sourceStack.get(DataComponents.BANNER_PATTERNS))

        return resultStack
    }

    override fun getRemainingItems(input: CraftingInput): NonNullList<ItemStack> {
        val remainder = NonNullList.withSize(input.size(), ItemStack.EMPTY)
        for (i in 0..<input.size()) {
            if (input.getItem(i).hasCraftingRemainingItem()) {
                remainder[i] = input.getItem(i).craftingRemainingItem
            }
            else if (!input.getItem(i).get(DataComponents.BANNER_PATTERNS)!!.layers.isEmpty()) {
                remainder[i] = input.getItem(i).copyWithCount(1)
            }
        }
        return remainder
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean {
        return width*height >= 2
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return SignsModRecipes.POSTER_BANNER_DUPLICATE_SERIALIZER.get()
    }

}