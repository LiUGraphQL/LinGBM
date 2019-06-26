package benchmark.testdriver;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import benchmark.generator.DateGenerator;
import benchmark.generator.ValueGenerator;
import benchmark.model.ProductType;

public class ParameterGenerator {
	public static List<Integer> getRandomProductFeatures(ProductType pt, Integer number) {
		ArrayList<Integer> pfs = new ArrayList<Integer>();
		
		ProductType temp = pt;
		while(temp!=null) {
			List<Integer> tempList = temp.getFeatures();
			if(tempList!=null)
				pfs.addAll(tempList);
			temp = temp.getParent();
		}
		
		if(pfs.size() < number) {
			System.err.println(pt.toString() + " doesn't contain " + number + " different Product Features!");
			System.exit(-1);
		}
		return pfs;
	}
	
	/*
	 * Get date string for ConsecutiveMonth
	 */
	public static String getConsecutiveMonth(GregorianCalendar date, int monthNr) {
		GregorianCalendar gClone = (GregorianCalendar)date.clone();
		gClone.add(GregorianCalendar.DAY_OF_MONTH, 28*monthNr);
		return DateGenerator.formatDate(gClone);
	}
	
	/*
	 * Get a random Product Type URI
	 */
	public static ProductType getRandomProductType(ProductType[] productTypes, ValueGenerator valueGen) {
		Integer index = valueGen.randomInt(0, productTypes.length-1);
		
		return productTypes[index];
	}
	
	/*
	 * Get a random date from (dateMin) to (dateMin+days)
	 */
	public static GregorianCalendar getRandomDate(GregorianCalendar dateMin, ValueGenerator valueGen, int days) {
		Integer dayOffset = valueGen.randomInt(0, days);
		GregorianCalendar gClone = (GregorianCalendar)dateMin.clone();
		gClone.add(GregorianCalendar.DAY_OF_MONTH, dayOffset);
		return gClone;
	}
	
	public static int getRandomProductTypeNrFromRange(List<Integer> maxProductTypePerLevel, Integer[] rangeModifier, ValueGenerator valueGen, ValueGenerator valueGen2) {
		int minLevel = 0;
		int maxLevel = maxProductTypePerLevel.size()-1;

		if(rangeModifier!=null) {
			if(rangeModifier[0]>0)
				minLevel = rangeModifier[0];
			else if(rangeModifier[0]<0)
				minLevel = maxLevel + rangeModifier[0] + 1;
			
			if(rangeModifier[1]<0)
				maxLevel = maxLevel + rangeModifier[1];
			else if(rangeModifier[1]>0)
				maxLevel = rangeModifier[1]-1;
		}
		
		if(minLevel > maxProductTypePerLevel.size()-1 || maxLevel < 0 || maxLevel < minLevel || minLevel < 0 || maxLevel > maxProductTypePerLevel.size()-1) {
			System.err.println("Trying to pick a random product type number from illegal level range " + minLevel + " to " + maxLevel);
			System.exit(-1);
		}
		
		if(rangeModifier!=null && rangeModifier[2]==1) {
			int levelToChooseFrom = valueGen2.randomInt(minLevel, maxLevel);
			minLevel = levelToChooseFrom;
			maxLevel = levelToChooseFrom;
		}
			
		int min = getMinProductTypeNrOfLevel(minLevel, maxProductTypePerLevel);
		int max = maxProductTypePerLevel.get(maxLevel);
		
		return valueGen.randomInt(min, max);
	}
	
	private static int getMinProductTypeNrOfLevel(int level, List<Integer> maxProductTypePerLevel) {
		int min = 1;
		if(level>0)
			min = maxProductTypePerLevel.get(level-1) + 1;

		return min;
	}
}
