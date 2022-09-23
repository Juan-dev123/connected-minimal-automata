package model;

import java.util.ArrayList;

public class State {
	//Attributes
	private String name;
	private ArrayList<Edge> edges;
	
	public State(String name) {
		this.name = name;
	}
	
	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}
	
	public String getName () {
		return name;
	}
	
	public void addEdge(Edge edge) {
		edges.add(edge);
	}
	
	public ArrayList<Edge> getEdges() {
		return edges;
	}
}

