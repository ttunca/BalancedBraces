package TestTool;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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


}
