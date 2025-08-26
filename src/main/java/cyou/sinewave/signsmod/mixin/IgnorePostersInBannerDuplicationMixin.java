package cyou.sinewave.signsmod.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import cyou.sinewave.signsmod.item.PosterBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.BannerDuplicateRecipe;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BannerDuplicateRecipe.class)
public class IgnorePostersInBannerDuplicationMixin {
    @Inject(at = @At("TAIL"), method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z", cancellable = true)
    private void ignore(CraftingInput input, Level level, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 0) ItemStack itemstack, @Local(ordinal = 1) ItemStack itemstack2) {
        if (itemstack != null && itemstack2 != null) {
            if (itemstack.getItem() instanceof PosterBlockItem || itemstack2.getItem() instanceof PosterBlockItem) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }
}
