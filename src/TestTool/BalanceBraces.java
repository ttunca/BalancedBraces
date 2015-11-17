package TestTool;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import javax.print.attribute.standard.PresentationDirection;
import javax.swing.JTextArea;


public class BalanceBraces {
	public static ArrayList<String> A; 								// (ds1)
	public static Stack<ErrorLog> S; 									// (ds2)
	public static ArrayList<ErrorLog> resolvErr;					// (ds3)
	public static ArrayList<ErrorLog> ELD;			// (ds4)
	
	public BalanceBraces(){
		A = new ArrayList<String>();
		S = new Stack<ErrorLog>(); 
		resolvErr = new ArrayList<ErrorLog>();
		ELD = new ArrayList<ErrorLog>();
	}
	
	public void balanceBraces(){
		ErrorLog elogdat = new ErrorLog();
		ErrorLog elogdatPeek = new ErrorLog();
		
		for(int i=0; i< A.size(); i++){
			String line = A.get(i);
			
			for(int j=0; j<line.length();j++){
				char c = line.charAt(j);
				if((c=='{') || (c=='[') || (c=='(')){
					
					elogdat = new ErrorLog(i,j,c);
					S.push(elogdat);
					elogdat = null;
				}else if((c=='}') || (c==']') || (c==')')){
					//simple
					if (S.isEmpty()) {
						System.out.println("stack is empty now, so the test result is false");
						return;
					}
					
					elogdat = new ErrorLog(i,j,c);
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
		
		// Result
		System.out.println("\n\nUnbalanced Braces, Brackets, and Parentheses:");
		elogdat.prnt(ELD);
	}
	
	public static void prnt(ArrayList<ErrorLog> a,JTextArea j){
		j.append("Unbalanced Braces, Brackets, and Parentheses:" + "\n");
		for(int i=0; i<a.size(); i++){
			ErrorLog el = a.get(i);
			j.append(el.str + "line:"+el.line+", pos:"+el.pos+", char:"+el.ch + "\n");
		}
	}
	
	/*Perform this before balanceBraces()*/
	public ArrayList<String> removeStrings(){
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
	
	
	public  ArrayList<String[]> removeComments(){
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
	
	
	
	public  char getOpeningChar(char c2){
		if(c2 == '}') return '{';
		else if(c2 == ']') return '[';
		else return '(';
	}
	
	
	public  boolean ifElseCheck(char c1, char c2){
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
	
}
