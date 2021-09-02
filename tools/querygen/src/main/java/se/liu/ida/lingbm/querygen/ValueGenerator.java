package se.liu.ida.lingbm.querygen;

import java.util.Random;

// This module works for selecting different type of values randomly
public class ValueGenerator {
	private Random ranGen;
	
	public ValueGenerator(long seed)
	{
		ranGen = new Random(seed);
	}
	
	/*
	 * Returns an int value between from and to (inclusive)
	 */
	public int randomInt(int from, int to)
	{
		return ranGen.nextInt(to-from+1) + from;
	}

}

