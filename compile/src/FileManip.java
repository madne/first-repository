
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
			System.out.println("init failed: could not create directory .save");
			return false;
		}
		
		try{
			snapshots.mkdir();
		}catch(Exception e){
			System.out.println("init failed: could not create directory .save/snapshots");
			save.delete();
			return false;
		}
		
		try{
			config.createNewFile();
		}catch(Exception e){
			save.delete();
			snapshots.delete();
			System.out.println("init failed: could not create file .save/config.json");
			return false;
		}
		
		try{
			snapLog.createNewFile();
		}catch(Exception e){
			save.delete();
			snapshots.delete();
			config.delete();
			System.out.println("init failed: could not create file .save/snaplog.json");
			return false;
		}
		try{
			head.createNewFile();
		}catch(Exception e){
			save.delete();
			snapshots.delete();
			config.delete();
			snapLog.delete();
			System.out.println("init failed: could not create file .save/head.txt");
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
		System.out.println("System:");
		ch = sc.next();
		ob.put("system",ch);

		System.out.println("gmail:");
		ch = sc.next();
		ob.put("gmail",ch);
		
		System.out.println("password:");
		ch = sc.next();
		ob.put("password",ch);
		String text = ob.toJSONString();
		FileWriter fw1 = new FileWriter(".save/config.json");
		BufferedWriter bw1 = new BufferedWriter(fw1);
		try{
		bw1.write(text);
		}catch(Exception e){
			System.out.println("in here:");
			return false;
		}
		bw.close();
		bw1.close();
		return true;
		
	}
	
	public static void init101(){
		boolean del = true;
		String cdir = System.getProperty("user.dir"); 
		File dir = new File(cdir + "/.save");
		if(dir.exists()){
			if(dir.isDirectory())
				System.out.println(".save already exists as a directory !");
			else
				System.out.println(".save already exists as a file!");
			
			System.out.println("overwrite it (y/n)?  ");
			Scanner sc = new Scanner(System.in);
			String  c ;
			c=sc.next();
			if(!c.equals("y")){
				System.out.println("initiation failed");
				del = false;
			}else{
				//delete file or folder
				if(!dir.delete()){
					System.out.println("initiation failed: could not delete .save");
				}
			}
		}
		if(del){
			//start initiation:
			if(!dir.mkdir()){
				System.out.println("initiation failed: could not create directory .save ");
			}else{
				File conf = new File (cdir + "/.save/config.json");
				try{
					conf.createNewFile();
				}catch(Exception e){
					System.out.println("initiatin failed: could not create file config.json");
					del = false;
					dir.delete();
				}
				
				if(del){
					//initiate config file
					String config = "";
					String val;
					System.out.println("initiation config file");
					System.out.println("gmail Account: ");
					Scanner sc = new Scanner(System.in);
					val = sc.next();
					config = "gmail="+val+"\n";
					System.out.println("gmail Password: ");
					val = sc.next();
					config +="password="+val+"\n";
					System.out.println("system: ");
					val = sc.next();
					config+="system="+val+"\n";
					
					//print config var to config file:
					try {
			            // Assume default encoding.
			            FileWriter fileWriter = new FileWriter(".save/config.json");
			            
			            BufferedWriter bufferedWriter =	new BufferedWriter(fileWriter);
			            bufferedWriter.write(config);
			         
			            bufferedWriter.close();
			        }catch(Exception e){
			        	System.out.println("initation failed : unable to write to file config.json");
			        	//delete files created:
			        	conf.delete();
			        	dir.delete();
			        }
				}
			}
		}
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
