
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;


public class ApiRequest {
	public static String getText(String url) throws IOException{
		InputStream is = new URL(url).openStream();
		try {
		      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		      StringBuilder sb = new StringBuilder();
		      int cp;
			    while ((cp = rd.read()) != -1) {
				      sb.append((char) cp);
				    }

		      String text = sb.toString(); 
		      return text;
		    } finally {
		      is.close();
		    }
	}
	
	
	public static JSONObject getJsonFromUrl(String url) throws ParseException, IOException{
		JSONParser parser  = new JSONParser();
		JSONObject ob = (JSONObject) parser.parse(getText(url));
		return ob;
	}
		
}
