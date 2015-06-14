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

public class PartCatalogIndex implements Page
{
    
    // Initialize fields
    private Document pageDoc;   // JSoup document containing the parsed page HTML
    private String url;         // URL which points to the bricklink
    private List<Date> pullTimeStamps;  // List of Java dates cointaining
    private String pageHTML;    // The raw html of the page in string form
    private String txtRep;      // The entire text of the page info stored in the database
    private File folder;        // Directory the data is saved to
    private String filename;    // Full filename of the database location
    private ArrayList<String> linkList; // List of links to individual part browse pages
    
    public PartCatalogIndex(String input)
    {
        // Initialize fields
        url = input;
        linkList = new ArrayList<String>();
        filename = "C:/Users/Owner/Documents/BLCrawler/OldDatabase/Pages/PartCatalogIndex/partcatalogindex.txt";
        pullTimeStamps = new ArrayList<Date>();
        pullTimeStamps.add(new Date());
        
        // Scrape the data from the live web, read raw HTML
        GUIModel.getSeleniumModel().gotoURL(url);
        pageHTML = GUIModel.getSeleniumModel().getHTML();
        pageDoc = Jsoup.parse(pageHTML, "http://www.bricklink.com");
        
        // Compile HTML and other data into a text representation for writing to the database
        txtRep = formTxtRep();
        
        // Update the page manager to reference the latest version of the part browse index, and
        // write it to the database
        GUIModel.getPageManager().updatePartCatalogIndex(this);
        
        // Clear unneeded fields
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
    
    /**
     * Use JSoup to find each part catalog page on the index page, and write it to the link list
     */
    public void listPartCatalogMasterPages()
    {
        
        // Read every line of the file in to the txtrep
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
        
        // Update pageHTML and pagedoc as a subset of txtrep
        pageHTML = txtRep.substring(txtRep.indexOf("Raw HTML:"));
        
        // Create a list of all links in the HTML
        pageDoc = Jsoup.parse(pageHTML, "http://www.bricklink.com");
        Elements links = pageDoc.select("a[href]");
        String buffer = "";
        
        // For each hyperlink, check if it matches the format of a partcatalog.
        // If it does, add the link to the list
        for (int i = 0; i < links.size(); i++)
        {
            
            buffer = links.get(i).attr("abs:href");
            if (buffer.startsWith("http://www.bricklink.com/catalogList.asp?catType=P")
                    && buffer.contains("catString"))
            {
                linkList.add(buffer);
            }
            
        }
        
        // Shuffle the list to randomize pull order
        Collections.shuffle(linkList);
        
        // Create a partbrowse for each link on the part browse index
        for (int i = 0; i < linkList.size(); i++)
        {
            GUIModel.getConsoleController().createPartCatalog(linkList.get(i));
            new ConsoleOutput("Partcatalogindex", "Added URL " + linkList.get(i));
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