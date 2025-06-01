package cyou.sinewave.signsmod.block.property

import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.block.state.properties.EnumProperty

/**
 * Blockstate property representing a character on a decal
 */
enum class DecalCharacter(val serialName: String): StringRepresentable {
    ZERO("0"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9");

    override fun getSerializedName(): String {
        return this.serialName
    }

    companion object {
        val property: EnumProperty<DecalCharacter> = EnumProperty.create("decal_character", DecalCharacter::class.java)
    }
}