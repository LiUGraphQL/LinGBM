package queryGenerator;

import java.security.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Date;

// This module works for ramdomly selecting different type of values
// It is used for 'valueSelector'
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
	
	/*
	 * Returns an random date between from and to
	 */
	
	public String getRandomDate(String start, String end) throws ParseException {
		String date;
		String year, month, day;
		String random = "1900-01-01";
		String connect = "-";
		SimpleDateFormat objSDF = new SimpleDateFormat("yyyy-mm-dd");
		Date startDate = objSDF.parse(start);
		Date endDate = objSDF.parse(end);
		Date randomDate = objSDF.parse(random);
		
		while(randomDate.compareTo(startDate) < 0 || randomDate.compareTo(endDate) > 0){
			year = String.valueOf(randomInt(2000, 2006));
			int monthTem = randomInt(1, 12);
			if(monthTem<10)
				month = "0"+String.valueOf(monthTem);
			else
				month = String.valueOf(monthTem);			
			int dayTem = randomInt(1, 30);
			if(dayTem<10)
				day = "0"+String.valueOf(dayTem);
			else
				day = String.valueOf(dayTem);
			random = year+connect+month+connect+day;
			if(random != "2004-01-14")
				randomDate = objSDF.parse(random);
		}
		date = random;
	    return date;
	}

}

