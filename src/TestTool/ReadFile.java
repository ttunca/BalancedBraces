package TestTool;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadFile {
	public ArrayList<String> readFile(String filename){
		ArrayList<String> AL = new ArrayList<String>();
		String lineOfText = "";
		try{
			File myFile = new File(filename);
			Scanner fileScan = new Scanner(myFile);
		
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
