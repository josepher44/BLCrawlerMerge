package blcrawler.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.SwingUtilities;

import blcrawler.commands.*;
import blcrawler.commands.Command;
import blcrawler.commands.InvalidCommand;
import blcrawler.commands.TimerTest;
import blcrawler.commands.Timestamp;
import blcrawler.model.ConsoleOutput;
import blcrawler.model.GUIModel;

public class ConsoleController 
{
	private List<String> validBaseCommands;
	private HashMap<String, Runnable> commandLibrary;
	private String commandBuffer;
	
	public ConsoleController() throws Exception
	{
		this.validBaseCommands = new ArrayList<String>();
		this.commandLibrary = new HashMap<String, Runnable>();
		commandLibrary.put("time", () -> {createTimestamp();});
		commandLibrary.put("timertest", () -> {createTimertest();});
		commandLibrary.put("invalid", () -> {createInvalid();});
		commandLibrary.put("addpage", () -> {createAddPage();});
		validBaseCommands.add("GetDate");
		redirectSystemStreams();
		
		
		
	}
	
	public void initializeCommand() 
	{
		GUIModel.getGuiController().sendInToOut(GUIModel.getGuiView().getCLText());
		InterpretText(GUIModel.getGuiView().getCLText());
		GUIModel.getGuiView().clearConsoleIn();
	}
	
	private void redirectSystemStreams() 
	{
		  OutputStream out = new OutputStream() 
		  {
		    @Override
		    public void write(int b) throws IOException 
		    {
		      updateTextAreaFromSystem(String.valueOf((char) b));
		    }
		 
		    @Override
		    public void write(byte[] b, int off, int len) throws IOException 
		    {

		      updateTextAreaFromSystem(new String(b, off, len));
		    }
		 
		    @Override
		    public void write(byte[] b) throws IOException 
		    {
		      write(b, 0, b.length);
		    }
		  };
		 
		  System.setOut(new PrintStream(out, true));
		  System.setErr(new PrintStream(out, true));
		}


	private void updateTextAreaFromSystem(final String text) 
	{
		  SwingUtilities.invokeLater(new Runnable() 
		  {
			  public void run() 
			  {
				  if (!text.contains("\n"))
				  {
					  GUIModel.getGuiView().getConsoleOut().append("System: ");
				  }
		    	
		      GUIModel.getGuiView().getConsoleOut().append(text);
		    }
		  });
	}
	
	public void InterpretText(String textInput) 
	{
		String command;
		int i = textInput.indexOf(' ');
		if (i!=-1)
		{
			command = textInput.substring(0, i);
		}
		else
		{
			command = textInput;
		}
		
		if (commandLibrary.containsKey(command))
		{
			commandBuffer = textInput;
			commandLibrary.get(command).run();
		}
		else
		{
			commandLibrary.get("invalid").run();
		}
	}
	
	
	public void outputConsole(ConsoleOutput output)
	{
		GUIModel.getGuiView().getConsoleOut().append(output.getCombined());
	}
	
	public Method packForHash(String s) throws Exception
	{
		return ConsoleController.class.getMethod(s);
	}
	
	public void createAddPage() 
	{
		GUIModel.getTaskTimer().addToQueue(new AddUrl(commandBuffer.substring(commandBuffer.indexOf(' '))));
	}
	
	public void createTimestamp() 
	{
		GUIModel.getTaskTimer().addToQueue(new Timestamp());
	}
	
	public void createTimertest() 
	{
		TimerTest timertest=new TimerTest();
		timertest.queue();
		GUIModel.getTaskTimer().addToQueue(timertest);
	}
	
	public void createInvalid() 
	{
		new InvalidCommand();
	}

}