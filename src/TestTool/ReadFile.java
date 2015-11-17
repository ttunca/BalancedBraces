package TestTool;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadFile {
	File file;
	public ArrayList<String> readFile(String filename){
		ArrayList<String> AL = new ArrayList<String>();
		String lineOfText = "";
		try{
			file = new File(filename);
			Scanner fileScan = new Scanner(file);
		
			while(fileScan.hasNextLine()){
				lineOfText = fileScan.nextLine();
				AL.add(lineOfText);
			}
			fileScan.close();
		
		}catch(Exception excptn){
            //excptn.printStackTrace();
        }
		return AL;
	}
}
