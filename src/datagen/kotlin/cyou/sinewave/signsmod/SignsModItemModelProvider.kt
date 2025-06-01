package cyou.sinewave.signsmod

import cyou.sinewave.signsmod.item.Items
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

class SignsModItemModelProvider(output: PackOutput, existingFileHelper: ExistingFileHelper) :
    ItemModelProvider(output, SignsMod.ID, existingFileHelper) {
    override fun registerModels() {
        basicItem(Items.TALL_DECAL.get())
    }
}