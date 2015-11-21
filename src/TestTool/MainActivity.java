package TestTool;
import javax.swing.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.management.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity {
	
	
	
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		///////////////User Time///////////////
		long startTime = System.nanoTime();
		
//////////////////////////MAIN CODE//////////////////////////
		Gui gui = new Gui();
		JFrame frame = gui;
		frame.show();
//////////////////////////MAIN CODE//////////////////////////
		
		long endTime = System.nanoTime();
		long totalTime = endTime-startTime;
		//System.out.println("creating total run time: " + totalTime + " nanoseconds");
		///////////////User Time///////////////
		GenReport stat = new GenReport();
		
		stat.genStat(totalTime,gui.getReportTime());
			    
	}
		
	
}
