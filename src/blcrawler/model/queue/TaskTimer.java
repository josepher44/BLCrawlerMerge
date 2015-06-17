package blcrawler.model.queue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import blcrawler.commands.Command;
import blcrawler.commands.InitialCommand;
import blcrawler.commands.TimerTest;
import blcrawler.commands.Timestamp;
import blcrawler.model.ConsoleOutput;
import blcrawler.model.GUIModel;

public class TaskTimer
{
    public long millis;
    public long startTime;
    public long endTime;
    public long currentStep;
    public long delay;
    public Thread thread;
    public Command currentCommand;
    public int totalCommands;
    public ArrayList<QueueEntry> queue;
    public boolean nextStepFlag;
    
    private boolean done;
    
    public TaskTimer()
    {
        totalCommands = 0;
        nextStepFlag = false;
        currentCommand = new InitialCommand();
        thread = new Thread()
        {
            public void run()
            {
                while (true)
                {
                    try
                    {
                        sleep(200);
                        currentStep++;
                        executeQueue();
                    }
                    catch (InterruptedException e)
                    {
                        System.out.println("ERR: Timer thread interrupted. Time has stopped. The "
                                + "possibilities are endless. Go swat a bullet out of the air or"
                                + "something. Also fix however the heck you actually got this"
                                + "exception to call, it really shouldn't happen.");
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        startTime = System.currentTimeMillis();
        
        queue = new ArrayList<QueueEntry>();
        
    }
    
    public void addToQueue(Command command)
    {
        if (command.executeImmediately())
        {
            command.execute();
        }
        else if (command.executeNext())
        {
            queue.add(0, new QueueEntry(command));
        }
        else
        {
            queue.add(new QueueEntry(command));
        }
        
    }
    
    public void executeQueue()
    {
        double percentDone;
        if ((currentCommand.equals(null) || currentCommand.isFinished()) && queue.size() != 0)
        {
            if (!nextStepFlag)
            {
                nextStepFlag = true;
                delay = queue.get(0).getCommand().getDelay();
            }
            else
            {
                delay--;
                percentDone = 1.0
                        - ((double) delay / (double) queue.get(0).getCommand().getDelay());
                GUIModel.getGuiView().setTaskBar((int) (percentDone * 100));
            }
            if (delay <= 0)
            {
                currentCommand = queue.get(0).getCommand();
                currentCommand.execute();
                GUIModel.getGuiView().setTaskBar(0);
                queue.remove(0);
                nextStepFlag = false;
                totalCommands++;
                if (totalCommands % 75 == 0)
                {
                    try
                    {
                        GUIModel.getSeleniumModel().relaunchTor();
                    }
                    catch (InterruptedException | IOException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            
        }
        // new ConsoleOutput("Queue","Queue executed");
        
        // Possibly a bad idea, but it's been working wonders on memory allocation
        // System.gc();
    }
    
    public boolean isDone()
    {
        if (System.currentTimeMillis() >= endTime)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public void set(long millis)
    {
        
    }
    
    public long get()
    {
        return millis;
    }
}