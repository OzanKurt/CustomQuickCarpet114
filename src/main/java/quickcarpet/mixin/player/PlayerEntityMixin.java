package quickcarpet.mixin.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import quickcarpet.interfaces.PlayerEntityMixinInterface;
import quickcarpet.annotation.Feature;
import quickcarpet.settings.Settings;
import quickcarpet.utils.DonatorStatus;

@Feature("portalCreativeDelay")
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerEntityMixinInterface {
    @Shadow public abstract Iterable<ItemStack> getItemsHand();

    public Integer donationAmount = 0;

    @Inject(method = "getMaxNetherPortalTime", at = @At("HEAD"), cancellable = true)
    private void portalCreativeDelay(CallbackInfoReturnable<Integer> cir) {
        if (!Settings.portalCreativeDelay) return;

        int delay = 80;
        for (ItemStack heldItem : this.getItemsHand()) {
            if (heldItem.getItem() == Items.OBSIDIAN) {
                delay = Integer.MAX_VALUE;
            }
        }
        cir.setReturnValue(delay);
        cir.cancel();
    }

//    @Inject(method = "addExperience", at = @At("HEAD"), cancellable = true)
//    public void giveDoubleExperience(CallbackInfoReturnable<Integer> cir, int experience) {
//        if (hasDonatorStatus(DonatorStatus.DONATOR)) {
//            experience *= 1.5;
//        }
//
//        if (hasDonatorStatus(DonatorStatus.SUPER_DONATOR)) {
//            experience *= 2;
//        }
//
//        if (hasDonatorStatus(DonatorStatus.EXTREME_DONATOR)) {
//            experience *= 4;
//        }
//    }

    public void addDonationAmount(int amount) {
        donationAmount += amount;

        if (donationAmount <= 0) {
            donationAmount = 0;
        }
    }

    public Integer getDonationAmount() {
        return donationAmount;
    }

    public boolean hasDonatorStatus(DonatorStatus donatorStatus) {
        return donationAmount >= donatorStatus.getMinRequiredAmount();
    }

    public DonatorStatus getDonatorStatus() {
        DonatorStatus current = null;

        for (DonatorStatus donatorStatus: DonatorStatus.values()) {
            if (hasDonatorStatus(donatorStatus)) {
                current = donatorStatus;
            }
        }

        return current;
    }
}
