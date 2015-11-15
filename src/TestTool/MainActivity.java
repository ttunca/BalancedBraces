package TestTool;
import java.util.ArrayList;

public class MainActivity {
	
	public static void main(String[] args) {
		ReadFile fileread = new ReadFile();
		BalanceBraces balanceFunc = new BalanceBraces();
		balanceFunc.A = fileread.readFile("data.txt");
		balanceFunc.A.add(0, "");
		
		@SuppressWarnings("unused")
		ArrayList<String[]> removedComments = balanceFunc.removeComments(); 			//(main-ds1)
		//**TO SEE THE COMMENTS LOCATED IN THE TEXT FILE, UNCOMMENT THE 4 LINES BELOW:
//		for(int i=0; i<removedComments.size(); i++){
//			String[] s = removedComments.get(i);
//			System.out.println("Line "+s[0]+": "+s[1]);
//		}
		
		
		@SuppressWarnings("unused")
		ArrayList<String> deletedStrings = balanceFunc.removeStrings();					//(main-ds2)
		//**TO SEE THE STRINGS LOCATED IN THE TEXT FILE, UNCOMMENT THE 4 LINES BELOW:
//		System.out.println("\n\nDeleted Strings:");
//		for(int i=0; i<deletedStrings.size(); i++){
//			System.out.println(deletedStrings.get(i));
//		}+
		
		balanceFunc.balanceBraces();

		//**TO SEE ALL PROPERLY BALANCED BRACES, UNCOMMENT THE 2 LINES BELOW:
//		System.out.println("\n\nResolved:");
//		prnt(resolvErr);
		
		
	}
	
}
