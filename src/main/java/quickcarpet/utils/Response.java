package quickcarpet.utils;

public class Response {

    private String message;
    private Integer donationAmount;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getDonationAmount() {
        return donationAmount;
    }

    public void setDonationAmount(Integer donationAmount) {
        this.donationAmount = donationAmount;
    }

    @Override
    public String toString() {
        return "Response{" +
            "message='" + message + '\'' +
            ", donationAmount=" + donationAmount +
            '}';
    }
}
