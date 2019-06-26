package benchmark.testdriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import benchmark.generator.Generator;
import benchmark.generator.RandomBucket;
import benchmark.generator.ValueGenerator;
import benchmark.model.ProductType;

public abstract class AbstractParameterPool {
	protected ValueGenerator valueGen;
	protected ValueGenerator valueGen2;
	protected RandomBucket countryGen;
	protected GregorianCalendar currentDate;
	protected String currentDateString;
	protected ProductType[] productTypeLeaves;
	protected HashMap<String,Integer> wordHash;
	protected String[] wordList;
	protected Integer[] producerOfProduct;
	protected Integer[] vendorOfOffer;
	protected Integer[] ratingsiteOfReview;
	protected Integer productCount;
	protected Integer reviewCount;
	protected Integer offerCount;
	protected int productTypeCount;
	protected List<Integer> maxProductTypePerLevel;
	
	protected Integer scalefactor; 
	
	public abstract Object[] getParametersForQuery(Query query);
	
	public Integer getScalefactor() {
		return scalefactor;
	}
	
    protected void init(File resourceDir, long seed) {
		Random seedGen = new Random(seed);
		valueGen = new ValueGenerator(seedGen.nextLong());

		countryGen = Generator.createCountryGenerator(seedGen.nextLong());
		
		valueGen2 = new ValueGenerator(seedGen.nextLong());
    	
		//Read in the Product Type hierarchy from resourceDir/pth.dat
		readProductTypeHierarchy(resourceDir);

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
			currentDateString = formatDateString(currentDate);
			
			@SuppressWarnings("unchecked")
			HashMap<String, Integer> x = (HashMap<String, Integer>)currentDateAndLabelWordsInput.readObject();
			wordHash = x ;
			wordList = wordHash.keySet().toArray(new String[0]);
		} catch(IOException e) {
			System.err.println("Could not open or process file " + cdlw.getAbsolutePath());
			System.err.println(e.getMessage());
			System.exit(-1);
		}
		catch(ClassNotFoundException e) { System.err.println(e); }
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
			scalefactor = producerOfProduct[producerOfProduct.length-1];
		} catch(IOException e) {
			System.err.println("Could not open or process file " + pp.getAbsolutePath());
			System.err.println(e.getMessage());
			System.exit(-1);
		}
		catch(ClassNotFoundException e) { System.err.println(e); }
		return pp;
	}

	@SuppressWarnings("unchecked")
	private void readProductTypeHierarchy(File resourceDir) {
		ObjectInputStream productTypeInput;
		File pth = new File(resourceDir, "pth.dat");
		try {
			productTypeInput = new ObjectInputStream(new FileInputStream(pth));
			productTypeLeaves = (ProductType[]) productTypeInput.readObject();
			productTypeCount = productTypeInput.readInt();
			maxProductTypePerLevel = (List<Integer>) productTypeInput.readObject();
		} catch(IOException e) {
			System.err.println("Could not open or process file " + pth.getAbsolutePath());
			System.err.println(e.getMessage());
			System.exit(-1);
		}
		catch(ClassNotFoundException e) { System.err.println(e); }
	}
    
    /**
     * Format the date string DBMS dependent
     * @param date The object to transform into a string representation
     * @return formatted String
     */
    abstract protected String formatDateString(GregorianCalendar date);
    
	/*
	 * Get a random Product URI
	 */
	protected Integer getRandomProductNr() {
		Integer productNr = valueGen.randomInt(1, productCount);
		
		return productNr;
	}
	
	/*
	 * Returns the ProducerNr of given Product Nr.
	 */
	protected Integer getProducerOfProduct(Integer productNr) {
		Integer producerNr = Arrays.binarySearch(producerOfProduct, productNr);
		if(producerNr<0)
			producerNr = - producerNr - 1;
		
		return producerNr;
	}
	
	/*
	 * Returns the ProducerNr of given Product Nr.
	 */
	protected Integer getVendorOfOffer(Integer offerNr) {
		Integer vendorNr = Arrays.binarySearch(vendorOfOffer, offerNr);
		if(vendorNr<0)
			vendorNr = - vendorNr - 1;
		
		return vendorNr;
	}
	
	/*
	 * Returns the Rating Site Nr of given Review Nr
	 */
	protected Integer getRatingsiteOfReviewer(Integer reviewNr) {
		Integer ratingSiteNr = Arrays.binarySearch(ratingsiteOfReview, reviewNr);
		if(ratingSiteNr<0)
			ratingSiteNr = - ratingSiteNr - 1;
		
		return ratingSiteNr;
	}
	
	/*
	 * Returns a random number between 1-500
	 */
	protected Integer getProductPropertyNumeric() {
		return valueGen.randomInt(1, 500);
	}
	
	/*
	 * Get random word from word list
	 */
	protected String getRandomWord() {
		Integer index = valueGen.randomInt(0, wordList.length-1);
		
		return wordList[index];
	}
}
