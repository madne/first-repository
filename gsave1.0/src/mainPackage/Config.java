package mainPackage;



import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class Config {
	public String system = null;
    public String gmail = null;
    public String password = null;
    public boolean allSet = false;
    
	public Config() throws IOException, ParseException {
		//parse file and set variables 
		FileReader rd = new FileReader(".save/config");
		
		
		JSONParser parser  = new JSONParser();
		JSONObject ob = (JSONObject) parser.parse(new FileReader(".save/config.json"));
		if(!ob.containsKey("system")){
			System.out.println("ParseError:system var not defined!");
			System.out.println("Try gsave config");
			return;
		}
		if(!ob.containsKey("gmail")){
			System.out.println("ParseError:gmail var not defined!");
			System.out.println("Try gsave config");
			return;
		}
		if(!ob.containsKey("password")){
			System.out.println("ParseError:password var not defined!");
			System.out.println("Try gsave config");
			return;
		}
		if(!ob.containsKey("gmail")){
			System.out.println("ParseError:system var not defined!");
			System.out.println("Try gsave config");
			return;
		}
		
		//getting value:
		system = (String) ob.get("system");
		gmail = (String) ob.get("gmail");
		password = (String) ob.get("password");
		
		allSet=true;
		
	}
	
	
	
	
	
	
	
	@Override
	public String toString() {
		return "Config [system=" + system + ", gmail=" + gmail + ", gmailPassword=" + password + "]";
	}

	    
}
