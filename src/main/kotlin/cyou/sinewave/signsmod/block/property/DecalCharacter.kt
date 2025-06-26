package cyou.sinewave.signsmod.block.property

import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.block.state.properties.EnumProperty

/**
 * Blockstate property representing a character on a decal
 */
enum class DecalCharacter(val serialName: String, val isTaller: Boolean = false): StringRepresentable {
    ZERO("0"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    A("a"),
    B("b"),
    C("c"),
    D("d"),
    E("e"),
    F("f"),
    G("g"),
    H("h"),
    I("i"),
    J("j"),
    K("k"),
    L("l"),
    M("m"),
    N("n"),
    O("o"),
    P("p"),
    Q("q"),
    R("r"),
    S("s"),
    T("t"),
    U("u"),
    V("v"),
    W("w"),
    X("x"),
    Y("y"),
    Z("z"),
    A_UMLAUT("a_umlaut", true),
    O_UMLAUT("o_umlaut", true),
    U_UMLAUT("u_umlaut", true),
    ESZETT("eszett");
    override fun getSerializedName(): String {
        return this.serialName
    }

    companion object {
        val property: EnumProperty<DecalCharacter> = EnumProperty.create("decal_character", DecalCharacter::class.java)
    }
}