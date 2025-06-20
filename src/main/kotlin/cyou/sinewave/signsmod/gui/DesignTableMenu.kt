package cyou.sinewave.signsmod.gui

import cyou.sinewave.signsmod.block.SignsModBlocks
import cyou.sinewave.signsmod.item.DecalBlockItem
import cyou.sinewave.signsmod.item.PosterBlockItem
import cyou.sinewave.signsmod.item.SignsModItems
import net.minecraft.core.Holder
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.inventory.DataSlot
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.DyeItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class DesignTableMenu(
    containerId: Int, playerInventory: Inventory, val access: ContainerLevelAccess
) : AbstractContainerMenu(SignsModMenuTypes.DESIGN_TABLE.get(), containerId) {
    // Client constructor
    constructor(containerId: Int, playerInventory: Inventory): this(containerId, playerInventory, ContainerLevelAccess.NULL)

    private val inContainer = object: SimpleContainer(2) {
        override fun setChanged() {
            super.setChanged()
            if (paperSlot.item.isEmpty) {
                selectedOutputSlot.set(-1)
            }
            setResult()
        }
    }
    private val outContainer = SimpleContainer(1)


    val paperSlot: Slot = addSlot(object: Slot(inContainer, 0, 20, 24) {
        override fun mayPlace(stack: ItemStack): Boolean = stack.`is`(Items.PAPER)
    })
    val dyeSlot: Slot = addSlot(object: Slot(inContainer, 1, 20, 42) {
        override fun mayPlace(stack: ItemStack): Boolean = stack.item is DyeItem
    })
    val outSlot: Slot = addSlot(object: Slot(outContainer, 0, 143, 33) {
        override fun mayPlace(stack: ItemStack): Boolean = false
        override fun onTake(player: Player, stack: ItemStack) {
            paperSlot.remove(1)
            dyeSlot.remove(1)
            super.onTake(player, stack)
        }
    })

    val selectedOutputSlot: DataSlot = DataSlot.standalone()

    private val playerSlotsRange = Pair(this.slots.size, this.slots.size+36)

    init {
        // Add player inventory slots
        for (row in 0..<3) {
            for (col in 0..<9) {
                addSlot(Slot(playerInventory, col+row*9+9, 8+col*18, 84+row*18))
            }
        }
        // Hotbar
        for (slot in 0..<9) {
            addSlot(Slot(playerInventory, slot, 8+slot*18, 142))
        }

        addDataSlot(selectedOutputSlot)
    }

    /**
     *  Returns a list of the things this block can craft
     *  The list is hardcoded as making JSON recipes for.. recipes which all use the same items would be silly
     */
    fun getCraftables(): List<Holder<Item>> {
        if (!this.paperSlot.hasItem()) { return listOf() }

        val color = if (this.dyeSlot.hasItem()) {
            (this.dyeSlot.item.item as DyeItem).dyeColor
        }
        else {
            DyeColor.WHITE
        }

        // Combine item lists and filter to dye
        val lists = SignsModItems.SMALL_DECALS + SignsModItems.TALL_DECALS + SignsModItems.POSTERS
        return lists.filter {
            if (it.get() is DecalBlockItem) {
                (it.get() as DecalBlockItem).color == color.textureDiffuseColor
            }
            else if (it.get() is PosterBlockItem) {
                (it.get() as PosterBlockItem).color == color
            }
            else {
                false
            }
        }
    }

    fun setResult() {
        if (getCraftables().isNotEmpty() && selectedOutputSlot.get() >= 0) {
            outSlot.set(getCraftables()[selectedOutputSlot.get()].value().defaultInstance)
        }
        else {
            outSlot.set(ItemStack.EMPTY)
        }
    }

    override fun clickMenuButton(player: Player, id: Int): Boolean {
        if (id in 0..getCraftables().lastIndex) {
            selectedOutputSlot.set(id)
            setResult()
        }
        return true
    }

    override fun quickMoveStack(
        player: Player,
        idx: Int
    ): ItemStack {
        var movedStack = ItemStack.EMPTY
        if (slots[idx].hasItem()) {
            val oldStack = slots[idx].item
            movedStack = slots[idx].item.copy()

            when(idx) {
                // Out slot to player inventory
                outSlot.index -> {
                    if (!moveItemStackTo(oldStack, playerSlotsRange.first, playerSlotsRange.second, false)) { return ItemStack.EMPTY }
                    slots[idx].onQuickCraft(oldStack, movedStack)
                }

                // Input slots to player inventory
                dyeSlot.index,
                paperSlot.index -> {
                    if (!moveItemStackTo(oldStack, playerSlotsRange.first, playerSlotsRange.second, false)) { return ItemStack.EMPTY }
                }

                // Player inventory
                else -> {
                    // Player inventory to dye slot
                    if (dyeSlot.mayPlace(movedStack)) {
                        if (!moveItemStackTo(oldStack, dyeSlot.index, dyeSlot.index+1, false)) { return ItemStack.EMPTY }
                    }
                    // Player inventory to paper slot
                    else if (paperSlot.mayPlace(oldStack)) {
                        if (!moveItemStackTo(oldStack, paperSlot.index, paperSlot.index+1, false)) { return ItemStack.EMPTY }
                    }
                }
            }

            //  Empty slot if stack is empty
            if (oldStack.isEmpty) { slots[idx].setByPlayer(ItemStack.EMPTY) }
            else { slots[idx].setChanged() }

            if (oldStack.count == movedStack.count) { return ItemStack.EMPTY }
            slots[idx].onTake(player, oldStack)
            broadcastChanges()
        }
        return movedStack
    }

    override fun removed(player: Player) {
        super.removed(player)
        access.execute { level, pos -> clearContainer(player, inContainer) }
    }

    override fun stillValid(player: Player): Boolean {
        return stillValid(access, player, SignsModBlocks.DESIGN_TABLE.get())
    }
}