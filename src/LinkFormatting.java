import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
//hasHost,hasProtocol,and hasBackslash need to be turned back into public methods for testing!!!
import java.util.regex.Pattern;

public class LinkFormatting {

	public Iterator<String> formatLinks(Iterator<String> barebonesURLs,
			URL currentPage) {
		ArrayList<String> formattedLinks = new ArrayList<String>();
		if(barebonesURLs.hasNext())
	  {
			do{
			String currentURL = barebonesURLs.next();
			if (!hasHost(currentURL)) {
				if (hasForwardslash(currentURL)) {
					currentURL = currentPage.getProtocol() + "://"
							+ currentPage.getHost() + currentURL;
				} else {
					currentURL = currentPage.getProtocol() + "://"
							+ currentPage.getHost() + "/" + currentURL;
				}
			} else if (!hasProtocol(currentURL)) {
				currentURL = currentPage.getProtocol() + "://" + currentURL;
			}
			formattedLinks.add(currentURL);
			}while(barebonesURLs.hasNext());
		}
		return formattedLinks.iterator();
	}

	private boolean hasHost(String basicURL) {
		String hostPattern = "(.*)\\.(.*)\\..{3}";
		Pattern p = Pattern.compile(hostPattern);
		Matcher m = p.matcher(basicURL);
		return m.find();
	}

	private boolean hasProtocol(String basicURL) {
		return (basicURL.toLowerCase().startsWith("https") || basicURL
				.toLowerCase().startsWith("http"));
	}

	private boolean hasForwardslash(String basicURL) {
		return (basicURL.startsWith("/"));
	}
}
