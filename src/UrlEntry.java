public class UrlEntry {

	private String url;

	private long count;

	public UrlEntry(String url, long count) {

		this.url = url;

		this.count = count;

	}

	public String getUrl() {

		return url;

	}

	public long getCount() {

		return count;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (count ^ (count >>> 32));
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UrlEntry other = (UrlEntry) obj;
		if (count != other.count)
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
}