package blcrawler.view;



import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import net.miginfocom.swing.MigLayout;

import blcrawler.model.GUIModel;
import blcrawler.controller.GUIMainController;


public class GUIView {
	
	private static GUIMainController guiMainController;
	private static GUIModel guiModel;
	
	private JFrame mainFrame;
	private JPanel mainPanel;
	private JLabel headerLabel;
	private JLabel statusLabel;
	private JPanel controlPanel;
	private JTextArea consoleOut;
	private JTextArea commandLine;
	private JScrollPane scrollableConsole;
	private static final String key = "ENTER";
    private KeyStroke keyStroke;
	

	public GUIView(GUIMainController guiMainController, GUIModel guiModel) 
	{	
		this.guiMainController = guiMainController;
		this.guiModel = guiModel;
		
		
		
		mainFrame = new JFrame("Bricklink");
	      mainFrame.setSize(800,600);
	      
	      
	      mainPanel = new JPanel();
	      mainPanel.setLayout(new MigLayout());
	      
	      //Center on the screen
	      Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	      int x = (int) ((dimension.getWidth() - mainFrame.getWidth()) / 2);
	      int y = (int) ((dimension.getHeight() - mainFrame.getHeight()) / 2);
	      mainFrame.setLocation(x, y);
	      
		  consoleOut=new JTextArea(25, 45);
		  consoleOut.setEditable(true);
		  consoleOut.setLineWrap(true);
		  JScrollPane scrollableConsole = new JScrollPane(consoleOut);
		  
		  
				  
		commandLine = new JTextArea(5,45);
		keyStroke = KeyStroke.getKeyStroke(key);
		  commandLine.setEditable(true);
		  commandLine.setLineWrap(true);
		  commandLine.getInputMap(JComponent.WHEN_FOCUSED).put(keyStroke,"Thing");
		  commandLine.getActionMap().put("Thing", this.guiMainController.getThing());

	      headerLabel = new JLabel(guiModel.getHeaderLabel(),JLabel.CENTER );
	      statusLabel = new JLabel(guiModel.getStatusLabel(),JLabel.CENTER);        


	      mainFrame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
		        System.exit(0);
	         }        
	      });    

	      mainFrame.add(mainPanel);
	      
	      mainPanel.add(headerLabel, "wrap 40");
	      mainPanel.add(scrollableConsole, "span 6");
	      mainPanel.add(statusLabel, "wrap");
	      mainPanel.add(commandLine, "span 6 6");
	      
	      mainFrame.setVisible(true);
	      
	      

	}

}