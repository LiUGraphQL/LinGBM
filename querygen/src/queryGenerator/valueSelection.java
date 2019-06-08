package queryGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Arrays;
import java.util.Iterator;
import java.lang.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.GregorianCalendar;
import java.util.Date;

// This module works for generating a group of values for one parameter or a combination of parameters.
// Given a placeholder, randomly select a group of values for it
public class valueSelection{

	
	protected ValueGenerator valueGen;
	protected HashMap<String,Integer> wordHash;
	protected String[] wordList;
	protected GregorianCalendar currentDate;
	protected Integer[] producerOfProduct;
	protected Integer[] vendorOfOffer;
	protected Integer[] ratingsiteOfReview;
	protected Integer productCount;
	protected Integer reviewCount;
	protected Integer offerCount;
	protected Integer vendorCount;
	protected Integer producerCount;
	protected Integer instanceNm;
	protected Integer maxInstanceNm;
	
	protected Integer scalefactor; 
	

	
	public Integer getScalefactor() {
		return scalefactor;
	}
	
	@SuppressWarnings("rawtypes")
	public Set getValues(String placeholder, int max){
		Set values = null;
		switch(placeholder){
		case "$productID":
			values =  getSelectedProductSet(max);
			break;
		case "$producerID":
			values = getSelectedProducerSet(max);
			break;
		case "$reviewID":
			values = getSelectedReviewSet(max);
			break;
		case "$offerID":
			values = getSelectedOfferSet(max);
			break;
		case "$vendorID":
			values = getSelectedVendorSet(max);
			break;
		case "$keyword":
			values = selectedWord(max);
			break;
		}
		return values;
	}
	public String[][] getCombines(String placeholder, int max) throws ParseException{
		String[][] combinevalues=null;
		switch(placeholder){
		case "$vendorID-$offset":
			combinevalues = getRandomCombinationOfParasQ7(max);
			break;
		case "$cnt-$attrOffer1-$attrOffer2":
			combinevalues = getRandomCombinationOfParasQ8(max);
			break;
		case "$vendorID-$attrReview":
			combinevalues = getRandomCombinationOfParasQ9(max);
			break;
		case "$producerID-$vendorID":
			combinevalues = getRandomCombinationOfParasQ12(max);
			break;
		case "$producerID-$date":
			combinevalues = getRandomCombinationOfParasQ13(max);
			break;
		case "$producerID-$date-$keyword":
			combinevalues = getRandomCombinationOfParasQ14(max);
			break;
			
		}
		return combinevalues;
	}
	
	protected void init(File resourceDir, long seed) {
		Random seedGen = new Random(seed);
		valueGen = new ValueGenerator(seedGen.nextLong());
		//Product-Producer Relationships from resourceDir/pp.dat
		File pp = readProductProducerData(resourceDir);	
		//Offer-Vendor Relationships from resourceDir/vo.dat
		readOfferAndVendorData(resourceDir, pp);
		//Review-Rating Site Relationships from resourceDir/rr.dat
		readReviewSiteData(resourceDir);
		//Current date and words of Product labels from resourceDir/cdlw.dat
		readDateAndLabelWords(resourceDir);
	}
	
	private void readDateAndLabelWords(File resourceDir) {
		File cdlw = new File(resourceDir, "cdlw.dat");
		ObjectInputStream currentDateAndLabelWordsInput;
		try {
			currentDateAndLabelWordsInput = new ObjectInputStream(new FileInputStream(cdlw));
			productCount = currentDateAndLabelWordsInput.readInt();
			reviewCount = currentDateAndLabelWordsInput.readInt();
			offerCount = currentDateAndLabelWordsInput.readInt();
			currentDate = (GregorianCalendar) currentDateAndLabelWordsInput.readObject();
			@SuppressWarnings("unchecked")
			HashMap<String, Integer> x = (HashMap<String, Integer>)currentDateAndLabelWordsInput.readObject();
			wordHash = x ;
			wordList = wordHash.keySet().toArray(new String[0]);
		} catch(IOException | ClassNotFoundException e) {
			System.err.println("Could not open or process file " + cdlw.getAbsolutePath());
			System.err.println(e.getMessage());
			System.exit(-1);
		}
	}

	private void readReviewSiteData(File resourceDir) {
		File rr = new File(resourceDir, "rr.dat");
		ObjectInputStream reviewRatingsiteInput;
		try {
			reviewRatingsiteInput = new ObjectInputStream(new FileInputStream(rr));
			ratingsiteOfReview = (Integer[]) reviewRatingsiteInput.readObject();
		} catch(IOException e) {
			System.err.println("Could not open or process file " + rr.getAbsolutePath());
			System.err.println(e.getMessage());
			System.exit(-1);
		}
		catch(ClassNotFoundException e) { System.err.println(e); }
	}

	private void readOfferAndVendorData(File resourceDir, File pp) {
		File vo = new File(resourceDir, "vo.dat");
		ObjectInputStream offerVendorInput;
		try {
			offerVendorInput = new ObjectInputStream(new FileInputStream(vo));
			vendorOfOffer = (Integer[]) offerVendorInput.readObject();
			//Add vendorCount
			vendorCount =  vendorOfOffer.length;
		} catch(IOException e) {
			System.err.println("Could not open or process file " + pp.getAbsolutePath());
			System.err.println(e.getMessage());
			System.exit(-1);
		} catch(ClassNotFoundException e) { System.err.println(e); }
	}

	private File readProductProducerData(File resourceDir) {
		File pp = new File(resourceDir, "pp.dat");
		ObjectInputStream productProducerInput;
		try {
			productProducerInput = new ObjectInputStream(new FileInputStream(pp));
			producerOfProduct = (Integer[]) productProducerInput.readObject();
			//Add ProducerCount
			producerCount =  producerOfProduct.length;
			scalefactor = producerOfProduct[producerOfProduct.length-1];
		} catch(IOException e) {
			System.err.println("Could not open or process file " + pp.getAbsolutePath());
			System.err.println(e.getMessage());
			System.exit(-1);
		}
		catch(ClassNotFoundException e) { System.err.println(e); }
		return pp;
	}

	/**
     * Format the date string DBMS dependent
     * @param date The object to transform into a string representation
     * @return formatted String
     */
	
	/*
	 * 1. Returns the random productNr
	 */
	protected Integer getRandomProductNr() {
		Integer productNr = valueGen.randomInt(1, productCount);	
		return productNr;
	}
	/*
	 * Return a set of productNr (without duplicated)
	 */
	protected Set getSelectedProductSet(Integer maxInstanceNm) {
		Set setProduct = new HashSet();
		int size = 0;
		Integer oneProduct = null;
		boolean Empty = true;
		instanceNm = Math.min(productCount, maxInstanceNm);	
		while(size < instanceNm){
			oneProduct = getRandomProductNr();
			setProduct.add(oneProduct);
			size = setProduct.size();
		}
		Empty = setProduct.isEmpty();
		if(Empty)
			System.out.println("The product set is empty");
		return setProduct;
	}
	/*
	 * 2. Returns the random reviewNr
	 */
	protected Integer getRandomReviewNr() {
		Integer reviewNr = valueGen.randomInt(1, reviewCount);	
		return reviewNr;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Set getSelectedReviewSet(Integer maxInstanceNm) {
		Set setReview = new HashSet();
		int size = 0;
		Integer oneReview = null;
		boolean Empty = true;
		instanceNm = Math.min(reviewCount, maxInstanceNm);
		while(size < instanceNm){
			oneReview = getRandomReviewNr();
			setReview.add(oneReview);
			size = setReview.size();
		}
		Empty = setReview.isEmpty();
		if(Empty)
			System.out.println("The review set is empty");
		return setReview;
	}
	/*
	 * 3. Returns the random offerNr
	 */
	protected Integer getRandomOfferNr() {
		Integer offerNr = valueGen.randomInt(1, offerCount);
		return offerNr;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Set getSelectedOfferSet(Integer maxInstanceNm) {
		Set setOffer = new HashSet();
		int size = 0;
		Integer oneOffer = null;
		boolean Empty = true;
		instanceNm = Math.min(offerCount, maxInstanceNm);
		while(size < instanceNm){
			oneOffer = getRandomOfferNr();
			setOffer.add(oneOffer);
			size = setOffer.size();
		}
		Empty = setOffer.isEmpty();
		if(Empty)
			System.out.println("The offer set is empty");
		return setOffer;
	}
	
	
	/*
	 * 4. Returns a random producerNr
	 */
	protected Integer getRandomProducerNr() {
		//Integer producerNr = Arrays.binarySearch(producerOfProduct, productNr);
		Integer producerNr = valueGen.randomInt(1, producerCount);	
		return producerNr;
	}
	protected Set getSelectedProducerSet(Integer maxInstanceNm) {
		Set setProducer = new HashSet();
		int size = 0;
		Integer oneProducer = null;
		boolean Empty = true;
		instanceNm = Math.min(producerCount, maxInstanceNm);
		while(size < instanceNm){
			oneProducer = getRandomProducerNr();
			setProducer.add(oneProducer);
			size = setProducer.size();
		}
		Empty = setProducer.isEmpty();
		if(Empty)
			System.out.println("The producer set is empty");
		return setProducer;
	}
	
	/*
	 * 4. Returns a random vendorNr
	 */
	protected Integer getRandomVendorNr() {
		//Integer producerNr = Arrays.binarySearch(producerOfProduct, productNr);
		Integer vendorNr = valueGen.randomInt(1, vendorCount);	
		return vendorNr;
	}
	protected Set getSelectedVendorSet(Integer maxInstanceNm) {
		Set setVendor = new HashSet();
		int size = 0;
		Integer oneVendor = null;
		boolean Empty = true;
		instanceNm = Math.min(vendorCount, maxInstanceNm);
		while(size < instanceNm){
			oneVendor = getRandomVendorNr();
			setVendor.add(oneVendor);
			size = setVendor.size();
		}
		Empty = setVendor.isEmpty();
		if(Empty)
			System.out.println("The producer set is empty");
		return setVendor;
	}	
	/*
	 * 5. Returns a random number between 1-500
	 */
	protected Integer getNumberOfPagingOffers() {
		return valueGen.randomInt(200, 300);
	}
	protected String[] offersAttribute = {"nr", "price", "validFrom", "validTo", "deliveryDays", "offerWebpage", "publisher", "publishDate"};
	/*
	 * Returns a random offersAttribute
	 */
	protected Integer getRandomOfferAttribute() {
		Integer index = valueGen.randomInt(0, offersAttribute.length-1);	
		//return offersAttribute[index];
		return index;
	}
	/*
	 * Returns a combination of parameters for Query8...
	 */
	@SuppressWarnings("unchecked")
	protected String[][] getRandomCombinationOfParasQ8(Integer maxInstanceNm) {
		int combTotalCount = 9000;
		instanceNm = Math.min(combTotalCount, maxInstanceNm);
		Set setCombination = new HashSet();
		String[][] paras = new String[instanceNm][3];
		int size = 0;
		String oneCombination = null;
		boolean Empty = true;
		// TODO
		while(size < instanceNm){
			//TODO
			int component1 = getNumberOfPagingOffers();
			String component2 = "-1";
			String component3 = "-1";
			while(component2 == component3){
				component2 = getRandomOfferAttribute().toString();
				component3 = getRandomOfferAttribute().toString();
			}
			String connect = "_";
			oneCombination = component1+connect+component2+connect+component3;
			setCombination.add(oneCombination);
			size = setCombination.size();
		}
		Empty = setCombination.isEmpty();
		//System.out.println("setCombination:"+setCombination);
		//TODO: return to String
		if(Empty)
			System.out.println("The combination set is empty");
		Iterator iterator = setCombination.iterator();
		int i=0;
		while(iterator.hasNext()){
			String element = (String) iterator.next();
			//System.out.println("element:"+element);
			String[] parts = element.split("_");
			int[] index = new int[3];
			int j=0;
			for(String a:parts){
				index[j]= Integer.valueOf(a);
				j++;
			}
			//System.out.println("index:"+index[0]+","+index[1]+","+index[2]);
			paras[i][0]= String.valueOf(index[0]);
			paras[i][1]= offersAttribute[index[1]];
			paras[i][2]= offersAttribute[index[2]];
			//System.out.println("index:"+paras[i][0]+","+paras[i][1]+","+paras[i][2]);
			i++;
		}
		return paras;
	}
	
	 //6：keywords
	 //Get random word from word list
	protected String getRandomWord() {
		Integer index = valueGen.randomInt(1, wordList.length-1);	
		return wordList[index];
	}
	 //Returns a group of word from word list (without duplicate)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Set selectedWord(Integer maxInstanceNm){
		Set setWord = new HashSet();
		int size = 0;
		String oneWord = null;
		boolean Empty = true;
		instanceNm = Math.min(wordList.length, maxInstanceNm);
		while(size < instanceNm){
			oneWord = "\""+getRandomWord()+"\"";
			setWord.add(oneWord);
			size = setWord.size();
		}
		Empty = setWord.isEmpty();
		if(Empty)
			System.out.println("The Word set is empty");
		return setWord;			
	}
	
	/*
	 * 7. Returns a random number between 1-200
	 */
	protected Integer getNumberOfPagingOffset() {
		return valueGen.randomInt(1, 200);
	}
	/*
	 * Returns a combination of parameters for Query8...
	 */
	@SuppressWarnings("unchecked")
	protected String[][] getRandomCombinationOfParasQ7(Integer maxInstanceNm) {
		int combTotalCount = vendorCount*200;
		instanceNm = Math.min(combTotalCount, maxInstanceNm);
		Set setCombination = new HashSet();
		String[][] paras = new String[instanceNm][2];
		int size = 0;
		String oneCombination = null;
		boolean Empty = true;
		// TODO
		while(size < instanceNm){
			//TODO
			String component1 = getRandomVendorNr().toString();
			String component2 = getNumberOfPagingOffset().toString();
			String connect = "_";
			oneCombination = component1+connect+component2;
			setCombination.add(oneCombination);
			size = setCombination.size();
		}
		Empty = setCombination.isEmpty();
		System.out.println("setCombination:"+setCombination);
		//TODO: return to String
		if(Empty)
			System.out.println("The combination set is empty");
		Iterator iterator = setCombination.iterator();
		int i=0;
		while(iterator.hasNext()){
			String element = (String) iterator.next();
			//System.out.println("element:"+element);
			String[] parts = element.split("_");
			int[] index = new int[2];
			int j=0;
			for(String a:parts){
				index[j]= Integer.valueOf(a);
				j++;
			}
			System.out.println("index0:"+index[0]);
			System.out.println("index1:"+index[1]);
			paras[i][0]= String.valueOf(index[0]);
			paras[i][1]= String.valueOf(index[1]);
			i++;
		}
		return paras;
	}
	
	//Q9
	protected String[] reviewAttribute = {"nr", "title", "text", "reviewDate", "rating1", "rating2", "rating3", "rating4", "publishDate"};
	/*
	 * Returns a random offersAttribute
	 */
	protected Integer getRandomReviewAttribute() {
		Integer index = valueGen.randomInt(0, reviewAttribute.length-1);	
		return index;
	}
	protected String[][] getRandomCombinationOfParasQ9(Integer maxInstanceNm) {
		int combTotalCount = vendorCount*9;
		instanceNm = Math.min(combTotalCount, maxInstanceNm);
		Set setCombination = new HashSet();
		String[][] paras = new String[instanceNm][2];
		int size = 0;
		String oneCombination = null;
		boolean Empty = true;
		// TODO
		while(size < instanceNm){
			//TODO
			String component1 = getRandomVendorNr().toString();
			String component2 = getRandomReviewAttribute().toString();
			String connect = "_";
			oneCombination = component1+connect+component2;
			setCombination.add(oneCombination);
			size = setCombination.size();
		}
		Empty = setCombination.isEmpty();
		//System.out.println("setCombination:"+setCombination);
		//TODO: return to String
		if(Empty)
			System.out.println("The combination set is empty");
		Iterator iterator = setCombination.iterator();
		int i=0;
		while(iterator.hasNext()){
			String element = (String) iterator.next();
			String[] parts = element.split("_");
			int[] index = new int[2];
			int j=0;
			for(String a:parts){
				index[j]= Integer.valueOf(a);
				j++;
			}
			//System.out.println("index:"+index[0]+","+index[1]+","+index[2]);
			paras[i][0]= String.valueOf(index[0]);
			paras[i][1]= reviewAttribute[index[1]];
			//System.out.println("index:"+paras[i][0]+","+paras[i][1]+","+paras[i][2]);
			i++;
		}
		return paras;
	}
	
	/*
	 * Returns a combination of parameters for Query12...
	 */
	@SuppressWarnings("unchecked")
	protected String[][] getRandomCombinationOfParasQ12(Integer maxInstanceNm) {
		int combTotalCount = vendorCount*producerCount;
		instanceNm = Math.min(combTotalCount, maxInstanceNm);
		Set setCombination = new HashSet();
		String[][] paras = new String[instanceNm][2];
		int size = 0;
		String oneCombination = null;
		boolean Empty = true;
		// TODO
		while(size < instanceNm){
			//TODO
			String component1 = getRandomVendorNr().toString();
			String component2 = getRandomProducerNr().toString();
			String connect = "_";
			oneCombination = component1+connect+component2;
			setCombination.add(oneCombination);
			size = setCombination.size();
		}
		Empty = setCombination.isEmpty();
		System.out.println("setCombination:"+setCombination);
		//TODO: return to String
		if(Empty)
			System.out.println("The combination set is empty");
		Iterator iterator = setCombination.iterator();
		int i=0;
		while(iterator.hasNext()){
			String element = (String) iterator.next();
			String[] parts = element.split("_");
			int[] index = new int[2];
			int j=0;
			for(String a:parts){
				index[j]= Integer.valueOf(a);
				j++;
			}
			paras[i][0]= String.valueOf(index[0]);
			paras[i][1]= String.valueOf(index[1]);
			i++;
		}
		return paras;
	}
	//Q13
	protected String getRandomDate() throws ParseException {
		String date = valueGen.getRandomDate("2000-09-20", "2006-12-23");
	    return date;
	}
	
	protected String[][] getRandomCombinationOfParasQ13(Integer maxInstanceNm) throws ParseException {
		int combTotalCount = producerCount*2285;
		instanceNm = Math.min(combTotalCount, maxInstanceNm);
		Set setCombination = new HashSet();
		String[][] paras = new String[instanceNm][2];
		int size = 0;
		String oneCombination = null;
		boolean Empty = true;
		// TODO
		while(size < instanceNm){
			//TODO
			String component1 = getRandomProducerNr().toString();
			String component2 = getRandomDate().toString();
			String connect = "_";
			oneCombination = component1+connect+component2;
			setCombination.add(oneCombination);
			size = setCombination.size();
		}
		Empty = setCombination.isEmpty();
		System.out.println("setCombination:"+setCombination);
		//TODO: return to String
		if(Empty)
			System.out.println("The combination set is empty");
		Iterator iterator = setCombination.iterator();
		int i=0;
		while(iterator.hasNext()){
			String element = (String) iterator.next();
			String[] parts = element.split("_");
			String[] index = new String[2];
			int j=0;
			for(String a:parts){
				index[j]= String.valueOf(a);
				j++;
			}
			paras[i][0]= index[0];
			paras[i][1]= index[1];
			i++;
		}
		return paras;
	}
	
	//Q14
	protected String[][] getRandomCombinationOfParasQ14(Integer maxInstanceNm) throws ParseException {
		int combTotalCount = producerCount*2285*(wordList.length);
		instanceNm = Math.min(combTotalCount, maxInstanceNm);
		Set setCombination = new HashSet();
		String[][] paras = new String[instanceNm][3];
		int size = 0;
		String oneCombination = null;
		boolean Empty = true;
		// TODO
		while(size < instanceNm){
			//TODO
			String component1 = getRandomProducerNr().toString();
			String component2 = getRandomDate().toString();
			String component3 = getRandomWord().toString();
			String connect = "_";
			oneCombination = component1+connect+component2+connect+component3;
			setCombination.add(oneCombination);
			size = setCombination.size();
		}
		Empty = setCombination.isEmpty();
		System.out.println("setCombination:"+setCombination);
		//TODO: return to String
		if(Empty)
			System.out.println("The combination set is empty");
		Iterator iterator = setCombination.iterator();
		int i=0;
		while(iterator.hasNext()){
			String element = (String) iterator.next();
			String[] parts = element.split("_");
			String[] index = new String[3];
			int j=0;
			for(String a:parts){
				index[j]= String.valueOf(a);
				j++;
			}
			paras[i][0]= index[0];
			paras[i][1]= index[1];
			paras[i][2]= "\""+index[2]+"\"";
			i++;
		}
		return paras;
	}
	

}
