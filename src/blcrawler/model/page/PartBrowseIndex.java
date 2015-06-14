package blcrawler.model.page;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import blcrawler.model.ConsoleOutput;
import blcrawler.model.GUIModel;

public class PartBrowseIndex implements Page
{
    
    private Document pageDoc;
    private String url;
    private List<Date> pullTimeStamps;
    private String pageHTML;
    private String txtRep;
    private File folder;
    private String filename;
    private ArrayList<String> linkList;
    
    public PartBrowseIndex(String input)
    {
        url = input;
        linkList = new ArrayList<String>();
        filename = "C:/Users/Owner/Documents/BLCrawler/OldDatabase/Pages/PartBrowseIndex/partbrowseindex.txt";
        pullTimeStamps = new ArrayList<Date>();
        pullTimeStamps.add(new Date());
        GUIModel.getSeleniumModel().gotoURL(url);
        pageHTML = GUIModel.getSeleniumModel().getHTML();
        pageDoc = Jsoup.parse(pageHTML, "http://www.bricklink.com");
        txtRep = formTxtRep();
        GUIModel.getPageManager().updatePartBrowseIndex(this);
        memSave();
    }
    
    /**
     * @return the txtRep
     */
    public String getTxtRep()
    {
        return txtRep;
    }
    
    /**
     * @param txtRep
     *            the txtRep to set
     */
    public void setTxtRep(String txtRep)
    {
        this.txtRep = txtRep;
    }
    
    public void listPartBrowseMasterPages()
    {
        
        BufferedReader in;
        
        try
        {
            in = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = in.readLine()) != null)
            {
                txtRep = txtRep + line + System.lineSeparator();
            }
            in.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        pageHTML = txtRep.substring(txtRep.indexOf("Raw HTML:"));
        pageDoc = Jsoup.parse(pageHTML, "http://www.bricklink.com");
        Elements links = pageDoc.select("a[href]");
        String buffer = "";
        for (int i = 0; i < links.size(); i++)
        {
            
            buffer = links.get(i).attr("abs:href");
            if (buffer.startsWith("http://www.bricklink.com/browseList.asp?itemType=P")
                    && buffer.contains("catString"))
            {
                linkList.add(buffer);
            }
            
        }
        
        Collections.shuffle(linkList);
        
        for (int i = 0; i < linkList.size(); i++)
        {
            GUIModel.getConsoleController().createPartBrowse(linkList.get(i));
            new ConsoleOutput("Partbrowseindex", "Added URL " + linkList.get(i));
        }
    }
    
    @Override
    public void parseFromWeb()
    {
        
        GUIModel.getSeleniumModel().gotoURL(url);
        pageHTML = GUIModel.getSeleniumModel().getHTML();
        
        pullTimeStamps.add(new Date());
        
    }
    
    /**
     * @return the url
     */
    public String getUrl()
    {
        return url;
    }
    
    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url)
    {
        this.url = url;
    }
    
    @Override
    public Date getLastPullTimestamp()
    {
        // TODO Auto-generated method stub
        return pullTimeStamps.get(pullTimeStamps.size() - 1);
    }
    
    /**
     * Combines fields into a single string representation for saving to .txt format
     */
    public String formTxtRep()
    {
        String returnText = "URL: " + url + "\n" + "\n" + pullTimeStamps.toString();
        returnText = returnText + "\n" + "\n" + "Raw HTML: " + "\n" + "\n" + pageHTML;
        returnText = returnText.replaceAll("\n", System.lineSeparator());
        return returnText;
    }
    
    /**
     * Reduces the memory saved in the object by nullifying fields which store raw HTML when not
     * used Call at the conclusion of any method which calls getHTML or getTxtRep
     */
    public void memSave()
    {
        txtRep = "";
        pageHTML = "";
    }
    
}