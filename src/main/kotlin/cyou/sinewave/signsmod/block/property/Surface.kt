package cyou.sinewave.signsmod.block.property

import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.block.state.properties.EnumProperty

/**
 * Blockstate property for what plane a block is on
 * That is, floor/ceiling/wall
 */
enum class Surface(val serialName: String): StringRepresentable {
    FLOOR("floor"),
    WALL("wall"),
    CEILING("ceiling");

    override fun getSerializedName(): String { return serialName }

    companion object {
        val property: EnumProperty<Surface> = EnumProperty.create("surface", Surface::class.java)
    }
}