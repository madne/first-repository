package mainPackage;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileManip {
	

	public static boolean checkInit(){
		File save = new File(".save");
		return save.exists();
	}
	
	
	public static boolean fullCheck(){
		File save = new File(".save");
		File config = new File(".save/config.json");
		File snapLog = new File(".save/snaplog.json");
		File snapshots = new File(".save/snapshots");

		if(!save.exists()){
		System.out.println("[-]Fatal Error: Cannot find directory .save");
		System.out.println("[-]Try gsave init");
		return false;
		}
		if(!config.exists()){
			System.out.println("[-]Error: Cannot find file .save/config.json");
			System.out.println("[-]Try gsave config");
			return false;
		}
		if(!snapLog.exists()){
			System.out.println("[-]Fatal Error: Cannot find file .save/snaplog.json");
			System.out.println("[-]Try gsave init");//all project must be reinitiated
			//reinitiation of snaplog is complex 
			return false;
		}
		if(!snapshots.exists()){
			System.out.println("[-]Fatal Error: Cannot find directory .save/spanshots");
			System.out.println("[-]Try gsave init");
			return false;
		}
		return true;

	}

	
	//delete a directory even if its filled
		public static void delete_rf(String filePath){
			File up = new File(filePath);
			File[] list = up.listFiles();
			for(File f: list){
				if(f.isFile()){
					f.delete();
				}else{
					if(f.getName().equals(".save"))
						continue;
					delete_rf(f.getAbsolutePath());
					f.delete();
				}
			}
			up.delete();
		}
		
	public static boolean init() throws FileNotFoundException, IOException, ParseException{
		
		File save = new File(".save");
		File config = new File(".save/config.json");
		File snapLog = new File(".save/snaplog.json");
		File snapshots = new File(".save/snapshots");
		File head = new File(".save/head.txt");
		
		try{
			save.mkdir();
		}catch(Exception e){
			System.out.println("[-]Initiation failed: could not create directory .save");
			return false;
		}
		
		try{
			snapshots.mkdir();
		}catch(Exception e){
			System.out.println("[-]Initiation failed: could not create directory .save/snapshots");
			save.delete();
			return false;
		}
		
		try{
			config.createNewFile();
		}catch(Exception e){
			save.delete();
			snapshots.delete();
			System.out.println("[-]Initiation failed: could not create file .save/config.json");
			return false;
		}
		
		try{
			snapLog.createNewFile();
		}catch(Exception e){
			save.delete();
			snapshots.delete();
			config.delete();
			System.out.println("[-]Initiation failed: could not create file .save/snaplog.json");
			return false;
		}
		try{
			head.createNewFile();
		}catch(Exception e){
			save.delete();
			snapshots.delete();
			config.delete();
			snapLog.delete();
			System.out.println("[-]Initiation failed: could not create file .save/head.txt");
			return false;
		}
		
		
		
		//initiate snaplog.json
		FileWriter fw = new FileWriter(".save/snaplog.json");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("[]");//empty JSONArray
		bw.close();
		
		//make json object
		String ch;
		JSONObject ob = new JSONObject();
		Scanner sc = new Scanner(System.in);
		System.out.println("[?]System: ");
		ch = sc.next();
		ob.put("system",ch);

		System.out.println("[?]Gmail: ");
		ch = sc.next();
		ob.put("gmail",ch);
		
		System.out.println("[?]Password: ");
		ch = sc.next();
		ob.put("password",ch);
		String text = ob.toJSONString();
		FileWriter fw1 = new FileWriter(".save/config.json");
		BufferedWriter bw1 = new BufferedWriter(fw1);
		try{
		bw1.write(text);
		}catch(Exception e){
			return false;
		}
		bw.close();
		bw1.close();
		return true;
		
	}
	
	
	
	public static void copyFile(String source_, String dest) throws IOException {
		String cdir = System.getProperty("user.dir"); 
		
		File sourceFile = new File(cdir + '/' + source_);
		File destFile = new File(cdir + '/' + dest);
		 
		if(!destFile.exists()) {
	        destFile.createNewFile();
	    }
	    
	    FileChannel source = null;
	    FileChannel destination = null;

	    try {
	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();
	        destination.transferFrom(source, 0, source.size());
	    }
	    finally {
	        if(source != null) {
	            source.close();
	        }
	        if(destination != null) {
	            destination.close();
	        }
	    }
	}

	
}
