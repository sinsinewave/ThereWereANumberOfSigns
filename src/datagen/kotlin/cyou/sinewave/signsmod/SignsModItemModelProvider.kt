package cyou.sinewave.signsmod

import cyou.sinewave.signsmod.item.Items
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

class SignsModItemModelProvider(output: PackOutput, existingFileHelper: ExistingFileHelper) :
    ItemModelProvider(output, SignsMod.ID, existingFileHelper) {
    override fun registerModels() {
        for (item in Items.TALL_DECALS) {
            withExistingParent(item.id.toString(), mcLoc("item/generated"))
                .texture("layer0", "signsmod:item/decal_background")
                .texture("layer1", "signsmod:item/tall_decal_overlay")
        }
    }
}