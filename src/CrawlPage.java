import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class CrawlPage implements Runnable {
	URL url;
	LinkFinder finder;
	Visitor visit;
	InputStream stream;
	WebTraversal traversal;

	public CrawlPage(URL url, LinkFinder finder, WebTraversal traversal) {
		this.url = url;
		this.finder = finder;
		this.visit = finder.visit;
		this.traversal = traversal;
	}

	@Override
	public void run() {
		visit.getURL(url);
		finder.processPage(createInputStream(url));
		traversal.decrementCounter();
	}

	private InputStream createInputStream(URL Url) {
		try {
			stream = Url.openStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stream;
	}

}
