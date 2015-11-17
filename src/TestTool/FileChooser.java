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
    JButton openButton, runButton;
    JTextArea log;
    JFileChooser fc;
    ReadFile fileObj;
    
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
    	//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    	
    	openButton = new JButton("Open File");
    	openButton.addActionListener(this);   	
    	runButton = new JButton("Run Test");
    	runButton.addActionListener(this);
    	
    	//For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(openButton);
        buttonPanel.add(runButton);

        //Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
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
				// detect file type
				//if()
			}else{
				log.append("Open command cancelled by user." + newline);
			}
			log.setCaretPosition(log.getDocument().getLength());
		}
		if(e.getSource() == runButton){
			log.append("Start doing balance brace detection method."+newline);
			BalanceBraces bb = new BalanceBraces();
			bb.A = fileObj.readFile(this.fileObj.file.getName());
			@SuppressWarnings("unused")
			ArrayList<String[]> removedComments = bb.removeComments(); 			//(main-ds1)
			
			@SuppressWarnings("unused")
			ArrayList<String> deletedStrings = bb.removeStrings();					//(main-ds2)
			bb.balanceBraces();
			bb.prnt(bb.ELD, log);
			
			log.setCaretPosition(log.getDocument().getLength());
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
}
