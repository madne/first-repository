package mainPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CheckSome {
	
	public static boolean different(String path1,String path2) throws IOException{
		FileReader fr1 = new FileReader(path1);
		BufferedReader br1 = new BufferedReader(fr1);
		FileReader fr2 = new FileReader(path2);
		BufferedReader br2 = new BufferedReader(fr2);
		int x1,x2;
		while((x1=br1.read())!=-1){
			x2=br2.read();
			if(x1!=x2)
				return true;
		}
		return false;
	}
	
	public static void compare(File file1) throws IOException{
		String folderId = SnapShot.getHeadId();
		String path1 = file1.getPath();
		String path2 = ".save\\snapshots\\"+folderId+"\\"+clean(path1);

		File file2 = new File(path2);
		
		if(!file2.exists()){
			System.out.println("- New File: " + file1.getName());
		}else{
			if(different(path1,path2)){
				System.out.println("- Changes in File: " + file1.getName());
			}
		}
	}
	
	public static void checkAll(File fl) throws IOException{
		//check for snapshots using head file
		String headId = SnapShot.getHeadId();
		if(headId==null){
			System.out.println("[-]No snapshots yet");
			return ;
		}
		File[] list = fl.listFiles();
		for(File f : list ){
			if(f.getName().equals(".save"))
				continue;
			
			if(f.isFile()){
				compare(f);
			}else{
				checkAll(f);
			}
		}
	}
	
	public static String clean(String path){
		path = path.substring(2,path.length());
		return path;
	}
}
