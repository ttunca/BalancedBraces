package TestTool;
import java.io.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.filechooser.*;

@SuppressWarnings("serial")
public class FileChooser extends JPanel
implements ActionListener{
	static private final String newline = "\n";
    JButton openButton, runButton, genFileButton;
    JTextArea log;
    JFileChooser fc;
    ReadFile fileObj;
    ArrayList<ErrorLog> content;
    
    public FileChooser() {
    	super(new BorderLayout());
    	fileObj = new ReadFile();
    	// create log
    	log = new JTextArea(5,20);
    	log.setMargin(new Insets(5, 5, 5, 5));
    	log.setEditable(false);
    	JScrollPane logScrollPane = new JScrollPane(log);
    	
    	// create a file chooser
    	fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    	
    	openButton = new JButton("Open File");
    	openButton.addActionListener(this);   	
    	runButton = new JButton("Run Test");
    	runButton.addActionListener(this);
    	genFileButton = new JButton("Gen Report");
    	genFileButton.addActionListener(this);
    	
    	//For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(openButton);
        buttonPanel.add(runButton);
        buttonPanel.add(genFileButton);

        //Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_END);
        add(logScrollPane, BorderLayout.CENTER);
    	
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		// Handle open button
		if(e.getSource() == openButton){
			int ret = fc.showOpenDialog(FileChooser.this);
			if(ret == JFileChooser.APPROVE_OPTION){
				fileObj.file = fc.getSelectedFile();
				log.append("Opening: " + fileObj.file.getName() + "." + newline);
				String type = getExtension(fileObj.file.getName());
				if (type == null) {
				    //System.err.format("'%s' has an" + " unknown filetype.%n", fileObj.file.getName());
				    log.append(fileObj.file.getName() + " has an" + " unknown filetype." + newline);
				} else if (!type.equals("txt")) {
				    //System.err.format("'%s' is not" + " a plain text file.%n", fileObj.file.getName());
				    log.append(fileObj.file.getName() + " is not an" + " .txt file type." + newline);
				}
			}else{
				log.append("Open command cancelled by user." + newline);
			}
			log.setCaretPosition(log.getDocument().getLength());
		}
		BalanceBraces bb = new BalanceBraces();
		String filepath = this.fileObj.file.getName();
		if(e.getSource() == runButton){
			log.append("Start doing balance brace detection method."+newline);			
			bb.A = fileObj.readFile(filepath);
			@SuppressWarnings("unused")
			ArrayList<String[]> removedComments = bb.removeComments(); 				//(main-ds1)
			@SuppressWarnings("unused")
			ArrayList<String> deletedStrings = bb.removeStrings();					//(main-ds2)
			bb.prnt(bb.ELD, log);
			content = bb.ELD;		
			log.setCaretPosition(log.getDocument().getLength());
		}
		if(e.getSource() == genFileButton){
		//testing time in milliseconds of file creation
			long startTime = System.currentTimeMillis();
			
			GenReport report = new GenReport();
			report.configReport( fileObj);
			report.genTextFile(content);
			
			long endTime = System.currentTimeMillis();
			long totalTime = endTime-startTime;
			System.out.println("creating gen report time: " + totalTime + " milliseconds");
		/////////////////////////////////////////////////	
		}
	}
	
	 protected static ImageIcon createImageIcon(String path) {
	        java.net.URL imgURL = FileChooser.class.getResource(path);
	        if (imgURL != null) {
	            return new ImageIcon(imgURL);
	        } else {
	            System.err.println("Couldn't find file: " + path);
	            return null;
	        }
	    }

	 // get file extension
	 private static final int NOT_FOUND = -1;
	 public static final char EXTENSION_SEPARATOR = '.';
	 public static final String EXTENSION_SEPARATOR_STR = Character.toString(EXTENSION_SEPARATOR);
	 private static final char UNIX_SEPARATOR = '/';
	 private static final char WINDOWS_SEPARATOR = '\\';
	 private static final char SYSTEM_SEPARATOR = File.separatorChar;
	 static boolean isSystemWindows() {
	        return SYSTEM_SEPARATOR == WINDOWS_SEPARATOR;
	    }
	 public static String getExtension(final String filename) {
	        if (filename == null) {
	            return null;
	        }
	        final int extensionPos = filename.lastIndexOf(EXTENSION_SEPARATOR);
	        final int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
	        final int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
	        final int lastSeparator = Math.max(lastUnixPos, lastWindowsPos);
	        if(lastSeparator > extensionPos){
	        		return "";
	        }else{
	        	return filename.substring(extensionPos + 1);
	        }
	    }
}
