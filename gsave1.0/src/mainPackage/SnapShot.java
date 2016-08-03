package mainPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Scanner;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


class SnapShot{


	
	
	//recursive copy to all what's under folder1 to __under__folder2
	public static boolean cloneDirectory(String source,String destination) throws IOException{
		File sc = new File(source);
		File des = new File(destination);
		try{
		File[] list = sc.listFiles();
		for(File f: list){
			if(f.getName().equals(".save")){
				continue;
			}
			File aux = new File(destination +"/"+ f.getName());
			if(f.isFile()){
				aux.createNewFile();
				FileManip.copyFile(f.getPath(),aux.getPath());
			}else{
				aux.mkdir();
				cloneDirectory(f.getPath(),destination +"/"+ f.getName());
			}
		}
		}catch(Exception e){
			System.out.println("[-]Error: Could not Make snapthot (error cloning directorie)");
			return false;
		}
		return true;
	}

	public static void setSnapLogJsonArray(JSONArray arr) throws IOException{
		FileWriter fw = new FileWriter(".save/snaplog.json");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(arr.toString());
		bw.close();
		
	}
	public static boolean makeSnapShot() throws ParseException, IOException{
		
		//get snapshot header details
		Date dt = new Date();
		DateFormat formatDate = new SimpleDateFormat("YYYY-MM-dd/hh:mm:ss"); 
		String totalDate = formatDate.format(dt);
		String[] list = totalDate.split("/");
		String date = list[0];
		String time = list[1];
		int snapId = totalDate.hashCode();
		Scanner sc = new Scanner(System.in);
		System.out.print("Snapshot-Title: ");
		String title = sc.nextLine();
		
		//make json object
		JSONObject ob = new JSONObject();
		ob.put("date", date);
		ob.put("time", time);
		ob.put("id", Integer.toString(snapId));
		ob.put("title", title);
		
		//get current array from snapshots.json
		JSONArray arr = getSnapLogJsonArray();
		arr.add(ob);
		
		//rewrite it back
		setSnapLogJsonArray(arr);
		
		
		File snapDir = new File(".save/snapshots/" + snapId);

		if(!snapDir.mkdir()){
			System.out.println("[-]Error: could not create directory for snapshot");
			return false;
		}
		
		//point head to this new snapshot
		FileWriter fw2 = new FileWriter(".save/head.txt");
		BufferedWriter bw2 = new BufferedWriter(fw2);
		bw2.write(Integer.toString(snapId));
		bw2.close();
		
		
		return cloneDirectory(".",".save/snapshots/" + snapId);
	}
	public static String getHeadId() throws IOException{
		//check file initiation
		File head = new File(".save/head.txt");
		if(head.length()==0)
			return null;

		FileReader fr = new FileReader(".save/head.txt");
		BufferedReader br = new BufferedReader(fr);
		String id = br.readLine();
		br.close();
		fr.close();
		return id;
	}
	
	public static void getCurrentHeadInfo() throws IOException, ParseException{

		String date = "";
		String time = "";
		String title = "";
		
		String id = getHeadId();

		if(id==null){
			System.out.println("No Snapshots yet");
			return;
		}

		JSONArray arr = getSnapLogJsonArray();
		
		int size = arr.size();
		for(int i=0;i<size;i++){
			JSONObject ob11 = (JSONObject) arr.get(i);
			String currentId = (String) ob11.get("id");
			if (currentId.equals(id)){
				title = (String) ob11.get("title");
				date = (String) ob11.get("date");
				time = (String) ob11.get("time");
	
				System.out.println("\ttitle  :" + title);
				System.out.println("\tdate   :" + date);
				System.out.println("\ttime   :" + time);
				System.out.println("\tId     :" + id);
				break;
			}
		}//for
	}

	public static JSONArray getSnapLogJsonArray() throws ParseException, IOException{
		FileReader fr = new FileReader(".save/snaplog.json");
		BufferedReader br = new BufferedReader(fr);
		StringBuilder sb = new StringBuilder();
		int x;
		while( (x=br.read())!=-1){
			sb.append((char)x);
		}
		String str = sb.toString();
		JSONParser parser = new JSONParser();
		JSONArray arr = (JSONArray) parser.parse(str);
		return arr;
	}
	
	public static void getLog() throws ParseException, IOException{
		JSONArray arr = getSnapLogJsonArray();
		int size = arr.size();
		System.out.println("[*]All Snapshots:");
		System.out.println("-------------------");
		for(int i=0;i<size;i++){
			JSONObject ob = (JSONObject) arr.get(i);
			System.out.println("title:" + ob.get("title"));
			System.out.println("date:" + ob.get("date"));
			System.out.println("time:" + ob.get("time"));
			System.out.println("id:" + ob.get("id"));
		}
		System.out.println("-------------------");
	}
	
	public static JSONObject selectJsonObject() throws IOException, ParseException{
		System.out.println("[*]Select Snapshot by Id:");
		System.out.println("-------------------------");
		
		//get all snapshots
		JSONArray arr = getSnapLogJsonArray();
		int size = arr.size();
		for(int i=0;i<size;i++){
			JSONObject ob = (JSONObject) arr.get(i);
			String title = (String) ob.get("title");
			String date = (String) ob.get("date");
			String time = (String) ob.get("time");
			System.out.printf("id: %d\t%10s\t%10s\t%s10\n", i+1,title,date,time);
		}
		
		//get user input
		System.out.print("[*]Give id: ");
		Scanner sc = new Scanner(System.in);
		int x1 = sc.nextInt();
		if(x1>size||x1<=0){
			System.out.println("invalid index!");
			return null;
		}
		JSONObject ob  = (JSONObject) arr.get(x1-1);
		return ob;
	}
	
	public static  void switchSnapshot() throws ParseException, IOException{
		
		JSONObject ob = selectJsonObject();
		
		//update the head:
		String newId = (String) ob.get("id");
		FileWriter fw = new FileWriter(".save/head.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(newId);
		bw.close();
		
		
		//update work directory
		//delete current files
		File fl = new File(".");
		File[] list = fl.listFiles();
		for(File f:list){
			if(f.getName().equals(".save"))
				continue;
			if(f.isFile()){
				f.delete();	
			}
			else{
				FileManip.delete_rf("./"+f.getName());
			}
		}
		
		//get new files from .save/snapshots/newId  to .
		cloneDirectory(".save/snapshots/" + newId, ".");
		System.out.println("[+]Success ");
		
	}
	
	
	public static void deleteSnapShot() throws IOException, ParseException{
		JSONObject ob = selectJsonObject();
		//warn if selected ob is the head object:
		String selectedId = (String) ob.get("id");
		String headId = getHeadId();
		if(selectedId.equals(headId)){
			System.out.println("[-]Selected Snapshot is head snapshot !");
			System.out.println("[-]try switch or make a new snapshot before delete operation");
			return ;
		}
		
		//delete snapshot from snaplog
		JSONArray arr = getSnapLogJsonArray();
		arr.remove(ob);
		
		//write changes
		setSnapLogJsonArray(arr);
		
		//delete associated save folder(discomment when executuble is made)
		FileManip.delete_rf(".save/snapshots/"+selectedId);
		System.out.println("[+]Success ");
	}
}