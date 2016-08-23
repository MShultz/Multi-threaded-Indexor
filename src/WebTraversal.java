import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class WebTraversal {
	public int threadCount = 0;
	private int maxVisits;
	private URL currentPageURL;
	private ArrayList<String> visitedPages = new ArrayList<String>();
	private ArrayList<String> foundPages = new ArrayList<String>();

	public WebTraversal(String startingLink, int maxVisits) {
		this.maxVisits = maxVisits;
		foundPages.add(startingLink);
	}

	public void traverseWeb() {
		Visitor visit = new Visitor();
		LinkFinder finder = new LinkFinder(visit);
		do {	
			String currentPage = foundPages.get(visitedPages.size());
			try {
				currentPageURL = new URL(currentPage);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			Thread thread = new Thread(new CrawlPage(currentPageURL, finder, this), "cp");
			threadCount++;
			addToHasVisited(currentPage);
			thread.start();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (visitedPages.size() < maxVisits && !visitedEqualsFound());
		waitForState();
		visit.closeIndex();
	}

	public synchronized void decrementCounter(String currentPage){
		threadCount--;	
		notifyAll();	
	}
	private synchronized void waitForState(){
		while(threadCount != 0){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void addToFoundList(Iterator<String> foundLinks) {
		if (foundLinks.hasNext()) {
			do {
				String currentFoundLink = foundLinks.next();
				if (!hasFound(currentFoundLink)) {
					foundPages.add(currentFoundLink);
				}
			} while (foundLinks.hasNext());
		}
	}

	private boolean visitedEqualsFound() {
		boolean equals = true;
		if (visitedPages.size() == foundPages.size()) {
			for (int i = 0; i < visitedPages.size() && equals; ++i) {
				if (!visitedPages.get(i).equals(foundPages.get(i))) {
					equals = false;
				}
			}
		} else {
			equals = false;
		}
		return equals;
	}



	private boolean hasFound(String URL) {
		boolean hasFound = false;
		if (foundPages.contains(URL) || foundPages.contains(URL.substring(0, URL.length() - 1)))
			hasFound = true;
		return hasFound;
	}

	private void addToHasVisited(String URL) {
		visitedPages.add(URL);
	}

}
