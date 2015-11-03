package PickingTopHalf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Test {
	public static void main(String[] args) throws IOException {
		HashMap<Integer, Integer> aHM = new HashMap<Integer, Integer>();
		aHM.put(1, 1);
		aHM.put(2, 2);
		aHM.put(3, 3);
		aHM.put(4, 4);
		aHM.put(5, 5);

		Random aRand = new Random();
		int randIndex = Math.abs(aRand.nextInt() % aHM.size());
		int count = 0;
		for (Map.Entry<Integer, Integer> eachHM : aHM.entrySet()) {
			if (count != randIndex) {
				count++;
				continue;
			}
			System.out.println(eachHM.getKey());
			break;
		}
	}
}
