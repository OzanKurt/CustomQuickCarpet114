package quickcarpet.utils;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DonationManager {

    private static final String BASE_URL = "https://demos-smp.com";

    public Integer getDonationAmountByUsername(String username) {
        HttpClient httpClient = new HttpClient();

        List<String> parameters = new ArrayList<String>();

        parameters.add("username=" + username);

        try {
            String httpResponse = httpClient.sendGet(BASE_URL + "/donations", parameters);

            System.out.println(httpResponse);

            Gson gson = new Gson();
            Response response = gson.fromJson(httpResponse, Response.class);

            return response.getDonationAmount();
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: Display an error message to the user.
        }

        return 0;
    }
}
