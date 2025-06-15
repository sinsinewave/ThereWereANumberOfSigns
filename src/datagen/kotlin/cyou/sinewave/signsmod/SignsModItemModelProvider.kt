package cyou.sinewave.signsmod

import cyou.sinewave.signsmod.item.SignsModItems
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

class SignsModItemModelProvider(output: PackOutput, existingFileHelper: ExistingFileHelper) :
    ItemModelProvider(output, SignsMod.ID, existingFileHelper) {
    override fun registerModels() {
        for (item in SignsModItems.TALL_DECALS) {
            withExistingParent(item.id.toString(), mcLoc("item/generated"))
                .texture("layer0", "signsmod:item/tall_decal_background")
                .texture("layer1", "signsmod:item/tall_decal_overlay")
        }
        for (item in SignsModItems.SMALL_DECALS) {
            withExistingParent(item.id.toString(), mcLoc("item/generated"))
                .texture("layer0", "signsmod:item/small_decal_background")
                .texture("layer1", "signsmod:item/small_decal_overlay")
        }
        for (item in SignsModItems.POSTERS) {
            withExistingParent(item.id.toString(), modLoc("item/poster"))
        }
    }
}