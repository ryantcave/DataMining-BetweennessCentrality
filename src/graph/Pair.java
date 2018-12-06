package graph;

public class Pair {
	String first;
	String second;
	/**
	 * @param first
	 * @param second
	 */
	public Pair(String first, String second) {
		this.first = first;
		this.second = second;
	}
	
	public Pair(){
		
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * (result + first.hashCode() * second.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		Pair other = (Pair) obj;
		
		if (this == obj)
	        return true;

	    if (obj == null)
	        return false;

	    if (getClass() != obj.getClass())
	        return false;
	    
	    if (first.equals(other.first) && second.equals(other.second))
	        return true;

	    if (first.equals(other.second) && second.equals(other.first))
	        return true;

	    return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Edge [" + first + " " + second + "]";
	}
}
