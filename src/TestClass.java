/*
 Copyright 2015 Robyn J. Silber

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

/*** OBJECTIVE:
 * This system was designed to check whether a user's Java code has been properly balanced with
 * respect to orderly opening and closing of curly braces, brackets, and parenthesis.
 * */

/* How the System Interacts with the Test Code:
 * 1. Declare 4 global, static data structures.
 * 2. Parse through a text file that contains the user's Java code. Store each line of code in a
 *     data structure (ds1). This data structure is of type ArrayList<String>. It has an empty
 *     String at index=0. It is arbitrary. The remaining elements from index=1 to index=A.size()-1
 *     store the respective lines of test code according to the line numbering of the text editor.
 * 3. Declare 2 more data structures within the main method.
 * 4. Find, Retrieve, and Remove all commented text from ds1. Relocate the removed text to a data
 *     structure that was declared in the main method (main-ds1).
 * 5. Find, Retrieve, and Remove all the text in the code that is housed between double quotations
 *     within ds1. Replace all previous text between double quotations with a space. Relocate the
 *     removed text to the second data structure that was declared in the main method (main-ds2).
 * 6. Now that all comments and strings have been removed from ds1, parse through the remaining
 *     content. Locate each occurrence of a brace character (curly brace, parenthesis, bracket).
 *     If it is an opening brace character ('{', '[', or '(' ), push() it into the Stack (ds2). If
 *     it is a closing brace character ('}', ']', or ')' ), pop() the Stack (ds2). Check to make
 *     sure that the popped char value is the respective opening brace of the closing brace. If so,
 *     both chars will be added to the third data structure (ds3). Otherwise, both chars will be added
 *     to the fourth data structure, which logs all unresolved issues. An unresolved issue is occurs
 *     whenever the Stack is popped and returns an opening brace char that is not the proper brace char
 *     of the identified closing brace char.
 * */

/*** USER MANUAL:
 * - The purpose of this system is to perform software testing on your Java code.
 * - The purpose of running this system is to detect the occurrence of unbalanced braces, and
 *    then generate a report to the console specifying all the locations of unbalanced braces.
 *  
 * - Download and unzip the archived file. Import the archived file to your Eclipse Workspace.
 * - Open the project's File Directory. Locate the text file (file with the extension .txt).
 * - Double click on "data.txt" from the project directory to open the file. Delete the contents
 *    of data.txt, and paste in the Java code that you would like to test. Be sure to save all changes.
 * - From the project directory, open the src folder, and then open the package (default package). In
 *    the default package, find the class named "TestClass.java", and double click on it to open it.
 * - Go to the menu bar of widgets located at the top of your Eclipse window. Locate the widget icon of
 *    a green circle with a white, right-side-pointing triangle in the center of the green circle. Click
 *    on this green circle to test your code.
 * - The results will be printed to the console. 
 * */

public class TestClass {
	
	public static Stack<ErrorLoggingData> S = new Stack<ErrorLoggingData>(); 					// (ds2)
	public static ArrayList<ErrorLoggingData> ELD = new ArrayList<ErrorLoggingData>();			// (ds4)
	public static ArrayList<ErrorLoggingData> resolvErr = new ArrayList<ErrorLoggingData>();	// (ds3)
	public static ArrayList<String> A = new ArrayList<String>(); 								// (ds1)
	
	/*** Dictionary
	 * 1. Opening Chars: refers to one of the following three chars: '{', '[', '('
	 * 2. Closing Chars: refers to one of the following three chars: '}', ']', ')'
	 * 3. "Proper" or "Expected" Opening/Closing Char: for each of the three opening chars, there
	 * 				  is a specific char to close it with. See method ifElseCheck(char c1, char c2)
	 * 4. "Test Code": refers to the code that the user of this software test will be pasting into
	 * 				   the text file data.txt to be tested
	 * 5. "Braces" : can refer to any of the following: "Brackets", "Curly Braces",	"Parentheses"	 
	 * 6. "Unresolved Issues": Brackets, Curly Braces, and/or Parentheses that were not properly
	 * 							opened and/or closed. This means either that the opening brace was
	 * 							found without its respective closing brace, OR that the closing
	 * 							brace was found without the prior, respective opening brace.
	 * 7. "Respective Brace": see method "ifElseCheck(char c1, char c2)", where the return value
	 * 						   is true if the input chars are respective braces, or false if not.
	 **/
	
	
	/* Rules:
	 * 1. Opening and closing of chars must be done in the proper order, meaning that
	 *    when a char such as '{' is opened followed by some sequence of other opening
	 *    chars, each opening char must be closed in order of LIFO.
	 * 2. Types of Test Code that will be ignored by the Software Test:
	 * 				- code located between two double quotations (Strings)
	 * 				- code located between the comment delimiters slash+asterisk 
	 * 				   and asterisk+slash (multi-line comments)
	 * 				- all code on the line following two consecutive slashes (single-line comments)
	 * 3. Text editors (like the text editor that comes with Eclipse IDE) begin the line
	 *    index at 1 (instead of 0). Since we will be using the ArrayList data structure
	 *    to store each line of test code as Strings, we will be inserting a blank String
	 *    as element 0 of the ArrayList. Therefore, all testing of this ArrayList of Strings
	 *    will begin from index=1. 
	 * */
	
	
	/* Cases:
	 * Case 1: An opening char is located in the text file. Perform S.push()
	 * 		    to add it into Stack S.
	 * Case 2: A closing char is located in text file. S.peek() is performed,
	 * 		    and returns the expected opening char. No errors/resolves to
	 * 		    report. Continue.
	 * Case 3: A closing char is located in the text file. S.peek() is performed
	 * 		    and the value returned is not the expected opening char
	 * 
	 * */
	

	
	public static void main(String[] args) {
		ReadFromAFile fileread = new ReadFromAFile();
		A = fileread.readFile("data.txt");
		A.add(0, "");
		
		@SuppressWarnings("unused")
		ArrayList<String[]> removedComments = removeComments(); 			//(main-ds1)
		//**TO SEE THE COMMENTS LOCATED IN THE TEXT FILE, UNCOMMENT THE 4 LINES BELOW:
//		for(int i=0; i<removedComments.size(); i++){
//			String[] s = removedComments.get(i);
//			System.out.println("Line "+s[0]+": "+s[1]);
//		}
		
		
		@SuppressWarnings("unused")
		ArrayList<String> deletedStrings = removeStrings();					//(main-ds2)
		//**TO SEE THE STRINGS LOCATED IN THE TEXT FILE, UNCOMMENT THE 4 LINES BELOW:
//		System.out.println("\n\nDeleted Strings:");
//		for(int i=0; i<deletedStrings.size(); i++){
//			System.out.println(deletedStrings.get(i));
//		}
		
		balanceBraces();
		System.out.println("\n\nUnbalanced Braces, Brackets, and Parentheses:");
		prnt(ELD);
		
		//**TO SEE ALL PROPERLY BALANCED BRACES, UNCOMMENT THE 2 LINES BELOW:
//		System.out.println("\n\nResolved:");
//		prnt(resolvErr);
		
		
	}
	
	
	public static void balanceBraces(){
		ErrorLoggingData elogdat = new ErrorLoggingData();
		ErrorLoggingData elogdatPeek = new ErrorLoggingData();

		for(int i=0; i<A.size(); i++){
			String line = A.get(i);
			
			for(int j=0; j<line.length();j++){
				char c = line.charAt(j);
				if((c=='{') || (c=='[') || (c=='(')){
					elogdat = new ErrorLoggingData(i,j,c);
					S.push(elogdat);
					elogdat = null;
				}else if((c=='}') || (c==']') || (c==')')){
					elogdat = new ErrorLoggingData(i,j,c);
					elogdatPeek = S.peek();  
					char c2 = elogdat.ch;
					char c1 = elogdatPeek.ch;
					S.pop();
					boolean check = ifElseCheck(c1, c2);
					if(check){
						resolvErr.add(elogdatPeek);
						resolvErr.add(elogdat);
					}else{
						ELD.add(elogdatPeek);
						ELD.add(elogdat);
					}
				}
			}
		}
		while(!S.isEmpty()){
			elogdatPeek = S.peek();
			ELD.add(elogdatPeek);
			S.pop();
		}
	}
	
	/*Perform this before balanceBraces()*/
	public static ArrayList<String> removeStrings(){
		boolean noMoreQuotes = true;
		ArrayList<String> stringsRemoved = new ArrayList<String>();
//		Integer quotes[] = new Integer[4];
		int p1=-1, p2=-1;
		int j, k, M=0;
		for(int i=0; i<A.size(); i++){
			String line = A.get(i);
			for(j=0; j<line.length(); j++){
				if(!noMoreQuotes){
					j = M;
					noMoreQuotes = true;
				}
				char c = line.charAt(j);
				if(c == '"'){
					p1 = j;
//					l1 = i;
					boolean closingQuoteFound = false;
					j++;
					while((!closingQuoteFound) && (j < line.length())){
						c = line.charAt(j);
						if(c=='"'){
							closingQuoteFound = true;
							break;
						}
						j++;
					}
					/*Note: we're assuming that all double quotations within test code are
					 * equally balanced*/
					if(closingQuoteFound){
						/*Closing quotation has been located on same line as opening quotation*/
						p2 = j;
//						l2 = i;
						String oneLine = line.substring(p1+1,p2);
						stringsRemoved.add(oneLine);
						
						String replacement = " ";
						String replacement1 = "";
						String replacement2 = "";
						
						
						for(int m=0; m<=p1; m++){
							replacement1 = replacement1 + line.charAt(m);
						}
						replacement1 += " " + line.charAt(p2);
						for(int m=p2+1; m<line.length(); m++){
							if(line.charAt(m)=='"'){
								M = m;
								noMoreQuotes = false;
							}
							replacement2 = replacement2 + line.charAt(m);
						}
						replacement = replacement1 + replacement2;
						A.set(i, replacement);
						
					}else{
						/*Closing quotation is NOT on same line as opening quotation*/
						String strRemoved = line.substring(p1+1);
						stringsRemoved.add(strRemoved);
						String replacem = line.substring(0,p1+1) + " ";
						A.set(i, replacem);
						i++;
						while(i < A.size()){
							line = A.get(i);
							for(k=0; k<line.length(); k++){
								c = line.charAt(k);
								if(c == '"'){
									p2 = k;
//									l2 = i;
									break;
								}
							}
							String nextRemoved = line.substring(0,k);
							stringsRemoved.add(nextRemoved);
							if(k == line.length()){
								A.set(i, " ");
							}else{
								String remainOfLine = " " + line.substring(k);
								A.set(i, remainOfLine);
								noMoreQuotes = false;
							}
							i++;
						}
					}
					break;
				}
				if(!noMoreQuotes){
					i--;
				}
				
			}
		}
		return stringsRemoved;
	}
	
	
	public static ArrayList<String[]> removeComments(){
		ArrayList<String[]> removedComments = new ArrayList<String[]>();
		for(int i=0; i<A.size(); i++){
			String line = A.get(i);
			for(int j=0; j<line.length()-1; j++){
				if((line.charAt(j)=='/') && (line.charAt(j+1)=='/')){
					String singleLineComment[] = {Integer.toString(j),line.substring(j)};
					removedComments.add(singleLineComment);
					if(j==0){
						A.set(i, " ");
					}else{
						A.set(i, line.substring(0, j));
					}
					break;
				}else if(((line.charAt(j)=='/') && (line.charAt(j+1)=='*'))){
					String notAComment = " ";
					String codeBeforeComment = " ";
					if(j>0){
						codeBeforeComment = line.substring(0, j);
					}
					String codeAfterComment = " ";
					if(line.length()>(j+2)){
						codeAfterComment = line.substring(j+2);
					}
					if((line.length()>(j+2)) && (codeAfterComment.contains("*/"))){
						/*multi-line comment that IS closed on same line as opening*/
						int m=j+1;
						for(m=j+2; m<line.length()-1; m++){
							if((line.charAt(m)=='*') && (line.charAt(m+1)=='/')){
								m++;
								break;
							}
						}
						int m2 = m+1;
						String singleMultiLineComment[] = {Integer.toString(j),line.substring(j,m2)};
						removedComments.add(singleMultiLineComment);
						notAComment = codeBeforeComment + codeAfterComment;
						A.set(i, notAComment);
						if(m2 < line.length()){
							//There may be another comment following the closing comment delimiter, so:
							i--;
						}
						
						break;
					}else{
						/*multi-line comment that is NOT closed on same line as opening*/
						notAComment = codeBeforeComment + codeAfterComment;
						A.set(i, notAComment);
						int start = i;
						while(i < A.size()){
							i++;
							line = A.get(i);
							if(line.contains("*/")){
								break;
							}
						}
						int n;
						for(n=start+1; n<i; n++){
							String multiLineComment[] = {Integer.toString(n),line.substring(0,line.length())};
							removedComments.add(multiLineComment);
							A.set(n, " ");
						}
						if(i<A.size()){
							line = A.get(i);
							int y=0;
							for(int x=0; x<line.length()-1; x++){
								y=x+1;
								if((line.charAt(x)=='*') && (line.charAt(y)=='/')){
									String linesubstring = line.substring(0, y+1);
									String mlc2[] = {Integer.toString(i),linesubstring};
									removedComments.add(mlc2);
									break;
								}
							}
							y++;
							if(y < line.length()){
								notAComment = line.substring(y);
								A.set(i, notAComment);
								//There may be another comment following the closing comment delimiter, so:
								i--;
							}
							break;
						}
					}
				}
			}
		}
		return removedComments;
	}
	
	
	
	public static char getOpeningChar(char c2){
		if(c2 == '}') return '{';
		else if(c2 == ']') return '[';
		else return '(';
	}
	
	
	public static boolean ifElseCheck(char c1, char c2){
		boolean match;
		if(c2=='}'){
			if(c1 == '{'){
				match = true;	
			}else{
				match = false;
			}
		}else if(c2 == ']'){
			if(c1 == '['){
				match = true;	
			}else{
				match = false;
			}
		}else if(c2 == ')'){
			if(c1 == '('){
				match = true;	
			}else{
				match = false;
			}
		}else return false;
		return match;
	}
	
	
	public static void prnt(ArrayList<ErrorLoggingData> a){
		for(int i=0; i<a.size(); i++){
			ErrorLoggingData el = a.get(i);
			System.out.println(el.str + "line:"+el.line+", pos:"+el.pos+", char:"+el.ch);
		}
	}
	
	
	
}


class ReadFromAFile {
	
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



class ErrorLoggingData {
	public int line; //index of the line of the text file (index begins at 1)
	public int pos; //index of the character in a String of all characters in the line (index begins at 0)
	public char ch; //character
	public String str; //optional variable to store message regarding a detected error
	
	
	public ErrorLoggingData(int line, int pos, char ch){
		this.line = line;
		this.pos = pos;
		this.ch = ch;
		str = ""; //initialized, providing the ability to enter and store an error message at a time after object instantiation
	}
	public ErrorLoggingData(int line, int pos, char ch, String str){
		this.line = line;
		this.pos = pos;
		this.ch = ch;
		this.str = str;
	}
	public ErrorLoggingData(){
		
	}
	
}
