package PickingTopHalf;

import java.util.HashMap;
import java.util.Map;

public class Node implements Comparable {
	int vertexNum;
	HashMap<Integer, Integer> neighborNodes;

	public Node(int pVertexNum) {
		vertexNum = pVertexNum;
		neighborNodes = new HashMap<Integer, Integer>();
	}

	public boolean equals(Object o) {
		Node compareNode = (Node) o;
		return (vertexNum == compareNode.vertexNum);
	}

	public int compareTo(Object o) {
		int compareNeighborSize = ((Node) o).getNeighborsSize();
		return compareNeighborSize - this.getNeighborsSize();
	}

	public int getNeighborsSize() {
		return neighborNodes.size();
	}

	public void addNeighbor(int neighborVertex) {
		this.neighborNodes.put(neighborVertex, neighborVertex);
	}

	public void removeNeighbor(int neighborVertex) {
		this.neighborNodes.remove(neighborVertex);
	}

	public Node clone() {
		Node cloneNode = new Node(this.vertexNum);
		cloneNode.neighborNodes = new HashMap<Integer, Integer>(this.getNeighborsSize());
		for (Map.Entry<Integer, Integer> aHM : this.neighborNodes.entrySet()) {
			cloneNode.addNeighbor(aHM.getKey());
		}
		return cloneNode;
	}

}
