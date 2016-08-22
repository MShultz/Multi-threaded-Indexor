import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class LinkFinder {
	ArrayList<String> linkList = new ArrayList<String>();
	Visitor visit;
	InputStream in;
	final static String pattern = "<\\s*[Aa]\\s+[Hh][Rr][Ee][Ff]\\s*=\\s*\"(?<Group1>[^\"]+)\"\\s*>*.*";
	final static Pattern pat = Pattern.compile(pattern);

	public LinkFinder(Visitor visit) {
		this.visit = visit;

	}

	public void processPage(InputStream in) {

		for (String line : getLines(in).toArray(String[]::new)) {
			visit.getLine(line);
		}

	}

	public Stream<String> getLines(InputStream in) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		

		Predicate<String> p = (s) -> {
			if (pat.matcher(s).find()) {
				Matcher m = pat.matcher(s);
				m.find();
				linkList.add(m.group("Group1"));
			}
			return true;
		};
		return reader.lines().filter(p);
	}

	public Stream<String> getLinks() {
		return linkList.stream();
	}
}
