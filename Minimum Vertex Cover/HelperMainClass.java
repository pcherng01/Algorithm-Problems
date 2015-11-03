package PickingTopHalf;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class HelperMainClass {

	HashMap<Integer, Node> allNodes;
	HashMap<Integer, HashMap<Integer, Integer>> numNeighborHM;
	Set<Integer> minVertexList = new HashSet<Integer>();
	int maxNumOfNeighbors = 0;
	static PrintWriter writer;

	static ArrayList<Node> allAvailableVertexNodeList = new ArrayList();
	static ArrayList<Integer> allAvailableVertexNumberList = new ArrayList();

	static HashMap<Integer, Node> copyAllNodes = new HashMap();
	static HashMap<Integer, HashMap<Integer, Integer>> copyNumNeighborHM = new HashMap();
	static Set<Integer> copyMinVertexList = new HashSet<Integer>();

	public static void main(String[] args) throws FileNotFoundException {
		writer = new PrintWriter("nodes.txt");
		HelperMainClass aMainClass = new HelperMainClass();
		aMainClass.getInputAndSetUpGraph();
		aMainClass.removeAllLeaves();
		for (Map.Entry<Integer, Node> eachNode : aMainClass.allNodes.entrySet()) {
			allAvailableVertexNodeList.add(eachNode.getValue());
		}

		Collections.sort(allAvailableVertexNodeList);
		for (Node eachNode : allAvailableVertexNodeList) {
			allAvailableVertexNumberList.add(eachNode.vertexNum);
		}

		int minVertexCover = Integer.MAX_VALUE;
		// Copying all variables 
		Node tempNode;
		for (Map.Entry<Integer, Node> eachNode : aMainClass.allNodes.entrySet()) {
			tempNode = eachNode.getValue().clone();
			copyAllNodes.put(eachNode.getKey(), tempNode);
		}
		HashMap<Integer, Integer> tempHM;
		for (Map.Entry<Integer, HashMap<Integer, Integer>> aHM : aMainClass.numNeighborHM.entrySet()) {
			HashMap<Integer, Integer> currentHM = aHM.getValue();
			tempHM = new HashMap();
			for (Map.Entry<Integer, Integer> innerCurrentHM : currentHM.entrySet()) {
				tempHM.put(innerCurrentHM.getKey(), innerCurrentHM.getValue());
			}
			copyNumNeighborHM.put(aHM.getKey(), tempHM);
		}
		for (Integer currentInt : aMainClass.minVertexList) {
			copyMinVertexList.add(currentInt);
		}

		for (Node eachNode : allAvailableVertexNodeList) {
			writer.printf("Node %d has %d neighbors.\n", eachNode.vertexNum, eachNode.getNeighborsSize());
		}

		Random aRandom = new Random();

		//int count = 0;
		//for (Integer eachVertexNum : allAvailableVertexNumberList) {
		//count++;
		for (int i = 0; i < 5000000; i++) {

			int availableSize = allAvailableVertexNumberList.size();
			int randomTopHalfIndex = Math.abs(aRandom.nextInt() % (availableSize / 2));
			//aMainClass.putGuardOnVertex(eachVertexNum);
			aMainClass.putGuardOnVertex(allAvailableVertexNumberList.get(randomTopHalfIndex));
			while (aMainClass.allNodes.size() > 0) {
				//				System.out.println("Total uncovered: " + aMainClass.allNodes.size());
				//				System.out.println("Size of numNeighborHM: " + aMainClass.numNeighborHM.size());
				//				System.out.println("All available numNeighborHM: ");
				//				for(Map.Entry<Integer, HashMap<Integer, Integer>> aHM : aMainClass.numNeighborHM.entrySet()){
				//					if(aHM.getValue().size() > 0){
				//						System.out.print(aHM.getKey() + " ");
				//					}
				//				}
				//				System.out.println();

				aMainClass.removeAllLeaves();
				aMainClass.putGuardOnVertex();
			}

			System.out.println("Current Minimum Vertex Cover so far: " + minVertexCover);
			if (aMainClass.minVertexList.size() < minVertexCover) {
				writer = new PrintWriter("nodes.txt");
				minVertexCover = aMainClass.minVertexList.size();
				System.out.println("Minimum Vertex Cover so far: " + minVertexCover);
				//System.out.println("With count :" + count);
				for (Integer eachVertex : aMainClass.minVertexList) {
					writer.print(eachVertex + "x");
				}
				writer.close();
			}

			//			for(Integer eachVertex : aMainClass.minVertexList){
			//				writer.print(eachVertex + "x");
			//			}

			// recopy
			aMainClass.allNodes = new HashMap<Integer, Node>();
			for (Map.Entry<Integer, Node> eachNode : copyAllNodes.entrySet()) {
				tempNode = eachNode.getValue().clone();
				aMainClass.allNodes.put(eachNode.getKey(), tempNode);
			}
			aMainClass.numNeighborHM = new HashMap<Integer, HashMap<Integer, Integer>>();
			for (Map.Entry<Integer, HashMap<Integer, Integer>> aHM : copyNumNeighborHM.entrySet()) {
				HashMap<Integer, Integer> currentHM = aHM.getValue();
				tempHM = new HashMap();
				for (Map.Entry<Integer, Integer> innerCurrentHM : currentHM.entrySet()) {
					tempHM.put(innerCurrentHM.getKey(), innerCurrentHM.getValue());
				}
				aMainClass.numNeighborHM.put(aHM.getKey(), tempHM);
			}
			aMainClass.minVertexList = new HashSet<Integer>();
			for (Integer currentInt : copyMinVertexList) {
				aMainClass.minVertexList.add(currentInt);
			}

			//}
		}
		writer.close();
		System.out.println("Done");
	}

	public void run() {
		getInputAndSetUpGraph();
		runAlgorithm();
		System.out.println("Minimum vertex cover: " + minVertexList.size());
		//		for (int i = 0; i < minVertexList.size(); i++) {
		//			System.out.print(minVertexList.get(i) + " ");
		//		}
		//		System.out.println();
		System.out.println("done");
	}

	/**
	 * Read input from file and set up graph by putting all nodes with its vertexNumber in allNodes.
	 */
	public void getInputAndSetUpGraph() {
		int vertexNum, neighborVertexNum, vertexSize;
		Scanner tempSc;
		Node newVertex;
		String fileName = "graph.txt";
		allNodes = new HashMap<Integer, Node>();
		numNeighborHM = new HashMap<Integer, HashMap<Integer, Integer>>();
		try {
			FileReader fileReader = new FileReader(fileName);
			Scanner sc = new Scanner(fileReader);
			String tempStr = "";
			sc.useDelimiter("x");

			while (sc.hasNext() && (tempStr = sc.next()) != null) {
				tempSc = new Scanner(tempStr);
				tempSc.useDelimiter(":");

				vertexNum = Integer.parseInt(tempSc.next());
				newVertex = new Node(vertexNum);
				allNodes.put(vertexNum, newVertex);

				tempStr = tempSc.next();
				tempSc = new Scanner(tempStr);
				tempSc.useDelimiter(",");
				while (tempSc.hasNext()) {
					neighborVertexNum = Integer.parseInt(tempSc.next());
					newVertex.addNeighbor(neighborVertexNum);
				}
				vertexSize = newVertex.getNeighborsSize();
				maxNumOfNeighbors = (vertexSize > maxNumOfNeighbors) ? vertexSize : maxNumOfNeighbors;
				addNodeToNumNeighborHM(vertexSize, vertexNum);
			}
		} catch (FileNotFoundException ex) {

		}
	}

	public void removeAllLeaves() {
		HashMap<Integer, Integer> leaves = numNeighborHM.get(1);
		Node leafNode, parentOfLeaf, neighborOfParentNode;
		int vertexNumOfLeaf, parentVertexNum, neighborOfParentVertexNum;
		int sizeOfNeighborOfParent;
		while (leaves.size() > 0) {
			// For each of the leaf
			for (Map.Entry<Integer, Integer> leaf : leaves.entrySet()) {
				vertexNumOfLeaf = leaf.getValue();
				leafNode = allNodes.get(vertexNumOfLeaf);

				// for each of the neighbor of leaf
				for (Map.Entry<Integer, Integer> neighborOfLeaf : leafNode.neighborNodes.entrySet()) {
					parentVertexNum = neighborOfLeaf.getKey();
					parentOfLeaf = allNodes.get(parentVertexNum);

					// For each of the neighbor of the leaf's parent
					for (Map.Entry<Integer, Integer> neighborOfParent : parentOfLeaf.neighborNodes.entrySet()) {
						neighborOfParentVertexNum = neighborOfParent.getKey();
						neighborOfParentNode = allNodes.get(neighborOfParentVertexNum);
						sizeOfNeighborOfParent = neighborOfParentNode.getNeighborsSize();

						removeItemFromNumNeighborHM(sizeOfNeighborOfParent, neighborOfParentVertexNum);
						neighborOfParentNode.removeNeighbor(parentVertexNum);

						if (neighborOfParentNode.getNeighborsSize() == 0) {
							allNodes.remove(neighborOfParentVertexNum);
						} else {
							addNodeToNumNeighborHM(neighborOfParentNode.getNeighborsSize(), neighborOfParentVertexNum);

						}
					}
					removeItemFromNumNeighborHM(parentOfLeaf.getNeighborsSize(), parentVertexNum);
					minVertexList.add(parentVertexNum);
					allNodes.remove(parentVertexNum);
				}
				break;
			}
		}
	}

	public void putGuardOnVertex() {
		Node neighborOfVertexNode, currentVertexNode;
		int sizeOfNeighborOfVertex, currentVertexNum;
		//int maxNumOfNeighbors = getMaxNumOfNeighborFromNumNeighborHM();
		int randomNumOfNeighbors = getRandomNumOfNeighborFromNumNeighborHM();

		Random aRandom = new Random();

		// Get the list of highest-number-of-neighbors vertices, run only once
		HashMap<Integer, Integer> highestVertices = numNeighborHM.get(randomNumOfNeighbors);

		if (highestVertices != null && highestVertices.size() > 0) {
			// Get only one vertex from that list

			int randomIndex = Math.abs((aRandom.nextInt() % highestVertices.size()));
			int count = 0;

			for (Map.Entry<Integer, Integer> aVertex : highestVertices.entrySet()) {
				if (count != randomIndex) {
					count++;
					continue;
				}
				// For each of the neighbor of this vertex
				currentVertexNode = allNodes.get(aVertex.getKey());
				currentVertexNum = currentVertexNode.vertexNum;
				for (Map.Entry<Integer, Integer> neighborOfVertex : currentVertexNode.neighborNodes.entrySet()) {
					int neighborOfVertexNum = neighborOfVertex.getKey();
					neighborOfVertexNode = allNodes.get(neighborOfVertexNum);
					sizeOfNeighborOfVertex = neighborOfVertexNode.getNeighborsSize();

					removeItemFromNumNeighborHM(sizeOfNeighborOfVertex, neighborOfVertexNum);
					neighborOfVertexNode.removeNeighbor(aVertex.getKey());

					if (neighborOfVertexNode.getNeighborsSize() == 0) {
						allNodes.remove(neighborOfVertexNum);
					} else {
						addNodeToNumNeighborHM(neighborOfVertexNode.getNeighborsSize(), neighborOfVertexNum);
					}
				}
				removeItemFromNumNeighborHM(currentVertexNode.getNeighborsSize(), currentVertexNum);
				minVertexList.add(currentVertexNum);
				allNodes.remove(currentVertexNum);
				break;
			}
		}
	}

	public void putGuardOnVertex(int pVertexNum) {
		Node neighborOfVertexNode, currentVertexNode;
		int sizeOfNeighborOfVertex, currentVertexNum;

		// For each of the neighbor of this vertex
		currentVertexNode = allNodes.get(pVertexNum);
		currentVertexNum = currentVertexNode.vertexNum;
		for (Map.Entry<Integer, Integer> neighborOfVertex : currentVertexNode.neighborNodes.entrySet()) {
			int neighborOfVertexNum = neighborOfVertex.getKey();
			neighborOfVertexNode = allNodes.get(neighborOfVertexNum);
			sizeOfNeighborOfVertex = neighborOfVertexNode.getNeighborsSize();

			removeItemFromNumNeighborHM(sizeOfNeighborOfVertex, neighborOfVertexNum);
			neighborOfVertexNode.removeNeighbor(pVertexNum);

			if (neighborOfVertexNode.getNeighborsSize() == 0) {
				allNodes.remove(neighborOfVertexNum);
			} else {
				addNodeToNumNeighborHM(neighborOfVertexNode.getNeighborsSize(), neighborOfVertexNum);
			}
		}
		removeItemFromNumNeighborHM(currentVertexNode.getNeighborsSize(), currentVertexNum);
		minVertexList.add(currentVertexNum);
		allNodes.remove(currentVertexNum);
	}

	public void runAlgorithm() {
		while (allNodes.size() > 0) {
			//System.out.println("Uncovered vertices: " + allNodes.size());
			removeAllLeaves();
			putGuardOnVertex();
		}
	}

	public int getMaxNumOfNeighborFromNumNeighborHM() {
		int maxNumOfNeighbors = 0;
		for (Map.Entry<Integer, HashMap<Integer, Integer>> aHM : numNeighborHM.entrySet()) {
			if (aHM.getValue().size() > 0) {
				maxNumOfNeighbors = (aHM.getKey() > maxNumOfNeighbors) ? aHM.getKey() : maxNumOfNeighbors;
			}
		}
		return maxNumOfNeighbors;
	}

	public int getRandomNumOfNeighborFromNumNeighborHM() {
		ArrayList<Integer> numOfNeighbor = new ArrayList<Integer>();
		for (Map.Entry<Integer, HashMap<Integer, Integer>> aHM : numNeighborHM.entrySet()) {
			if (aHM.getValue().size() > 0) {
				numOfNeighbor.add(aHM.getKey());
			}
		}
		Collections.sort(numOfNeighbor);
		Collections.reverse(numOfNeighbor);
		Random aRand = new Random();

		int topHalf = (numOfNeighbor.size() / 5);
		if (topHalf == 0) {
			if (numOfNeighbor.size() == 0) {
				return 0;
			}
			return numOfNeighbor.get(0);
		} else {
			int randomIndex = Math.abs(aRand.nextInt() % 5);
			return numOfNeighbor.get(randomIndex);
		}
	}

	public void removeItemFromNumNeighborHM(int neighborSize, int vertexNum) {
		HashMap<Integer, Integer> someHM;
		someHM = numNeighborHM.get(neighborSize);
		if (someHM != null) {
			someHM.remove(vertexNum);
		}
	}

	/**
	 * Add node to the numNeighborHM HashMap
	 * 
	 * @param neighborSize
	 *            - the size of neighbor of the VertexNode
	 * @param vertexNum
	 *            - the vertex number of the node
	 */
	public void addNodeToNumNeighborHM(int neighborSize, int vertexNum) {
		HashMap<Integer, Integer> someHM;
		someHM = (numNeighborHM.containsKey(neighborSize)) ? numNeighborHM.get(neighborSize)
				: new HashMap<Integer, Integer>();
		someHM.put(vertexNum, vertexNum);
		numNeighborHM.put(neighborSize, someHM);
	}

	public void printNumNeighborHM() {
		for (Map.Entry<Integer, HashMap<Integer, Integer>> aHM : numNeighborHM.entrySet()) {
			System.out.printf("Node with number of neighbors of %d: ", aHM.getKey());
			for (Map.Entry<Integer, Integer> innerHM : aHM.getValue().entrySet()) {
				System.out.print(innerHM.getKey() + " ");
			}
			System.out.println();
		}
	}

	/**
	 * Print all the nodes with their neighbors in allNodes HashMap
	 */
	public void printAllNodes() {
		for (Map.Entry<Integer, Node> eachEle : allNodes.entrySet()) {
			System.out.printf("Node %d with neighbors: ", eachEle.getKey());
			for (Map.Entry<Integer, Integer> eachNeighbor : eachEle.getValue().neighborNodes.entrySet()) {
				System.out.printf("%d ", eachNeighbor.getKey());
			}
			System.out.println();
		}
	}

}
