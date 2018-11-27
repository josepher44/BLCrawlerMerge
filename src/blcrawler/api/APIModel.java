package blcrawler.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import org.json.simple.JSONObject;

public class APIModel
{
    
    public APIModel(String partnumber)
    {
        String consumerKey = "16AB07C5220C43768822A29EB745FDC1";
        String consumerSecret = "F0FE04252666486CB1D954006CA580A3";
        String tokenValue = "0FD7279A6760427681A6C37E2AC89768";
        String tokenSecret = "986EE49534414CE684D53A59E07FDEEA";
        
        String consumerSecretTwitter = "MCD8BKwGdgPHvAuvgvz4EQpqDAtx89grbuNMRd7Eh98";
        String tokenSecretTwitter = "J6zix3FfA9LofH0awS24M3HcBYXO5nI1iYe8EfBA";
        
        boolean twitter = false;
        
        BLAuthSigner signer = new BLAuthSigner(consumerKey, consumerSecret);
        if (twitter)
        {
            signer = new BLAuthSigner(consumerKey, consumerSecretTwitter);
            signer.setToken(tokenValue, tokenSecretTwitter);
        }
        else
        {
            signer = new BLAuthSigner(consumerKey, consumerSecret);
            signer.setToken(tokenValue, tokenSecret);
        }
        
        signer.setVerb("GET");
        String baseURL = "https://api.bricklink.com/api/store/v1/items/part/" + partnumber
                + "/price?color_id=11&guide_type=sold";
        // signer.addParameter("type", "part");
        // signer.addParameter("no", partnumber);
        signer.addParameter("color_id", "11");
        signer.addParameter("guide_type", "sold");
        
        // String baseURL = "https://api.bricklink.com/api/store/v1/orders?direction=in";
        // signer.addParameter("direction", "in");
        //
        
        signer.setURL(baseURL);
        
        Map<String, String> params = Collections.emptyMap();
        
        try
        {
            params = signer.getFinalOAuthParams();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        try
        {
            System.out.println(signer.getBaseString());
        }
        catch (Exception e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        String Signature = params.get("oauth_signature");
        String Nonce = params.get("oauth_nonce");
        String Version = params.get("oauth_version");
        String ConsumerKey = params.get("oauth_consumer_key");
        String SigMethod = params.get("oauth_signature_method");
        String Token = params.get("oauth_token");
        String Timestamp = params.get("oauth_timestamp");
        System.out.println(Signature);
        System.out.println(Nonce);
        System.out.println("Version: " + Version);
        System.out.println(ConsumerKey);
        System.out.println(SigMethod);
        System.out.println(Token);
        System.out.println(Timestamp);
        
        String fullURL = baseURL + "&Authorization=%7B%22oauth_signature%22%3A%22" + Signature
                + "%22%2C%22oauth_nonce%22%3A%22" + Nonce + "%22%2C%22oauth_version%22%3A%22"
                + Version + "%22%2C%22oauth_consumer_key%22%3A%22" + ConsumerKey
                + "%22%2C%22oauth_signature_method%22%3A%22" + SigMethod
                + "%22%2C%22oauth_token%22%3A%22" + Token + "%22%2C%22oauth_timestamp%22%3A%22"
                + Timestamp + "%22%7D";
        
        fullURL = fullURL.replace("%22", "\"");
        fullURL = fullURL.replace("%2C", ",");
        fullURL = fullURL.replace("%3A", ":");
        fullURL = fullURL.replace("%7B", "{");
        fullURL = fullURL.replace("%7D", "}");
        
        JSONObject obj = new JSONObject();
        obj.putAll(params);
        
        System.out.println(fullURL);
        System.out.println(obj);
        
        // try
        // {
        // System.out.println(getHTML(fullURL));
        // }
        // catch (Exception e)
        // {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        
    }
    
    public static String getHTML(String urlToRead) throws Exception
    {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null)
        {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }
}
