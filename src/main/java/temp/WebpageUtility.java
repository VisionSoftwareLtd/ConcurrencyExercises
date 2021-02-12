package temp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebpageUtility {
    public static String getWebpage(String url) {
        try {
            URL urlToOpen = new URL(url);
            HttpURLConnection con = (HttpURLConnection) urlToOpen.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            if (status != 200)
                System.out.println("URL = " + url + ", status = " + status);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            return content.toString();
        } catch (IOException e) {
//            e.printStackTrace();
            return "Couldn't get web page: " + e.getMessage();
        }
    }
}
