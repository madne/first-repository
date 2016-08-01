package mainPackage;

import java.io.*;
import java.util.Scanner;

import org.json.simple.parser.ParseException;


public class MainClass {

	

		public static void listOptions(){
			System.out.println("listing all options:");
			System.out.println("--------------------");
			System.out.println("gsave init");
			System.out.println("gsave snapshot");
			System.out.println("gsave status");
			System.out.println("gsave changeStatus statusName");
			System.out.println("gsave upload");
		}
		
		public static boolean configCheck;
		public static boolean initCheck;
		
		public static void main(String[] args) throws IOException, ParseException {
			Config cf = null;
			
			
			
			//num args
			String cmd;
			if(args.length==0){
				listOptions();
				return;
			}
			
			
			
			cmd = args[0];
			if(cmd.equals("init")){
				if(FileManip.checkInit()){
					System.out.println("[-]Project already initiated ");
					System.out.println("[*]Delete older configuration (y/n):");
					Scanner sc = new Scanner(System.in);
					String answer = sc.next();
					answer = answer.toLowerCase();
					if (!answer.equals("y")&&!answer.equals("yes") ){
						return;
					}
					FileManip.delete_rf(".save");
				}
				
				FileManip.init();
				System.out.println("[+]Project initiated successfully!");
				
			}else{
				//check initiation :
				if(!FileManip.fullCheck()){
					System.out.println("[-]Try gsave init ");
					return ;
				}
				//check configurations
				cf = new Config();
				configCheck = cf.allSet;
				if(!configCheck)
					return;
				
				
				
				if (cmd.equals("changeStatus")){
					System.out.println("command on build state");
				}else if (cmd.equals("upload")){
					System.out.println("command on build stat");
				}else if (cmd.equals("info")){
				
					System.out.println("[+]Project initiated");
					
					//listing all configurations
					System.out.println("[*]All configuurations:");
					System.out.println("----------------------");
					System.out.println("System: " + cf.system);
					System.out.println("Gmail: " +cf.gmail);
					System.out.println("Password: " + cf.password);
					
				}else if (cmd.equals("snapshot")){
					System.out.println("command on build state");
				}else if (cmd.equals("status")){
					System.out.println("command on build state");
				}else if (cmd.equals("config")){
					System.out.println("command on build state");
					//lunch config module (sets configuration file)
				}else{
					System.out.println(cmd + ": Command not found ");
				}
				
			}
			
		}

	}


