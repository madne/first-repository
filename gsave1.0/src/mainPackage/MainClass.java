package mainPackage;

import java.io.*;
import java.util.Scanner;

import org.json.simple.parser.ParseException;


public class MainClass {

	
		public static void listConfigurations(){
			//listing all configurations
			System.out.println("[*]Listing all configurations:");
			System.out.println("-----------------------------");
			System.out.println("System: " + cf.system);
			System.out.println("Gmail: " +cf.gmail);
			System.out.println("Password: " + cf.password);
			System.out.println("Corrent Snapshot: " );
		}

		public static void listOptions(){
			System.out.println("[*]listing all options:");
			System.out.println("----------------------");
			System.out.println("->gsave init");
			System.out.println("->gsave snapshot");
			System.out.println("->gsave status");
			System.out.println("->gsave info");
			System.out.println("->gsave log");
			System.out.println("->gsave switch");
			System.out.println("->gsave delete");
			System.out.println("->gsave upload (onbuild)");
		}
		
		public static boolean configCheck;
		public static boolean initCheck;
		public static Config cf = null;
		
		public static void main(String[] args) throws IOException, ParseException {
	
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
					System.out.print("[*]Delete older configuration (y/n): ");
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
					return ;
				}
				//check configurations
				cf = new Config();
				configCheck = cf.allSet;
				if(!configCheck)
					return;
				
				if (cmd.equals("config")){
					cf.resetConfigurations();
				}else if (cmd.equals("upload")){
					System.out.println("[-]Command on build state");
				}else if (cmd.equals("delete")){
					SnapShot.deleteSnapShot(); 
				}else if (cmd.equals("info")){
					System.out.println("[+]Project initiated");
					listConfigurations();
					SnapShot.getCurrentHeadInfo();
					System.out.println("-------------------------");
				}else if (cmd.equals("snapshot")){
					if(!SnapShot.makeSnapShot()){
						System.out.println("[-]Check Write permission !");
					}
				}else if (cmd.equals("status")){
					File fl = new File(".");
					CheckSome.checkAll(fl);
				}else if (cmd.equals("log")){
					SnapShot.getLog();
				}else if (cmd.equals("switch")){
					SnapShot.switchSnapshot();
				}else{
					System.out.println("[-]"+cmd + ": Command not found ");
				}		
			}
		}
	}


