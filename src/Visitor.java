import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Visitor implements Traversal {
	String hashedIndexFile = "../Index Search/src/Indexed.bin";
	URL currentUrl;
	String pattern = "\\s*(?<Tag1><[^\\/]*>)?(?<Content>[^<]*)?(?<Tag2><\\/.*>)?";
	Pattern wordPattern;
	WordIndex index;

	public Visitor() {
		wordPattern = Pattern.compile(pattern);
		index = new WordIndex(hashedIndexFile);
	}

	@Override
	public void getURL(URL url) {
		currentUrl = url;

	}

	@Override
	public void getLine(String line) {
		if(line.trim().length() > 0){
			String[] words = getWords(line);
			if(words.length > 0)
		addWordsToIndex(words);
		}
	}

	private String[] getWords(String line) {
		Matcher match = wordPattern.matcher(line);
		match.find();
		String text = match.group("Content");
		text = text.replaceAll("[,!.\":()*]", " ").trim();
		text = text.replaceAll("\\]\\[", "] [").trim();
		String[] foundWords = new String[0];
		if(!text.isEmpty())
		foundWords =  text.split("\\s+");
		return foundWords;

	}
	private void addWordsToIndex(String[] words){
		for(String s: words){
			index.add(s, currentUrl.toString());
		}
	}
	public void closeIndex(){
		index.close();
	}
}
