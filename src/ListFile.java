import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.stream.Stream;

public class ListFile {
	RandomAccessFile raFile;
	long currentOffset;

	public ListFile(String listFileName) {
		try {
			raFile = new RandomAccessFile(listFileName, "rw");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void initialize(String listFileName) {
		File file = new File(listFileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public long newEntry(Entry entry) {
		long offset = 0;
		try {
			offset = raFile.length();
			raFile.seek(raFile.length());
		} catch (IOException e) {
			e.printStackTrace();
		}
		writeEntry(entry);
		return  offset;
	}



	public Entry get(long offset) {
		Entry thisEntry = null;
		try {
			raFile.seek(offset);
			int stringSize = raFile.readInt();
			byte[] string = new byte[stringSize];
			for (int i = 0; i < stringSize; ++i) {
				string[i] = raFile.readByte();
			}
			thisEntry = new Entry(new String(string), raFile.readLong(), raFile.readLong());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return thisEntry;
	}

	public void putEntry(long offset, Entry entry) {
		try {
			raFile.seek(offset);
		} catch (IOException e) {
			e.printStackTrace();
		}
		writeEntry(entry);
	}

	public void close() {
		try {
			raFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void delete(String listFileName) {
		new File(listFileName).delete();
	}

	private void writeEntry(Entry entry) {
		try {
			raFile.writeInt(entry.getString().length());
			raFile.writeBytes(entry.getString());
			raFile.writeLong(entry.getValue());
			raFile.writeLong(entry.getLink());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Stream<Entry> getEntries(long offset){
		ArrayList<Entry> entry = new ArrayList<Entry>();
		while(offset != -1){
			Entry e = get(offset);
			entry.add(e);
			offset = e.getLink();
		}
		return entry.stream();
	}
}
