package quickcarpet.utils;

public enum DonatorStatus {
    NON_DONATOR("Non-Donator", 0),
    DONATOR("Donator", 20),
    SUPER_DONATOR("Super", 50),
    EXTREME_DONATOR("Extreme", 200);

    private String name;
    private int minRequiredAmount;

    DonatorStatus(String name, int minRequiredAmount) {
        this.name = name;
        this.minRequiredAmount = minRequiredAmount;
    }

    public String getName() {
        return name;
    }

    public int getMinRequiredAmount() {
        return minRequiredAmount;
    }
}