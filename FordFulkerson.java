import java.util.*;
import java.io.File;

public class FordFulkerson {
	
	public static ArrayList<Integer> pathDFS(Integer source, Integer destination, WGraph graph){
		ArrayList<Integer> visited = new ArrayList<Integer>();
		return pathDFSHelper(source, destination, visited, graph);
	}
	
	
	// if sink is not in visited, return empty array
	
	// helper method for pathDFS with recursion
	private static ArrayList<Integer> pathDFSHelper(Integer source, Integer destination, ArrayList<Integer> visited, WGraph graph) {
		
		visited.add(source);
		ArrayList<Integer> path = new ArrayList<>();
		path.add(source);
		
		if (source == destination) {
			return path;
		}

		for (Integer vertice : findAdjVertices(source, graph)) {
			if (!visited.contains(vertice) && graph.getEdge(source, vertice).weight > 0) {
				visited.add(vertice);
				List<Integer> traceback = pathDFSHelper(vertice, destination, visited, graph);
				if (traceback.contains(destination)) {
					for(Integer integer : traceback) {
						path.add(integer);
					}
					return path;
				}
			}
		}
		path.clear();
		return path;
	}
	
	// another helper for finding adjacent edges
	private static ArrayList<Integer> findAdjVertices(Integer vertice, WGraph graph) {
		ArrayList<Integer> adj = new ArrayList<Integer>();
		ArrayList<Edge> edges = graph.getEdges();
		for (Edge edge : edges) {
			if (edge.nodes[0] == vertice) {
				adj.add(edge.nodes[1]);
			}
		}
		return adj;
	}
	// another helper
	private static boolean allVisited(ArrayList<Edge> list, ArrayList<Integer> visited) {
		for (Edge edge: list) {
			if (!visited.contains(edge.nodes[1])) {
				return false;
			}
		}
		return true;
	}
	

	public static String fordfulkerson(WGraph graph){
		String answer="";
		int maxFlow = 0;
		
		//WGraph resGraph = new WGraph(graph);
		ArrayList<Integer> path = new ArrayList<Integer>();
		Integer source = graph.getSource();
		Integer destination = graph.getDestination();
		Integer bottleneck;
		WGraph curFlow = new WGraph(graph);
		WGraph finalRes = new WGraph(graph);
		
		while(!pathDFS(source, destination, graph).isEmpty()) {
			bottleneck = Integer.MAX_VALUE;
			path = pathDFS(source, destination, graph);
			for (int i=0; i < path.size()-1; i++) {
				bottleneck = Math.min(bottleneck, graph.getEdge(path.get(i),path.get(i+1)).weight);				
			}
			
			
			WGraph resGraph = new WGraph(graph);
			for (int i=0; i<path.size()-1; i++) {
				resGraph.setEdge(path.get(i), path.get(i+1), graph.getEdge(path.get(i),path.get(i+1)).weight - bottleneck);
				if (resGraph.getEdge(path.get(i+1), path.get(i))==null){
					Edge newEdge = new Edge(path.get(i+1), path.get(i), 0);
					resGraph.addEdge(newEdge);
				}
				resGraph.setEdge(path.get(i+1), path.get(i), resGraph.getEdge(path.get(i+1),path.get(i)).weight + bottleneck);
			}
			maxFlow += bottleneck;
			graph = resGraph;
			finalRes = resGraph;
		}
		
		for (Edge edge : curFlow.getEdges()) {
			for (Edge edge2 : finalRes.getEdges()) {
				if (edge.nodes[0] == edge2.nodes[0] &&
						edge.nodes[1] == edge2.nodes[1]) {
					edge.weight -= edge2.weight;
				}
			}
		}
		
		answer += maxFlow + "\n" + curFlow.toString();	
		return answer;
	}
	

	 public static void main(String[] args){
		 String file = args[0];
		 File f = new File(file);
		 WGraph g = new WGraph(file);
	         System.out.println(fordfulkerson(g));
	 }
}

