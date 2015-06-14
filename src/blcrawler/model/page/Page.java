package blcrawler.model.page;

import java.util.Date;

public interface Page
{
    /**
     * Parse the page from the live web, adding a new pull timestamp
     */
    void parseFromWeb();
    
    /**
     * Get the timestamp of the last pull operation from the live web
     * 
     * @return The last time the part was pulled, as a java date
     */
    Date getLastPullTimestamp();
    
}