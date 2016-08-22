import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class WordIndex {

	private PersistentArray hashIndex;
	private ListFile wordLists;
	private ListFile urlLists;
	private static int arrayLength = 30;

	public static void initialize(String indexName, long indexSize) {
		PersistentArray.initialize(indexName, (int) indexSize, -1);
		arrayLength = (int) indexSize;
	}

	public static void delete(String indexName) {
		new File(indexName).delete();
	}

	public WordIndex(String indexName) {
		hashIndex = new PersistentArray(indexName);
		wordLists = new ListFile(indexName);
		urlLists = new ListFile(indexName);
	}

	public void close() {
		hashIndex.close();
		wordLists.close();
		urlLists.close();
	}

	public void add(String word, String url) {
		if (wordExists(word)) {
			if (urlExists(word, url)) {
				incrementFoundURLCount(getURLOffset(word, url));
			} else {
				addUrlToList(getWordOffset(word), url);
			}
		} else {
			Long offset = addWordToList(word);
			addUrlToList(offset, url);
		}
	}

	public Iterator<UrlEntry> getUrls(String word) {
		ArrayList<UrlEntry> urlList = new ArrayList<UrlEntry>();
		if (wordExists(word)) {
			Entry urlWord = wordLists.get(getWordOffset(word));
			Long offset = urlWord.getValue();
			while (offset != -1) {
				Entry url = urlLists.get(offset);
				urlList.add(new UrlEntry(url.getString(), url.getValue()));
				offset = url.getLink();
			}
		}
		return urlList.iterator();
	}

	private int getIndex(String word) {
		return Math.abs(word.hashCode()) % arrayLength;
	}

	private Long addWordToList(String word) {
		Long firstWordOffset = hashIndex.get(getIndex(word));
		Entry wordEntry = new Entry(word, -1, firstWordOffset);
		Long newWordOffset = wordLists.newEntry(wordEntry);
		hashIndex.set(getIndex(word), newWordOffset);

		return newWordOffset;
	}

	private void addUrlToList(Long wordOffset, String url) {

		Entry wordEntry = wordLists.get(wordOffset);
		Long firstURLOffset = wordEntry.getValue();
		Entry urlEntry = new Entry(url, 1, firstURLOffset);
		Long newURLOffset = urlLists.newEntry(urlEntry);
		wordEntry.setValue(newURLOffset);
		wordLists.putEntry(wordOffset, wordEntry);
	}

	private boolean wordExists(String word) {
		long entryLink = hashIndex.get(getIndex(word));
		boolean exists = false;
		while (entryLink != -1 && !exists) {
			Entry currentEntry = wordLists.get(entryLink);
			entryLink = currentEntry.getLink();
			if (currentEntry.getString().equals(word)) {
				exists = true;
			}
		}
		return exists;
	}

	private long getWordOffset(String word) {
		long entryLink = hashIndex.get(getIndex(word));
		boolean found = false;

		while (!found && entryLink != -1) {
			Entry current = wordLists.get(entryLink);
			if (current.getString().equals(word)) {
				found = true;
			} else {
				entryLink = current.getLink();
			}
		}
		return entryLink;
	}

	private long getURLOffset(String word, String url) {
		long wordOffset = getWordOffset(word);
		long offset = wordLists.get(wordOffset).getValue();
		boolean found = false;

		while (!found) {
			Entry current = urlLists.get(offset);
			if (current.getString().equals(url)) {
				found = true;
			} else {
				offset = current.getLink();
			}
		}
		return offset;
	}

	private void incrementFoundURLCount(Long urlOffset) {
		Entry urlEntry = urlLists.get(urlOffset);
		urlEntry.setValue(urlEntry.getValue() + 1);
		urlLists.putEntry(urlOffset, urlEntry);
	}

	private boolean urlExists(String word, String url) {
		Iterator<UrlEntry> urls = getUrls(word);
		boolean exists = false;
		while(!exists && urls.hasNext()){
			String currentUrl = urls.next().getUrl();
			if(currentUrl.equals(url)){
				exists = true;
			}
		}
		return exists;
	}
}