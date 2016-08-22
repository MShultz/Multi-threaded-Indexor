import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class PersistentArray implements Array {
	private final static int LONG_SIZE = 8;
	RandomAccessFile raFile;
	RandomAccessFile initialFile;

	public PersistentArray(String arrayFileName) {
		try {
			raFile = new RandomAccessFile(new File(arrayFileName), "rw");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void initialize(String arrayFileName, int arraySize,
			long initialValue) {
		RandomAccessFile initialFile = null;
		try {
			File file = new File(arrayFileName);
			file.createNewFile();
			initialFile = new RandomAccessFile(file, "rw");

			for (int i = 0; i < arraySize; ++i) {
				initialFile.seek(i * LONG_SIZE);
				initialFile.writeLong(initialValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try {
				initialFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void set(int index, long value) {
		movePointer(index);
		try {
			raFile.writeLong(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public long get(int index) {
		Long gottenValue = null;
		movePointer(index);
		try {
			gottenValue = raFile.readLong();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return gottenValue;
	}

	public long getLength() {
		Long length = null;
		try {
			length = raFile.length() / LONG_SIZE;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return length;
	}

	private void movePointer(int index) {
		try {
			raFile.seek(index * LONG_SIZE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			raFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void delete(String arrayFileName) {
		 (new File(arrayFileName)).delete();
	}
}
