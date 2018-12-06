package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Graph {
		
	final HashMap<String, Set<String>> adjacencyList;
	ArrayList<Pair> edgeList = new ArrayList<>();
	
	public Graph(){
		adjacencyList = new HashMap<>();
	}
	
	public void addVertex(String x){
		if (adjacencyList.containsKey(x)){
			return;
		}
		
		adjacencyList.put(x, new HashSet<String>());
		
	}
	
	public void removeVertex(String x){
		if (!adjacencyList.containsKey(x)){
			return;
		}
		
		adjacencyList.remove(x);
		
		for (String y : adjacencyList.keySet()){
			adjacencyList.get(y).remove(x);
		}
		
	}
	
	public boolean isAdjacent(String x, String y){
		return adjacencyList.get(x).contains(y);
	}
	
	public Iterable<String> getNeighbors(String x){
		return adjacencyList.get(x);
	}
	
	public Iterable<String> getVertices(){
		return adjacencyList.keySet();
	}
	
	public void addEdge(String x, String y){
		
		if (adjacencyList.get(x).contains(y)){
			return;
		}
		
		
		adjacencyList.get(x).add(y);
		adjacencyList.get(y).add(x);
		Pair temp = new Pair(x, y);
		if (!edgeList.contains(temp)){
			edgeList.add(temp);
		}
	}
	
	public void removeEdge(String x, String y){
		
		this.adjacencyList.get(x).remove(y);
		this.adjacencyList.get(y).remove(x);
		
		Pair temp = new Pair(x, y);
		edgeList.remove(temp);
		
	}
	
	public int numVertices(){
		return adjacencyList.size();
	}
	
	public double getDensity(){		
		return (2.0 * edgeList.size()) / (adjacencyList.size() * (adjacencyList.size() - 1));			
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		String ret = numVertices() + ": { ";
		
		for (String x : getVertices()){
			ret += x + " ";
		}
		
		ret += "}";
		
		return ret;
	}
	
	public ArrayList<Pair> getEdgeList(){
		return edgeList;
	}
	
	public int numEdges(){
		return edgeList.size();
	}

}
