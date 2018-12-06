// Ryan Cave
// 

package graph;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;


public class main {

	public static void main(String[] args) {	
		
		Graph graph = new Graph();
		// density threshold
		Double dt = 0.65;
		
		// initial file processing
		
		try {
			File file = new File("assignment5_input.txt");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				String[] temp = new String[2];
				
				temp = line.split("\t");
							
				graph.addVertex(temp[0]);
				graph.addVertex(temp[1]);
				graph.addEdge(temp[0], temp[1]);				
				
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}		
		
		// prepare to remove vertices and split graph
		
		ArrayList<Graph> graphList = new ArrayList<>();
				
		graphList.add(graph);
		
		// start splitting graphs based on betweenness
		
			// at the end, all graphs should be a final cluster
		ArrayList<Graph> finalClusters = new ArrayList<>();		
		
		while (!graphList.isEmpty()){
						
			// if a graph on the list is too small, remove it
			// if a graph on the list meets cluster criteria, remove it and add to final clusters
			
			System.out.print("Graph List Size: " + graphList.size() + " | ");
			
			for (Graph g : graphList){
				System.out.print(g.numVertices() + " | ");
			}
			
			for (Iterator<Graph> it = graphList.iterator(); it.hasNext(); ){
				Graph g = it.next();
				if (g.numVertices() <= 2){
					System.out.println("Removed: " + g.toString() + " with density " + g.getDensity());
					it.remove();
				}
				
				if (g.numVertices() > 2 && g.getDensity() >= dt){
					finalClusters.add(g);
					System.out.println("Added: " + g.toString() + " with density " + g.getDensity());
					it.remove();
				}
								
				
			}
			
			// for remaining graphs, recalculate scores and split them
			
			if (graphList.isEmpty())
				break;
			
			Graph cur = graphList.get(graphList.size() - 1);
			graphList.remove(graphList.size() - 1);
			
			Map<Pair, Double> edgeScores = new HashMap<>();
			cur.getEdgeList().forEach(v-> edgeScores.put(new Pair(v.first, v.second), 0.0));
			cur.getVertices().forEach(v -> compute(v, cur, edgeScores));					
			edgeScores.forEach((v, score) -> edgeScores.put(v, score/2));			
						
			Pair tEdge = findEdgeRemoval(edgeScores);
			System.out.println("Removing " + tEdge.toString());
			cur.removeEdge(tEdge.first, tEdge.second);
			
			graphList.add(cur);
				
			splitGraph(graphList);
			
		}
		
		// print results		
		
		finalClusters.sort(Comparator.comparing(Graph::numVertices));
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("assignment5_answers.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		for (int i = finalClusters.size() - 1; i >= 0; i--){
			System.out.println(finalClusters.get(i).toString());
			writer.println(finalClusters.get(i).toString());
		}
		writer.close();
		
	}
	

	public static void compute (String s, Graph graph, Map<Pair, Double> edgeScores){
		
		ArrayDeque<String> stack = new ArrayDeque<>();
		Map<String, ArrayList<String>> pred = new HashMap<>();
		graph.getVertices().forEach(w -> pred.put(w, new ArrayList<>()));
		
		// num shortest paths from our vertex to v
		
		Map<String, Double> sigma = new HashMap<>();
		graph.getVertices().forEach(t -> sigma.put(t, 0.0));
		sigma.put(s, 1.0);
		
		// distance of path from s to v
		
		Map<String, Double> distance = new HashMap<>();
		graph.getVertices().forEach(t-> distance.put(t, -1.0));
		distance.put(s, 0.0);
		
		Queue<String> queue = new ArrayDeque<>();
		queue.add(s);
		
		//
		
		while (!queue.isEmpty()){
			String v = queue.remove();
			stack.push(v);
			
			for (String w : graph.getNeighbors(v)){
				
				// unweighted graph so we add 1, all edge weights are 1
				double d = distance.get(v) + 1;
				
				if (distance.get(w) < 0){
					queue.add(w);
					distance.put(w, d);
				}
				
				if (distance.get(w) == d){
					sigma.put(w, sigma.get(w) + sigma.get(v));
					pred.get(w).add(v);
				}
				
			}
			
		}
		
		Map<String, Double> dependency = new HashMap<>();
		graph.getVertices().forEach(v -> dependency.put(v, 0.0));
				
		while (!stack.isEmpty()){
			String w = stack.pop();
			for (String v : pred.get(w)){
				Double c = (sigma.get(v) / sigma.get(w)) * (1 + (dependency.get(w)));
				Pair temp = new Pair(v, w);				
				
				edgeScores.put(temp, edgeScores.get(temp) + c);
				dependency.put(v, dependency.get(v) + c);
			}
		}
		
	}
	
	
	static Pair findEdgeRemoval(Map<Pair, Double> scores){
		
		Double num = Double.NEGATIVE_INFINITY;
		Pair ret = new Pair();
		
		for (Pair x : scores.keySet()){
			if (scores.get(x) > num){
				num = scores.get(x);
				ret = x;
			}
		}
		return ret;
	}

	public static void splitGraph(ArrayList<Graph> graphList){
		
		Graph cur = graphList.remove(0);
		HashMap<String, Boolean> visited = new HashMap<>();
		
		for (String x : cur.getVertices()){
			visited.put(x, false);
		}
		
		// bfs
		
		while (visited.containsValue(false)){
			Graph newGraph = new Graph();			
			String startVertex = null;
			ArrayList<String> queue = new ArrayList<>();
			
			for (String x : visited.keySet()){
				if (visited.get(x).equals(false)){
					startVertex = x;
					break;
				}
			}
			
			visited.put(startVertex, true);
			queue.add(startVertex);
			
			while (!queue.isEmpty()){
				String temp = queue.remove(0);
				newGraph.addVertex(temp);
				for (String x : cur.getNeighbors(temp)){
					newGraph.addVertex(x);
					newGraph.addEdge(temp, x);
					if (visited.get(x).equals(false)){
						queue.add(x);
						visited.put(x, true);
						
					}
				}
			}
			
			graphList.add(newGraph);			
		}
		
	
		
	}
	
}
