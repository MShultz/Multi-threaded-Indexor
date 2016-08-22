
public class Entry {
	 private String string;
     private long value;
     private long link;

     public Entry(String string, long value, long link){
    	 this.string = string;
    	 this.value = value;
    	 this.link = link;
     }

     public String getString(){
    	 return string;
     }

     public long getValue(){
    	 return value;
     }

     public void setValue(long value){
    	 this.value = value;
     }

     public long getLink(){
    	 return link;
     }

     public void setLink(long link){
    	 this.link = link;
     }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (link ^ (link >>> 32));
		result = prime * result + ((string == null) ? 0 : string.hashCode());
		result = prime * result + (int) (value ^ (value >>> 32));
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
		Entry other = (Entry) obj;
		if (link != other.link)
			return false;
		if (string == null) {
			if (other.string != null)
				return false;
		} else if (!string.equals(other.string))
			return false;
		if (value != other.value)
			return false;
		return true;
	}
     
}
