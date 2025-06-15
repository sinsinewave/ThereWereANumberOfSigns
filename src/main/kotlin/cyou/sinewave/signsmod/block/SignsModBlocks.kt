package cyou.sinewave.signsmod.block

import cyou.sinewave.signsmod.SignsMod
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object SignsModBlocks {
    val BE_TYPES: DeferredRegister<BlockEntityType<*>> = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, SignsMod.ID)
    val REGISTRY: DeferredRegister.Blocks = DeferredRegister.createBlocks(SignsMod.ID)

    val TALL_DECALS  = arrayListOf<DeferredBlock<DecalBlock>>()
    val SMALL_DECALS = arrayListOf<DeferredBlock<DecalBlock>>()
    val POSTERS      = arrayListOf<DeferredBlock<PosterBlock>>()

    val BlockEntities = BlockEntitiesHolder()

    init {
        // Generate decal blocks for each dye colour
        for (color in DyeColor.entries) {
            TALL_DECALS.add(REGISTRY.register("${color.serializedName}_tall_decal") { ->
                TallDecalBlock(BlockBehaviour.Properties.of().strength(0.1f), color.textureDiffuseColor)
            })
            SMALL_DECALS.add(REGISTRY.register("${color.serializedName}_small_decal") { ->
                DecalBlock(BlockBehaviour.Properties.of().strength(0.1f), color.textureDiffuseColor)
            })
            POSTERS.add(REGISTRY.register("${color.serializedName}_poster") { ->
                PosterBlock(color, BlockBehaviour.Properties.of().strength(0.1f))
            })
        }
    }

    class BlockEntitiesHolder {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "PropertyName")
        val POSTER: Supplier<BlockEntityType<PosterBlockEntity>> = BE_TYPES.register("poster") { ->
            BlockEntityType.Builder.of(::PosterBlockEntity, *POSTERS.map { it.get() }.toTypedArray()).build(null)
        }
    }
}
