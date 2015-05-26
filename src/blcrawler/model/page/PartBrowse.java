package blcrawler.model.page;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PartBrowse implements Page 
{
	
	Document page;
	String url;
	
	public PartBrowse() 
	{
		this.url = "";
	}

	@Override
	public void parseFromWeb() 
	{
		try
		{
			page = Jsoup.connect(url).get();
			page.toString();
		
		} catch (IOException e) 
		{
			System.out.println("Jsoup ran out of the little letters");
			e.printStackTrace();
		}

	}
	
}