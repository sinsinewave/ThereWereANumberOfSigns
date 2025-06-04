package cyou.sinewave.signsmod

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

object SignsModTags {
    private fun modRL(path: String): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(SignsMod.ID, path)
    }

    object ItemTags {
        val TALL_DECALS: TagKey<Item>  = TagKey<Item>.create(Registries.ITEM, modRL("tall_decals"))
        val SMALL_DECALS: TagKey<Item> = TagKey<Item>.create(Registries.ITEM, modRL("small_decals"))
    }
}