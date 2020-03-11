package quickcarpet.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpClient {

    private static final String API_KEY = "AKCU8lcgA8apFQoQrlX1";

    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    private void close() throws IOException {
        httpClient.close();
    }

    public String sendGet(String url, List<String> parameters) throws IOException {
        String urlWithParameters = getUrlWithParameters(url, parameters);

        System.out.println(urlWithParameters);

        HttpGet request = new HttpGet(urlWithParameters);

        return sendGetRequest(request);
    }

    public String sendGet(String url, String parameters) throws IOException {
        String urlWithParameters = getUrlWithParameters(url, parameters);

        HttpGet request = new HttpGet(urlWithParameters);

        return sendGetRequest(request);
    }

    private String sendGetRequest(HttpGet request) throws IOException {
        request.setHeader("api-key", API_KEY);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                return EntityUtils.toString(entity);
            }
        }

        return null;
    }

    private String getUrlWithParameters(String url, List<String> parameters) {
        StringBuilder parametersBuilder = new StringBuilder();

        parametersBuilder.append(url + '?');

        parameters.forEach(parametersBuilder::append);

        return parametersBuilder.toString();
    }

    private String getUrlWithParameters(String url, String parameters) {
        return url + "?" + parameters;
    }

    public void sendPost(String url, List parameters) throws Exception {
        HttpPost post = new HttpPost(url);

//        List<NameValuePair> parameters = new ArrayList<>();
//        parameters.add(new BasicNameValuePair("username", "abc"));
//        parameters.add(new BasicNameValuePair("password", "123"));
//        parameters.add(new BasicNameValuePair("custom", "secret"));

        post.setEntity(new UrlEncodedFormEntity(parameters));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            System.out.println(EntityUtils.toString(response.getEntity()));
        }

    }

    public void sendPost(String url) throws Exception {
        sendPost(url, new ArrayList());
    }
}