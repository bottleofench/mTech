package api.bottleofench.main;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HastebinAPI {

    protected static String post(String text) {
        try {
            byte[] postData = text.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            URL url = new URL("https://www.toptal.com/developers/hastebin/documents");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "Hastebin Java Api");
            conn.setRequestProperty("Content-Length", "" + postDataLength);
            conn.setUseCaches(false);

            String response = null;
            try {
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.write(postData);
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                response = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String postURL = null;
            if (response.contains("\"key\"")) {
                response = response.substring(response.indexOf(":") + 2, response.length() - 2);
                postURL = "https://www.toptal.com/developers/hastebin/" + response;
            }
            return postURL;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

}
