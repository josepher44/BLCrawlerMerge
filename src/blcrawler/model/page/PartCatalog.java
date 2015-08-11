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
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import blcrawler.model.ConsoleOutput;
import blcrawler.model.GUIModel;

/**
 * Represents a partCatalog page, showing catalog entries within one of the ~200 top level bricklink
 * categories. Used as an intermediate step to link to part pages
 * 
 * @author Joe Gallagher
 *
 */
public class PartCatalog implements Page
{
    
    // Initialize fields
    private Document pageDoc;   // Jsoup document containing HTML of Partbrowse page
    private String url;         // The url to scrape
    private List<Date> pullTimeStamps;  // List of java dates for each time the page has been pulled
    private String pageHTML;    // Raw string representation of the page's source HTML
    private File folder;        // Directory which part browse pages are saved in
    private String txtRep;      // Concatenated, badly defined data structure representing all page
                                // information in a string format, including url and timestamps
    
    /**
     * Constructor. Constructing this class auto-pulls the data from the live web
     */
    public PartCatalog(String input) throws IllegalArgumentException
    {
        /*
         * Perform separate initialization, dependent on if call comes from a url (called by command
         * to create new partcatalog), or call comes from file (called by page manager to read from
         * database)
         */
        if (input.startsWith("http://"))
        {
            // Run for initial creation of new partcatalog
            
            // Initialization of fileds
            url = input;
            pullTimeStamps = new ArrayList<Date>();
            pullTimeStamps.add(new Date());
            
            // Access the page and record html source
            GUIModel.getSeleniumModel().gotoURL(url);
            pageHTML = GUIModel.getSeleniumModel().getHTML();
            
            // Parse the HTML to a Jsoup document, using bricklink as the root
            pageDoc = Jsoup.parse(pageHTML, "http://www.bricklink.com");
            
            // Concatenate all data together, write to file via page manager
            // TODO: This change is backwards, this should be a local operation
            txtRep = formTxtRep();
            GUIModel.getPageManager().addPartCatalog(this);
            
            // Release temporary memory usage
            memSave();
        }
        else if (input.startsWith("partcatalog_"))
        {
            // Run for reading a partcatalog from the database
            // TODO: Shouldn't leave other fields uninitialized
            
            // Initialize a file reader
            BufferedReader in;
            try
            {
                in = new BufferedReader(new FileReader(
                        "C:/Users/Owner/Documents/BLCrawler/OldDatabase/Pages/PartCatalog"
                                + input));
                
                // Read each line, append to txtrep
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
            
        }
        else    // Only run if call argument has an illegal format
        {
            throw new IllegalArgumentException();
        }
        
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
    
    // TODO: Call should be used in constructor. Repeated code
    @Override
    public void parseFromWeb()
    {
        
        // Use selenium to pull and record raw html
        GUIModel.getSeleniumModel().gotoURL(url);
        pageHTML = GUIModel.getSeleniumModel().getHTML();
        
        // Add timestamp of the pull
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
     * Form the concatenated text representation of all page data, for writing to file
     * 
     * @return the full database representation of the page
     */
    public String formTxtRep()
    {
        String returnText = "[URL]" + url + "\n" + "[/URL]" + "\n";
        returnText = returnText + "[TIMESTAMPS]" + pullTimeStamps.toString() + "\n"
                + "[/TIMESTAMPS]" + System.lineSeparator();
        returnText = returnText + "[HTML]: " + System.lineSeparator() + System.lineSeparator()
                + pageHTML + "\n" + "[/HTML]";
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