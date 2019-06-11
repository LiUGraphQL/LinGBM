package se.liu.ida.querygen;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.util.*;

// This module works for generating a group of values for one parameter or a combination of parameters.
// Given a placeholder, randomly select a group of values for it
public class valueSelection{

	protected ValueGenerator valueGen;
	protected HashMap<String,Integer> wordHashLabelOfProduct;
	protected HashMap<String,Integer> wordHashTextOfReview;
	protected HashMap<String,Integer> wordHashCommentOfVendor;
	protected String[] wordListLabelOfProduct;
	protected String[] wordListTextOfReview;
	protected String[] wordListCommentOfVendor;
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
	
	protected Integer scalefactor; 

	
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

	//Read in cdlw.dat file: scaling data and words of Product labels
	private void readDateAndLabelWords(File resourceDir) {
		File cdlw = new File(resourceDir, "cdlw.dat");
		ObjectInputStream currentDateAndLabelWordsInput;
		try {
			currentDateAndLabelWordsInput = new ObjectInputStream(new FileInputStream(cdlw));
			productCount = currentDateAndLabelWordsInput.readInt();
			System.out.println("number of products: "+productCount);
			reviewCount = currentDateAndLabelWordsInput.readInt();
			System.out.println("number of reviews: "+reviewCount);
			offerCount = currentDateAndLabelWordsInput.readInt();
			System.out.println("number of offers: "+offerCount);
			currentDate = (GregorianCalendar) currentDateAndLabelWordsInput.readObject();
			@SuppressWarnings("unchecked")
			HashMap<String, Integer> x = (HashMap<String, Integer>)currentDateAndLabelWordsInput.readObject();
			wordHashLabelOfProduct = x ;
			wordListLabelOfProduct = wordHashLabelOfProduct.keySet().toArray(new String[0]);
			HashMap<String, Integer> y = (HashMap<String, Integer>)currentDateAndLabelWordsInput.readObject();
			wordHashTextOfReview = y ;
			wordListTextOfReview = wordHashTextOfReview.keySet().toArray(new String[0]);
			System.out.println("number of words that used in the text of Reviews: "+wordListTextOfReview.length);
			HashMap<String, Integer> z = (HashMap<String, Integer>)currentDateAndLabelWordsInput.readObject();
			wordHashCommentOfVendor = z ;
			wordListCommentOfVendor = wordHashCommentOfVendor.keySet().toArray(new String[0]);
			System.out.println("number of words that used in the comment of Vendors: "+wordListCommentOfVendor.length);
		} catch(IOException | ClassNotFoundException e) {
			System.err.println("Could not open or process file " + cdlw.getAbsolutePath());
			System.err.println(e.getMessage());
			System.exit(-1);
		}
	}

	//Read in rr.dat file: relationship of ratingsiteOfReview
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

	//Read in vo.dat file: vendor -offer relationships
	private void readOfferAndVendorData(File resourceDir, File pp) {
		File vo = new File(resourceDir, "vo.dat");
		ObjectInputStream offerVendorInput;
		try {
			offerVendorInput = new ObjectInputStream(new FileInputStream(vo));
			vendorOfOffer = (Integer[]) offerVendorInput.readObject();
			//Add vendorCount
			vendorCount =  vendorOfOffer.length-1;
			System.out.println("number of vendors: "+vendorCount);

		} catch(IOException e) {
			System.err.println("Could not open or process file " + pp.getAbsolutePath());
			System.err.println(e.getMessage());
			System.exit(-1);
		} catch(ClassNotFoundException e) { System.err.println(e); }
	}

	// read in pp.dat file: Product-Producer Relationships in outputDir
	private File readProductProducerData(File resourceDir) {
		File pp = new File(resourceDir, "pp.dat");
		ObjectInputStream productProducerInput;
		try {
			productProducerInput = new ObjectInputStream(new FileInputStream(pp));
			producerOfProduct = (Integer[]) productProducerInput.readObject();
			//Add ProducerCount
			producerCount =  producerOfProduct.length-1;
			System.out.println("number of producers: "+producerCount);
			scalefactor = producerOfProduct[producerOfProduct.length-1];
		} catch(IOException e) {
			System.err.println("Could not open or process file " + pp.getAbsolutePath());
			System.err.println(e.getMessage());
			System.exit(-1);
		}
		catch(ClassNotFoundException e) { System.err.println(e); }
		return pp;
	}


	protected String[] offersAttribute = {"nr", "price", "validFrom", "validTo", "deliveryDays", "offerWebpage", "publisher", "publishDate"};
	protected String[] reviewAttribute = {"nr", "title", "text", "reviewDate", "rating1", "rating2", "rating3", "rating4", "publishDate"};

	/*
	 * return a ramdom value for specified placeholder (int)
	 */
	protected String getRandom(String field) throws ParseException {
		String randomNr = null;
		if ("$productID".equals(field)) {
			randomNr = String.valueOf(valueGen.randomInt(1, productCount));
		} else if ("$producerID".equals(field)) {
			randomNr = String.valueOf(valueGen.randomInt(1, producerCount));
		} else if ("$reviewID".equals(field)) {
			randomNr = String.valueOf(valueGen.randomInt(1, reviewCount));
		} else if ("$offerID".equals(field)) {
			randomNr = String.valueOf(valueGen.randomInt(1, offerCount));
		} else if ("$vendorID".equals(field)) {
			randomNr = String.valueOf(valueGen.randomInt(1, vendorCount));
		} else if ("$attrOffer1".equals(field)||"$attrOffer2".equals(field)) {
			//return index of attribute field
			randomNr = String.valueOf(valueGen.randomInt(0, offersAttribute.length-1));
		}
		else if ("$attrReview".equals(field)) {
			//return index of attribute field
			randomNr = String.valueOf(valueGen.randomInt(0, reviewAttribute.length-1));
		}
		else if ("$cnt".equals(field)){
			randomNr= String.valueOf(valueGen.randomInt(200, 300));
		}
		else if("$offset".equals(field)){
			randomNr= String.valueOf(valueGen.randomInt(1, 200));
		}
		else if ("$date".equals(field)){
			randomNr = valueGen.getRandomDate("2000-09-20", "2006-12-23");
		}
		else if ("$textOfReviewKeyword".equals(field)){
			Integer index = valueGen.randomInt(0, wordListTextOfReview.length-1);
			randomNr = wordListTextOfReview[index];
		}
		else if ("$commentOfVendorKeyword".equals(field)){
			Integer index = valueGen.randomInt(0, wordListCommentOfVendor.length-1);
			randomNr = wordListCommentOfVendor[index];
		}
		return randomNr;
	}


	//number of instances
	protected Integer getInstanceNm(String field, Integer maxInstanceNm) {
		Integer instanceNm= null;
		if ("$productID".equals(field)) {
			instanceNm = Math.min(productCount, maxInstanceNm);
		} else if ("$producerID".equals(field)) {
			instanceNm = Math.min(producerCount, maxInstanceNm);
		} else if ("$reviewID".equals(field)) {
			instanceNm = Math.min(reviewCount, maxInstanceNm);
		} else if ("$offerID".equals(field)) {
			instanceNm = Math.min(offerCount, maxInstanceNm);
		} else if ("$vendorID".equals(field)) {
			instanceNm = Math.min(vendorCount, maxInstanceNm);
		} else if ("$textOfReviewKeyword".equals(field)) {
			//return index of attribute field
			instanceNm = Math.min(wordListTextOfReview.length, maxInstanceNm);
		}else if ("$producerID-$vendorID".equals(field)) {
			int combTotalCount = vendorCount*producerCount;
			instanceNm = Math.min(combTotalCount, maxInstanceNm);
		}else if ("$vendorID-$offset".equals(field)) {
			int combTotalCount = vendorCount * 200;
			instanceNm = Math.min(combTotalCount, maxInstanceNm);
		}
		else if ("$producerID-$date".equals(field)) {
			int combTotalCount = producerCount*2285;
			instanceNm = Math.min(combTotalCount, maxInstanceNm);
		}
		else if ("$producerID-$date-$commentOfVendorKeyword".equals(field)) {
			int combTotalCount = producerCount*2285*(wordListCommentOfVendor.length);
			instanceNm = Math.min(combTotalCount, maxInstanceNm);
		}
		else if ("$vendorID-$attrReview".equals(field)) {
			int combTotalCount = vendorCount*9;
			instanceNm = Math.min(combTotalCount, maxInstanceNm);
		}
		else if ("$cnt-$attrOffer1-$attrOffer2".equals(field)){
			int combTotalCount = 9000;
			instanceNm = Math.min(combTotalCount, maxInstanceNm);
		}
		return instanceNm;
	}

	/*
	 * Returns a combination of values for placeholders
	 */
	protected Set getRandomSelectedValues(String field, Integer maxInstanceNm) throws ParseException {
		instanceNm = getInstanceNm(field, maxInstanceNm);
		Set setCombination = new HashSet();
		int size = 0;

		String[] fields = field.split("-");
		int paraNum = fields.length;

		boolean Empty = true;
		String component = null;
		String connect = "_";

		while(size < instanceNm){
			String oneCombination = getRandom(fields[0]);
			for(int i = 1; i< paraNum;i++){
				String para = fields[i];
				component = getRandom(para);
				oneCombination = oneCombination+connect+component;
			}

			setCombination.add(oneCombination);
			size = setCombination.size();
		}
		Empty = setCombination.isEmpty();

		if(Empty)
			System.out.println("The combination set is empty");
		return setCombination;
	}

	protected String[][] SelectedValues(String field, Integer maxInstanceNm) throws ParseException {
		Set setCombination=getRandomSelectedValues(field, maxInstanceNm);
		String[] fields = field.split("-");
		int paraNum = fields.length;
		String[][] paras = new String[instanceNm][paraNum];
		Iterator iterator = setCombination.iterator();
		int i=0;
		while(iterator.hasNext()){
			String element = (String) iterator.next();
			String[] parts = element.split("_");
			int k=0;
			for(String value:parts){
				if(("$date".equals(fields[k]))||("$textOfReviewKeyword".equals(fields[k]))||("$commentOfVendorKeyword".equals(fields[k]))){
					paras[i][k]= "\""+value+"\"";
				}
				else if("$attrReview".equals(fields[k])){
					int reviewIndex= Integer.valueOf(value);
					paras[i][k]= reviewAttribute[reviewIndex];
				}
				else if(("$attrOffer1".equals(fields[k]))||("$attrOffer2".equals(fields[k]))){
					int offerIndex= Integer.valueOf(value);
					paras[i][k]= offersAttribute[offerIndex];
				}
				else{
					paras[i][k]= value;
				}
				k++;
			}
			i++;
		}
		return paras;
	}


}
