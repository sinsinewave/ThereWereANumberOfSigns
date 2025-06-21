package cyou.sinewave.signsmod.gui

import cyou.sinewave.signsmod.SignsMod
import cyou.sinewave.signsmod.block.designtable.DesignTableBlock
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class DesignTableScreen(
    menu: DesignTableMenu, playerInventory: Inventory, title: Component
) : AbstractContainerScreen<DesignTableMenu>(menu, playerInventory, DesignTableBlock.title) {
    companion object {
        private val BACKGROUND = ResourceLocation.fromNamespaceAndPath(SignsMod.ID, "textures/gui/design_table.png")
        // We can just use the loom and stonecutter sprites here, no sense duplicating them
        private val DYE_PLACEHOLDER   = ResourceLocation.withDefaultNamespace("container/loom/dye_slot")
        private val PAPER_PLACEHOLDER = ResourceLocation.fromNamespaceAndPath(SignsMod.ID, "paper_slot")
        private val BUTTON            = ResourceLocation.withDefaultNamespace("container/stonecutter/recipe")
        private val BUTTON_SELECTED   = ResourceLocation.withDefaultNamespace("container/stonecutter/recipe_selected")
        private val BUTTON_HOVER      = ResourceLocation.withDefaultNamespace("container/stonecutter/recipe_highlighted")

        private const val SLOTS_X = 5
        private const val SLOTS_START_X = 48
        private const val SLOTS_START_Y = 16
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        renderTooltip(guiGraphics, mouseX, mouseY)
    }

    override fun renderBg(
        guiGraphics: GuiGraphics,
        partialTick: Float,
        mouseX: Int,
        mouseY: Int
    ) {
        guiGraphics.blit(BACKGROUND, leftPos, topPos, 0, 0, imageWidth, imageHeight)

        if (!menu.dyeSlot.hasItem()) {
            guiGraphics.blitSprite(DYE_PLACEHOLDER, leftPos+menu.dyeSlot.x, topPos+menu.dyeSlot.y, 16, 16)
        }
        if (!menu.paperSlot.hasItem()) {
            guiGraphics.blitSprite(PAPER_PLACEHOLDER, leftPos+menu.paperSlot.x, topPos+menu.paperSlot.y, 16, 16)
        }

        for ((idx, item) in menu.getCraftables().withIndex()) {
            val slotX = leftPos+SLOTS_START_X+(idx%SLOTS_X)*16
            val slotY = topPos+SLOTS_START_Y+(idx/SLOTS_X)*18

            guiGraphics.renderItem(
                item.value().defaultInstance, slotX, slotY)

            val buttonSprite = if (menu.selectedOutputSlot.get() == idx) {
                BUTTON_SELECTED
            }
            else if (mouseX in slotX..slotX+16 && mouseY in slotY..slotY+18) {
                BUTTON_HOVER
            }
            else {
                BUTTON
            }
            guiGraphics.blitSprite(buttonSprite, leftPos+SLOTS_START_X+(idx%4)*16, topPos+SLOTS_START_Y+(idx/4)*16, 16, 18)
        }
    }

    override fun renderTooltip(guiGraphics: GuiGraphics, x: Int, y: Int) {
        super.renderTooltip(guiGraphics, x, y)
        for ((idx, item) in menu.getCraftables().withIndex()) {
            val slotX = leftPos+SLOTS_START_X+(idx%SLOTS_X)*16
            val slotY = topPos+SLOTS_START_Y+(idx/SLOTS_X)*18
            if (x in slotX..slotX+16 && y in slotY..slotY+18) {
                guiGraphics.renderTooltip(font, item.value().defaultInstance, x, y)
            }
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        for (idx in menu.getCraftables().indices) {
            val slotX = (leftPos+SLOTS_START_X+(idx%SLOTS_X)*16).toDouble()
            val slotY = (topPos+SLOTS_START_Y+(idx/SLOTS_X)*18).toDouble()

            if (mouseX in slotX..slotX+16 && mouseY in slotY..slotY+18) {
                menu.clickMenuButton(minecraft!!.player!!, idx)
                Minecraft.getInstance().soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_LOOM_SELECT_PATTERN, 1.0f))
                minecraft!!.gameMode!!.handleInventoryButtonClick(menu.containerId, idx)
            }
        }

        return super.mouseClicked(mouseX, mouseY, button)
    }
}