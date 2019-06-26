package benchmark.testdriver;

import java.io.*;
import java.util.*;

import benchmark.generator.DateGenerator;
import benchmark.model.*;
import benchmark.vocabulary.*;

import benchmark.vocabulary.XSD;

public class LocalSPARQLParameterPool extends AbstractParameterPool {
	private BufferedReader updateFileReader = null;
	private GregorianCalendar publishDateMin = new GregorianCalendar(2007,5,20);
	
	public LocalSPARQLParameterPool(File resourceDirectory, Long seed) {
		init(resourceDirectory, seed);
	}
	
	public LocalSPARQLParameterPool(File resourceDirectory, Long seed, File updateDatasetFile) {
		init(resourceDirectory, seed);
		try {
			updateFileReader = new BufferedReader(new FileReader(updateDatasetFile));
		} catch (FileNotFoundException e) {
			System.out.println("Could not open update dataset file: " + e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	@Override
	public Object[] getParametersForQuery(Query query) {
		Byte[] parameterTypes = query.getParameterTypes();
		Object[] parameters = new Object[parameterTypes.length];
		ArrayList<Integer> productFeatureIndices = new ArrayList<Integer>();
		ProductType pt = null;
		GregorianCalendar randomDate = null;
		
		for(int i=0;i<parameterTypes.length;i++) {
			if(parameterTypes[i]==Query.PRODUCT_TYPE_URI) {
				pt = ParameterGenerator.getRandomProductType(productTypeLeaves, valueGen);
				parameters[i] = pt.toString();
			}
			else if(parameterTypes[i]==Query.PRODUCT_FEATURE_URI)
				productFeatureIndices.add(i);
			else if(parameterTypes[i]==Query.PRODUCT_PROPERTY_NUMERIC)
				parameters[i] = getProductPropertyNumeric();
			else if(parameterTypes[i]==Query.PRODUCT_URI)
				parameters[i] = getRandomProductURI();
			else if(parameterTypes[i]==Query.CURRENT_DATE)
				parameters[i] = currentDateString;
			else if(parameterTypes[i]==Query.COUNTRY_URI)
				parameters[i] = "<" + ISO3166.find((String)countryGen.getRandom()) + ">";
			else if(parameterTypes[i]==Query.REVIEW_URI)
				parameters[i] = getRandomReviewURI();
			else if(parameterTypes[i]==Query.WORD_FROM_DICTIONARY1)
				parameters[i] = getRandomWord();
			else if(parameterTypes[i]==Query.OFFER_URI)
				parameters[i] = getRandomOfferURI();
			else if(parameterTypes[i]==Query.UPDATE_TRANSACTION_DATA)
				parameters[i] = getUpdateTransactionData();
			else if(parameterTypes[i]==Query.CONSECUTIVE_MONTH) {
				if(randomDate==null)
					randomDate = ParameterGenerator.getRandomDate(publishDateMin, valueGen, 309);
				int monthNr = (Integer)query.getAdditionalParameterInfo(i);
				parameters[i] = ParameterGenerator.getConsecutiveMonth(randomDate, monthNr);
			} else if(parameterTypes[i]==Query.PRODUCER_URI)
				parameters[i] = getRandomProducerURI();
			else if(parameterTypes[i]==Query.PRODUCT_TYPE_RANGE) {
				Integer[] rangeModifiers = (Integer[])query.getAdditionalParameterInfo(i);
				int ptnr = ParameterGenerator.getRandomProductTypeNrFromRange(maxProductTypePerLevel, rangeModifiers, valueGen, valueGen2);
				parameters[i] = ProductType.getURIRef(ptnr);
			}
			else
				parameters[i] = null;
		}
		
		if(productFeatureIndices.size()>0 && pt == null) {
			System.err.println("Error in parameter generation: Asked for product features without product type.");
			System.exit(-1);
		}
		
		String[] productFeatures = getRandomProductFeatures(pt, productFeatureIndices.size());
		for(int i=0;i<productFeatureIndices.size();i++) {
			parameters[productFeatureIndices.get(i)] = productFeatures[i];
		}
		
		return parameters;
	}
	
	/*
	 * Get number distinct random Product Feature URIs of a certain Product Type
	 */
	private String[] getRandomProductFeatures(ProductType pt, Integer number) {
		String[] productFeatures = new String[number];
		
		List<Integer> pfs = ParameterGenerator.getRandomProductFeatures(pt, number);
		
		for(int i=0;i<number;i++) {
			Integer index = valueGen.randomInt(0, pfs.size()-1);
			productFeatures[i] = ProductFeature.getURIref(pfs.get(index));
			pfs.remove(index);
		}
		
		return productFeatures;
	}

	
	/*
	 * Get a random Product URI
	 */
	private String getRandomProductURI() {
		Integer productNr = getRandomProductNr();
		Integer producerNr = getProducerOfProduct(productNr);
		
		return Product.getURIref(productNr, producerNr);
	}
	
	/*
	 * Get a random Offer URI
	 */
	private String getRandomOfferURI() {
		Integer offerNr = valueGen.randomInt(1, offerCount);
		Integer vendorNr = getVendorOfOffer(offerNr);
		
		return Offer.getURIref(offerNr, vendorNr);
	}
	
	/*
	 * Get a random Review URI
	 */
	private String getRandomReviewURI() {
		Integer reviewNr = valueGen.randomInt(1, reviewCount);
		Integer ratingSiteNr = getRatingsiteOfReviewer(reviewNr);
		
		return Review.getURIref(reviewNr, ratingSiteNr);
	}
	
	/*
	 * Get a random producer URI
	 */
	private String getRandomProducerURI() {
		Integer producerNr = valueGen.randomInt(1, producerOfProduct.length-1);
		
		return Producer.getURIref(producerNr);
	}
	
	
	/*
	 * Return the triples to inserted into the store
	 */
	private String getUpdateTransactionData() {
		StringBuilder s = new StringBuilder();
		String line = null;
		if(updateFileReader==null) {
			System.err.println("Error: No update dataset file specified! Use -udataset option of the test driver with a generated update dataset file as argument.");
			System.exit(-1);
		}
		
		try {
			while((line=updateFileReader.readLine()) != null) {
				if(line.equals("#__SEP__"))
					break;
				s.append(line);
				s.append("\n");
			}
		} catch (IOException e) {
			System.err.println("Error reading update data from file: " + e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		return s.toString();
	}

	@Override
	protected String formatDateString(GregorianCalendar date) {
		return "\"" + DateGenerator.formatDateTime(date) + "\"^^<" + XSD.DateTime + ">";
	}
}
