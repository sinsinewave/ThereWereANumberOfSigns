package cyou.sinewave.signsmod

import cyou.sinewave.signsmod.item.SignsModItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class SignsModItemTagProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper?
) : ItemTagsProvider(output, lookupProvider, CompletableFuture.completedFuture(null), SignsMod.ID, existingFileHelper) {
    override fun addTags(provider: HolderLookup.Provider) {
        tag(SignsModTags.ItemTags.TALL_DECALS)
            .add(*SignsModItems.TALL_DECALS.map { it.value() }.toTypedArray())
        tag(SignsModTags.ItemTags.SMALL_DECALS)
            .add(*SignsModItems.SMALL_DECALS.map { it.value() }.toTypedArray())
    }
}