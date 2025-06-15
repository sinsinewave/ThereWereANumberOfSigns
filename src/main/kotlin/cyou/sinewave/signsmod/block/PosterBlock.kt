package cyou.sinewave.signsmod.block

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.AbstractBannerBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.MapColor

class PosterBlock(color: DyeColor, properties: Properties) : AbstractBannerBlock(
    color,
    properties
        .noCollission()
        .instabreak()
        .noOcclusion()
        .mapColor(MapColor.NONE)
) {
    companion object {
        val CODEC: MapCodec<PosterBlock> = RecordCodecBuilder.mapCodec<PosterBlock> { codecBuilder: RecordCodecBuilder.Instance<PosterBlock> ->
            codecBuilder.group<DyeColor?, Properties?>(
                DyeColor.CODEC.fieldOf("color").forGetter<PosterBlock> { obj: PosterBlock -> obj.color },
                propertiesCodec<PosterBlock>()
            ).apply<PosterBlock>(codecBuilder) { color: DyeColor, properties: Properties -> PosterBlock(color, properties) }
        }

        fun byColor(color: DyeColor): Block {
            return SignsModBlocks.POSTERS.first().value()
        }
    }
    override fun codec(): MapCodec<out PosterBlock> { return CODEC }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return PosterBlockEntity(pos, state)
    }
}