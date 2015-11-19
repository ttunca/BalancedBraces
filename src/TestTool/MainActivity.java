package TestTool;
import javax.swing.*;
import java.lang.management.*;

public class MainActivity {
	
	private static final long MEGABYTE = 1024L * 1024L;

	  public static long bytesToMegabytes(long bytes) {
	    return bytes / MEGABYTE;
	  }
	
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		///////////////User Time///////////////
		long startTime = System.nanoTime();
		
//////////////////////////MAIN CODE//////////////////////////
		JFrame frame  = new Gui();	
		frame.show();
//////////////////////////MAIN CODE//////////////////////////
		
		long endTime = System.nanoTime();
		long totalTime = endTime-startTime;
		System.out.println("creating total run time: " + totalTime + " nanoseconds");
		///////////////User Time///////////////
		
		
		
		
//////////////////////////CPU Thread Time Test//////////////////////////
ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
System.out.print("CPU time: ");
System.out.print("" + bean.isCurrentThreadCpuTimeSupported( ) != null ?
bean.getCurrentThreadCpuTime( ) : 0L);
System.out.print(" nanoseconds");
//////////////////////////CPU Thread Time Test//////////////////////////		
		
		
		
		
//////////////////////////memory test//////////////////////////
		 // Get the Java runtime
	    Runtime runtime = Runtime.getRuntime();
	    // Run the garbage collector
	    runtime.gc();
	    // Calculate the used memory
		long memory = runtime.totalMemory() - runtime.freeMemory();
	    System.out.println("Used memory in bytes: " + memory + " bytes");
	    System.out.println("Used memory in megabytes: "
	        + bytesToMegabytes(memory) + " mb");
//////////////////////////memory test//////////////////////////
	    
	    
	}
		
	
}
