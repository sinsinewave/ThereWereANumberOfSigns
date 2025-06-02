package cyou.sinewave.signsmod.util

import net.minecraft.core.Direction
import net.minecraft.world.level.block.Block
import net.minecraft.world.phys.shapes.VoxelShape

object VoxelShapeUtils {
    /**
     * Returns a VoxelShape box rotated on the vertical axis to a specific facing
     * Input is assumed to be facing north
     * @param   x1      Minimum X
     * @param   y1      Minimum Y
     * @param   z1      Minimum Z
     * @param   x2      Maximum X
     * @param   y2      Maximum Y
     * @param   z2      Maximum Z
     * @param   facing  Target facing
     */
    fun horizontalRotatedBox(
        x1: Double, y1: Double, z1: Double,
        x2: Double, y2: Double, z2: Double,
        facing: Direction
    ): VoxelShape {
        return when(facing) {
            Direction.NORTH -> Block.box(x1, y1, z1, x2, y2, z2)
            Direction.SOUTH -> Block.box(16.0-x2, y1, 16.0-z2, 16.0-x1, y2, 16.0-z1)
            Direction.WEST  -> Block.box(z1, y1, x1, z2, y2, x2)
            Direction.EAST  -> Block.box(16.0-z2, y1, 16.0-x2, 16.0-z1, y2, 16.0-x1)
            else            -> throw RuntimeException("Invalid rotation for rotated box: ${facing.serializedName}")
        }
    }
}