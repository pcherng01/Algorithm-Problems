package Revision;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AlienMaze {

	public static void main(String[] args) throws IOException {
		AlienMaze aMaze = new AlienMaze(45);
		aMaze.runProgram();
	}

	int totalCell;
	byte dimension;
	HashMap<String, Coordinate> aHM = new HashMap<String, Coordinate>();

	public AlienMaze(int n) {
		dimension = (byte) n;
		totalCell = (n * n) * (n * n);
	}

	public void runProgram() throws IOException {
		int cellCount = 0;
		// Make every cell a set
		Coordinate randomCoord;
		// Make every Coordinate a set, starting from 0,0,0,0 --> n-1,n-1,n-1,n-1
		for (int t = 0; t < dimension; t++) {
			for (int z = 0; z < dimension; z++) {
				for (int y = 0; y < dimension; y++) {
					for (int x = 0; x < dimension; x++) {
						int numCoord = (t * 1000000) + (z * 10000) + (y * 100)
								+ x;
						randomCoord = new Coordinate(numCoord);
						randomCoord.makeSet();
						aHM.put(randomCoord.getString(), randomCoord);
						//System.out.println(cellCount);
						//String gridTehWall = String.format("%8s",
						//		Integer.toBinaryString(randomCoord.wallGrid & 0xFF));
						//System.out.println(randomCoord.getString() + " " + gridTehWall);
						cellCount++;
					}
				}
			}
		}
		System.out.println((cellCount == totalCell) ? "Yeah" : "Nah");

		Coordinate currentCoord;
		String lookUpKey;
		int byteWallToKnock1, byteWallToKnock2;
		byte aRandNum;

		Random randNum = new Random();

		for (Map.Entry<String, Coordinate> ans : aHM.entrySet()) {
			// For each one, check all four corners
			currentCoord = ans.getValue();
			boolean xChecked = false, yChecked = false, zChecked = false, tChecked = false, wallKnocked = false;
			while ((!wallKnocked)
					&& (xChecked == false && yChecked == false
							&& zChecked == false && tChecked == false)) {
				aRandNum = (byte) (randNum.nextInt(10000) % 4);
				switch (aRandNum) {
				case 0:// Checking the negative and positive X Axis
					for (int i = -1; i < 2; i = i + 2) {
						// Check if cell is in the world
						lookUpKey = currentCoord.tAxis + ","
								+ currentCoord.zAxis + "," + currentCoord.yAxis
								+ "," + ((currentCoord.xAxis) + i);

						//System.out.println(currentCoord.getString()
						//		+ " looking horizontal " + lookUpKey);

						if (aHM.containsKey(lookUpKey)) {
							byteWallToKnock1 = (i == -1) ? -2 : -1;
							byteWallToKnock2 = (byteWallToKnock1 == -2) ? -1
									: -2;
							wallKnocked = union(currentCoord,
									aHM.get(lookUpKey), byteWallToKnock1,
									byteWallToKnock2);
							//	System.out.println("Unioning: " + currentCoord.getString()
							//			+ ", " + aHM.get(lookUpKey).getString());
						}
					}
					xChecked = true;
					break;
				case 1:
					// Checking for the negative and positive Y Axis
					for (int i = -1; i < 2; i += 2) {
						lookUpKey = currentCoord.tAxis + ","
								+ currentCoord.zAxis + ","
								+ (currentCoord.yAxis + i) + ","
								+ currentCoord.xAxis;

						//	System.out.println(currentCoord.getString()
						//			+ " looking vertically " + lookUpKey);

						if (aHM.containsKey(lookUpKey)) {
							//System.out.println("Unioning: " + currentCoord.getString()
							//			+ ", " + aHM.get(lookUpKey).getString());
							byteWallToKnock1 = (i == -1) ? -8 : -4;
							byteWallToKnock2 = (byteWallToKnock1 == -8) ? -4
									: -8;
							wallKnocked = union(currentCoord,
									aHM.get(lookUpKey), byteWallToKnock1,
									byteWallToKnock2);
						}
					}
					yChecked = true;
					break;
				case 2:
					for (int i = -1; i < 2; i += 2) {
						lookUpKey = currentCoord.tAxis + ","
								+ (currentCoord.zAxis + i) + ","
								+ currentCoord.yAxis + "," + currentCoord.xAxis;
						//System.out.println(currentCoord.getString()
						//		+ " looking up/down " + lookUpKey);

						if (aHM.containsKey(lookUpKey)) {
							//System.out.println("Unioning: " + currentCoord.getString()
							//		+ ", " + aHM.get(lookUpKey).getString());
							byteWallToKnock1 = (i == -1) ? -32 : -16;
							byteWallToKnock2 = (byteWallToKnock1 == -32) ? -16
									: -32;
							wallKnocked = union(currentCoord,
									aHM.get(lookUpKey), byteWallToKnock1,
									byteWallToKnock2);
						}
					}
					zChecked = true;
					break;
				case 3:
					for (int i = -1; i < 2; i += 2) {
						lookUpKey = (currentCoord.tAxis + i) + ","
								+ currentCoord.zAxis + "," + currentCoord.yAxis
								+ "," + currentCoord.xAxis;
						//System.out.println(currentCoord.getString()
						//		+ " looking time " + lookUpKey);

						if (aHM.containsKey(lookUpKey)) {
							//	System.out.println("Unioning: " + currentCoord.getString()
							//			+ ", " + aHM.get(lookUpKey).getString());
							byteWallToKnock1 = (i == -1) ? -128 : -64;
							byteWallToKnock2 = (byteWallToKnock1 == -128) ? -64
									: -128;
							wallKnocked = union(currentCoord,
									aHM.get(lookUpKey), byteWallToKnock1,
									byteWallToKnock2);
						}
					}
					tChecked = true;
					break;
				}
			}
		}

		/*
		for (Map.Entry<String, Coordinate> ans : aHM.entrySet()) {
			String s1 = String.format("%8s",
					Integer.toBinaryString(ans.getValue().wallGrid & 0xFF)
							.replace(' ', '0'));
			//System.out.println(ans.getValue().getString() + ", " + s1);
		}*/

		System.out.println("\n\n");
		byte[] ans = new byte[totalCell];
		int ansIndex = 0;
		String s1;

		for (int t = 0; t < dimension; t++) {
			for (int z = 0; z < dimension; z++) {
				for (int y = 0; y < dimension; y++) {
					for (int x = 0; x < dimension; x++) {
						/*
						s1 = String.format(
								"%8s",
								Integer.toBinaryString(
										aHM.get("" + t + "," + z + "," + y
												+ "," + x).wallGrid & 0xFF)
										.replace(' ', '0'));
						System.out.println("" + t + "," + z + "," + y + "," + x
								+ ", " + s1);*/
						ans[ansIndex] = aHM.get("" + t + "," + z + "," + y
								+ "," + x).wallGrid;
						ansIndex++;
					}
				}
			}
		}

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream("428Lab2Donez45");
			fos.write(ans);
			fos.flush();
			System.out.println("done");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

	public boolean union(Coordinate coord1, Coordinate coord2,
			int numWallKnock1, int numWall2) {
		Coordinate parent1 = findSet(coord1);
		Coordinate parent2 = findSet(coord2);

		if (parent1.getString().equals(parent2.getString())) {
			return false;
		}

		if (parent1.rank >= parent2.rank) {
			parent1.rank = (parent1.rank == parent2.rank) ? parent1.rank + 1
					: parent1.rank;
			parent2.parent = parent1;
			coord1.wallGrid += numWallKnock1;
			coord2.wallGrid += numWall2;
			return true;
			/*
			System.out.println("Wall Knocked Down");
			String s1 = String.format(
					"%8s",
					Integer.toBinaryString(coord1.wallGrid & 0xFF).replace(' ',
							'0'));
			String s2 = String.format(
					"%8s",
					Integer.toBinaryString(coord2.wallGrid & 0xFF).replace(' ',
							'0'));
			System.out.println(coord1.getString() + ", " + s1);
			System.out.println(coord2.getString() + ", " + s2);*/
		} else {
			parent1.parent = parent2;
			coord1.wallGrid += numWallKnock1;
			coord2.wallGrid += numWall2;
			return true;
			/*
			System.out.println("Wall Knocked Down");
			String s1 = String.format(
					"%8s",
					Integer.toBinaryString(coord1.wallGrid & 0xFF).replace(' ',
							'0'));
			String s2 = String.format(
					"%8s",
					Integer.toBinaryString(coord2.wallGrid & 0xFF).replace(' ',
							'0'));
			System.out.println(coord1.getString() + ", " + s1);
			System.out.println(coord2.getString() + ", " + s2);*/
		}
	}

	public Coordinate findSet(Coordinate pCoordinate) {
		Coordinate parent = pCoordinate.parent;
		if (parent == pCoordinate) {
			return parent;
		}
		pCoordinate.parent = findSet(pCoordinate.parent);
		return pCoordinate.parent;
	}

	class Coordinate {
		byte xAxis;
		byte yAxis;
		byte zAxis;
		byte tAxis;
		int rank;
		byte wallGrid;
		Coordinate parent;

		/**
		 * Generate a Coordinate with random x and y axis within the dimension
		 * 
		 * @param pDimension
		 *            - if n = 2, then x and y can only go up to 0,1
		 */
		public Coordinate(int anInt) {
			xAxis = (byte) (anInt % 100);
			yAxis = (byte) ((anInt % 10000) / 100);
			zAxis = (byte) ((anInt % 1000000) / 10000);
			tAxis = (byte) (anInt / 1000000);
			rank = 0;
			wallGrid = (byte) (-1);
		}

		public void makeSet() {
			parent = this;
		}

		public String getString() {
			return ("" + tAxis + "," + zAxis + "," + yAxis + "," + xAxis);
		}
	}
}
