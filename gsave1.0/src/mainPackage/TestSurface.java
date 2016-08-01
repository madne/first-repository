package mainPackage;

import java.io.File;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class TestSurface {

	
	
	
	public static void main(String[] args) throws IOException, ParseException {
		
		 JSONObject ob = ApiRequest.getJsonFromUrl("http://localhost/test.php");
		 System.out.println(ob.get("name"));
		 System.out.println(ob.get("age"));
	}
	
	
	
	
	
	
	public void copyFolderStructure(String source,String dest){
		File sc = new File(source);
		File des = new File(dest);
		File[] table = sc.listFiles();
		for(File f : table){
			if(f.isFile()){
				
			}else{
				//wrong
				File newFile = new File(f.getName());
				copyFolderStructure(f.getName(),".");
			}
		}
	}
	


}
