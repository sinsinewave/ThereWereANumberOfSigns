package cyou.sinewave.signsmod

import cyou.sinewave.signsmod.block.DecalBlock
import cyou.sinewave.signsmod.block.PosterBlock
import cyou.sinewave.signsmod.block.SignsModBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import java.util.concurrent.CompletableFuture


class SignsModLootTableProvider(
    output: PackOutput,
    provider: CompletableFuture<HolderLookup.Provider>
) : LootTableProvider(output, setOf(), listOf(), provider) {
    override fun getTables(): List<SubProviderEntry> {
        return listOf(
            SubProviderEntry(this::SignsModBlockLootSubProvider, LootContextParamSets.BLOCK)
        )
    }

    private inner class SignsModBlockLootSubProvider(
        provider: HolderLookup.Provider
    ) : BlockLootSubProvider(setOf(), FeatureFlags.DEFAULT_FLAGS, provider) {
        override fun getKnownBlocks(): Iterable<Block?> {
            return SignsModBlocks.REGISTRY.entries.stream().map { it -> it.value() as Block }.toList()
        }

        override fun generate() {
            // Decal blocks drop themselves
            for (block in SignsModBlocks.REGISTRY.entries) {
                if (block.value() is PosterBlock) {
                    add(
                        block.value(),
                        createBannerDrop(block.value())
                    )
                }
                else {
                    dropSelf(block.value())
                }
            }
        }
    }
}