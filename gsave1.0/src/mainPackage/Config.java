package mainPackage;



import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class Config {
	public static String system = null;
    public static String gmail = null;
    public static String password = null;
    public boolean allSet = false;
    
    public static void resetConfigurations() throws IOException{
    	JSONObject ob = new JSONObject();
    	String ch;
    	Scanner sc = new Scanner(System.in);
    	System.out.println("[*]Resetting gonfigurations");
    	System.out.print("[*]Gmail Account: ");
    	ch = sc.next();
    	gmail=ch;
    	ob.put("gmail", ch);
    	System.out.print("[*]Gmail Password: ");
    	ch = sc.next();
    	password=ch;
    	ob.put("password", ch);
    	System.out.print("[*]System: ");
    	ch = sc.next();
    	system=ch;
    	ob.put("system",ch);
    	ch = ob.toJSONString();
    	FileWriter fw = new FileWriter(".save/config.json");
    	BufferedWriter bw = new BufferedWriter(fw);
    	bw.write(ch);
    	bw.close();
    	System.out.println("[+]All cufigurations set");
    }
    
	public Config() throws IOException, ParseException {
		//parse file and set variables 
		FileReader rd = new FileReader(".save/config.json");
		
		
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
			System.out.println("ParseError: password var not defined!");
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
