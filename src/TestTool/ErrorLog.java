package TestTool;

import java.util.ArrayList;

import javax.swing.JTextArea;

public class ErrorLog {
	public int line; //index of the line of the text file (index begins at 1)
	public int pos; //index of the character in a String of all characters in the line (index begins at 0)
	public char ch; //character
	public String str; //optional variable to store message regarding a detected error
	
	
	public ErrorLog(int line, int pos, char ch){
		this.line = line;
		this.pos = pos;
		this.ch = ch;
		str = ""; //initialized, providing the ability to enter and store an error message at a time after object instantiation
	}
	public ErrorLog(int line, int pos, char ch, String str){
		this.line = line;
		this.pos = pos;
		this.ch = ch;
		this.str = str;
	}
	public ErrorLog(){
		super();
	}
	
	public static void prnt(ArrayList<ErrorLog> a){
		for(int i=0; i<a.size(); i++){
			ErrorLog el = a.get(i);
			System.out.println(el.str + "line:"+el.line+", pos:"+el.pos+", char:"+el.ch);
		}
	}

	
}
