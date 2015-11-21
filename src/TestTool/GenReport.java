package TestTool;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
public class GenReport {
	Path filepath;
	String filename;
	static private final String newline = "\n";
	private static final long MEGABYTE = 1024L * 1024L;
	public static long bytesToMegabytes(long bytes) {
	    return bytes / MEGABYTE;
	  }

	public GenReport(){			
	}
	
	public void configReport(ReadFile f){
		filepath = Paths.get(f.file.getPath()+ "_report.txt");
		filename = f.file.getName();
	}
	
	public void genTextFile(ArrayList<ErrorLog> a){
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(filename + "_report.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||" + newline);
		writer.println("|||                           Balance Brace Report                           |||" + newline);
		writer.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||" + newline);
		writer.println( newline);
		writer.println("||||||||||||||||||||||||||||||||||Generate Time|||||||||||||||||||||||||||||||||" + newline);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		//get current date time with Date()
		Calendar cal = Calendar.getInstance();
		writer.println(dateFormat.format(cal.getTime()) + newline);
		writer.println( newline);
		writer.println("|||||||||||||||||||Unbalanced Braces, Brackets, and Parentheses:||||||||||||||||" + newline);
		for(int i=0; i<a.size(); i++){
			ErrorLog el = a.get(i);
			writer.println(el.str + "line:"+el.line+", pos:"+el.pos+", char:"+el.ch);
			//System.out.println(el.str + "line:"+el.line+", pos:"+el.pos+", char:"+el.ch);
		}
		writer.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||" + newline);
		writer.close();
	}
	
	public void genTextFile2(ArrayList<ErrorLog> a){
		Charset charset = Charset.forName("US-ASCII");
		String s = "hello";
		try (BufferedWriter writer = Files.newBufferedWriter(filepath, charset)) {
		    writer.write(s, 0, s.length());
		    writer.write("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||" + newline);
		    writer.write("|||                           Balance Brace Report                           |||" + newline);
			writer.write("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||" + newline);
			writer.write( newline);
			writer.write("||||||||||||||||||||||||||||||||||Generate Time|||||||||||||||||||||||||||||||||" + newline);
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			//get current date time with Date()
			Calendar cal = Calendar.getInstance();
			writer.write(dateFormat.format(cal.getTime()) + newline);
			writer.write( newline);
			writer.write("|||||||||||||||||||Unbalanced Braces, Brackets, and Parentheses:||||||||||||||||" + newline);
			for(int i=0; i<a.size(); i++){
				ErrorLog el = a.get(i);
				writer.write(el.str + "line:"+el.line+", pos:"+el.pos+", char:"+el.ch);
				//System.out.println(el.str + "line:"+el.line+", pos:"+el.pos+", char:"+el.ch);
			}
			writer.write("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||" + newline);
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
	}
	
	public void genStat(long totalTime, long genReportTime){
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("stats.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||" + newline);
		writer.println("|||                          Statistics Report                           |||" + newline);
		writer.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||" + newline);
		writer.println( newline);
		writer.println("||||||||||||||||||||||||||||||||||Generate Time|||||||||||||||||||||||||||||||||" + newline);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		writer.println(dateFormat.format(cal.getTime()) + newline);
		writer.println( newline);
		
		//writing total time in the text file//
		writer.println("Creating total run time: " + totalTime + " nanoseconds");
		//writing total time in the text file//
		
		writer.println("Creating total time for creating a Gen Report: " + genReportTime + " nanoseconds");
		
		//////////////////////////CPU Thread Time Test//////////////////////////
		ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
		writer.print("CPU time: ");
		writer.print("" + bean.isCurrentThreadCpuTimeSupported( ) != null ? bean.getCurrentThreadCpuTime( ) : 0L);
		writer.println(" nanoseconds");
		//////////////////////////CPU Thread Time Test//////////////////////////		
		
		
		
		
//////////////////////////memory test//////////////////////////
		 // Get the Java runtime
	    Runtime runtime = Runtime.getRuntime();
	    // Run the garbage collector
	    runtime.gc();
	    // Calculate the used memory
		long memory = runtime.totalMemory() - runtime.freeMemory();
		writer.println("Used memory in bytes: " + memory + " bytes");
		writer.println("Used memory in megabytes: " + bytesToMegabytes(memory) + " mb");
//////////////////////////memory test//////////////////////////
	    writer.close(); 

	}


}
