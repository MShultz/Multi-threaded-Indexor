public interface Array {
	public void set(int index, long value);
	public long get(int index);
	public long getLength();
	public void close();
	public static void initialize(String arrayFileName, int arraySize,
			long initialValue) {}

	public static void delete(String arrayFileName) {}
}
