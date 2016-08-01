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


	public static void copyFile(String sc,String dest) throws IOException{
		FileReader fr = new FileReader(sc);
		FileWriter fw = new FileWriter(dest);
		BufferedReader br = new BufferedReader(fr);
		BufferedWriter bw = new BufferedWriter(fw);
		String buff;
		while(( buff = br.readLine())!=null){
			bw.write(buff);
			bw.newLine();
		}
		bw.close();
	}
	
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
				copyFile(f.getPath(),aux.getPath());
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
		ob.put("id", snapId);
		ob.put("title", title);
		
		//get current array from snapshots.json
		StringBuilder sb = new StringBuilder();
		try {
		FileReader fr = new FileReader(".save/snaplog.json");	
		BufferedReader rd = new BufferedReader(fr);
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		String text = sb.toString(); 
		rd.close();
		fr.close();
		}catch(Exception e){
			System.out.println("[-]Error reading from file snaplog.json");
		}
		String str = sb.toString();
		JSONArray arr = new JSONArray();
		JSONParser parser = new JSONParser();
		try{
		arr = (JSONArray) parser.parse(str);
		arr.add(ob);
		
		//rewrite it back
		FileWriter fw = new FileWriter(".save/snaplog.json");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(arr.toString());
		bw.close();
		}catch(Exception e){
			System.out.println("[-]Fatal Error: parsing sanplog.json");
			return false;
		}
		
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
	
	public static void getCurrentHeadInfo() throws IOException, ParseException{
		FileReader fr = new FileReader(".save/head.txt");
		BufferedReader br = new BufferedReader(fr);
		String id = br.readLine();
		br.close();
		fr.close();
		
		FileReader fr1 = new FileReader(".save/snaplog.json");
		BufferedReader br1 = new BufferedReader(fr1);
		
		StringBuilder str = new StringBuilder();
		int aux;
		while((aux = br1.read()) !=-1){
			str.append((char) aux);
		}
		String str1 = str.toString();
		String date = "";
		String time = "";
		String title = "";
		JSONParser parser = new JSONParser();
		JSONArray arr = (JSONArray) parser.parse(str1);
		JSONObject[] list =  arr.toArray();//fix this
		for(JSONObject obj: list){
			int currentid = (int) obj.get("id");
			if (Integer.toString(currentid).equals(id)){
				System.out.println("	title:" + title);
				System.out.println("	date:" + date);
				System.out.println("	time:" + time);
				break;
			}
		}
	}

	
}