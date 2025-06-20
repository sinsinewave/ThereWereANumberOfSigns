package cyou.sinewave.signsmod.block.designtable

import cyou.sinewave.signsmod.gui.DesignTableMenu
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class DesignTableBlock(properties: Properties) : Block(properties) {
    companion object {
        val title: Component = Component.translatable("container.signsmod.design_table")
    }

    override fun getMenuProvider(state: BlockState, level: Level, pos: BlockPos): MenuProvider? {
        return SimpleMenuProvider(
            { id, inventory, player ->
                DesignTableMenu(id, inventory, ContainerLevelAccess.create(level, pos))
            },
            title
        )
    }

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        return if (level.isClientSide) {
            InteractionResult.SUCCESS
        }
        else {
            player.openMenu(state.getMenuProvider(level, pos))
            return InteractionResult.CONSUME
        }
    }
}