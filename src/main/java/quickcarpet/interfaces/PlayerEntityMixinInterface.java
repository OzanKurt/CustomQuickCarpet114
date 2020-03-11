package quickcarpet.interfaces;

import quickcarpet.utils.DonatorStatus;

public interface PlayerEntityMixinInterface {
    public void addDonationAmount(int amount);

    public Integer getDonationAmount();

    public DonatorStatus getDonatorStatus();
}
