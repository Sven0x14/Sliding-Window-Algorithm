import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

/**
 * In this class, the algorithm is implemented. 
 * The actual algorithm is represented by the method "updateHistogram", which is called in the event of the arrival of a new element.
 * 
 * @author Sven Winkler
 */
public class SlidingWindowHistogram {
	
	/**
	 * Updates the histogram, which is generally represented by an ArrayList containing "Bucket" datatypes.
	 * @param histogram  	The histogram of the last iteration step, which should be updated.
	 * @param windowSize 	Size of the Sliding Window, which determines whether the last Bucket with the highest timestamp should be removed or not.
	 * @param newElement 0 or 1	that is processed by the algorithm
	 * @param errParam	 	According to the algorithm, the absolute error is limited by errParam/2
	 * @return			Returns the updated histogram (ArrayList containing Buckets)
	 */

	public static ArrayList <Bucket> updateHistogram (ArrayList <Bucket> histogram, int windowSize, int newElement, int errParam) {
		
		//Increment timestamps in every iteration step
		incrementTimestamps(histogram);
		
		//Only if the new character in the window is a one, create a new Bucket (1,1)
		createNewBucket(newElement, histogram);
		
		//Remove the last bucket, if its timestamp is expired (timestamp > window Size)
		removeBucket(histogram, windowSize);
		
		int bucketSizeMergeRequired;
		do {
			//First, determine the count of Buckets in every size class
			HashMap <Integer, Integer> bucketsPerClass = new HashMap <Integer, Integer>();
			bucketsPerClass = countBucketsPerClass(histogram);
			
			//Determine if a size class contains more buckets than allowed by the algorithm
			bucketSizeMergeRequired = bucketSizeMergeRequired(bucketsPerClass, errParam);
			
			//Merge Buckets, when a class contains more than allowed. Do this until the number is correct for every class.
			mergeBuckets(histogram, bucketSizeMergeRequired);
			} while ((bucketSizeMergeRequired != 0) );
		
		return histogram;
	}
	
	/**
	 * Increments the Timestamps of all Buckets (the window "moved" one step)
	 * @param histogram	is represented by an ArrayList containing "buckets"
	 * @return 		Returns the same datastructure with the timestamps of the buckets incremented by one
	 */
	private static ArrayList <Bucket> incrementTimestamps (ArrayList <Bucket> histogram) {
		histogram.forEach((n) -> n.timestamp++);
		return histogram;
	}
	
	/** 
	 * Creates new Bucket with timestamp = 1 and size = 1 
	 * @param newSign	new character in the window
	 * @param histogram	
	 * @return
	 */
	private static ArrayList <Bucket> createNewBucket (int newSign, ArrayList <Bucket> histogram) {
		if (newSign == 1) {
			Bucket newBucket = new Bucket(1,1);
			histogram.add(newBucket);
		}
		return histogram;
	}
	
	/**
	 * Iterates through the histogram in order to figure out if the timestamp of a bucket is expired. Removes this bucket in this case.
	 * @param histogram	ArrayList containing bucket datatypes
	 * @param windowSize	To compare the timestamp of each bucket with the size of the Sliding Window
	 * @return		The histogram with possibly removed last bucket
	 */
	private static ArrayList <Bucket> removeBucket (ArrayList <Bucket> histogram, int windowSize) {
		histogram.removeIf(n -> n.timestamp > windowSize);
		return histogram;
	}
	
	/**
	 * Counts the number of buckets in each size class. 
	 * @param histogram	For example: A histogram (ArrayList) 4,4,4,2,2,1,1,1
	 * @return		returns a HashMap: {(4:3),(2:2),(1,3)}
	 */
	private static HashMap <Integer,Integer> countBucketsPerClass (ArrayList <Bucket> histogram) {
		
		//Returns only the sizes of the buckets
		int [] bucketList = new int[histogram.size()];
		int i = 0;
		Iterator <Bucket> iterator = histogram.iterator();
		while (iterator.hasNext()) {
			Bucket b = iterator.next();
			bucketList[i] = b.bucketSize;
			i++;
		}
		Arrays.sort(bucketList);
		
		int [] printBucketList = bucketList.clone();
		//Sort in descending order
        for (int k = 0, j = printBucketList.length - 1, temp; k < j; k++, j--) {
            temp = printBucketList[k];
            printBucketList[k] = printBucketList[j];
            printBucketList[j] = temp;
        }
		
		HashMap <Integer,Integer> bucketsPerClass = new HashMap <Integer,Integer>();
		
		int j = 0;
		int numberOfBuckets = 0;
		int sizeClass = 1;
		
		while (j < bucketList.length) {
			
			if (bucketList[j] == sizeClass) {
				numberOfBuckets++;
			}
			
			if (j == bucketList.length - 1) {
				bucketsPerClass.put(sizeClass, numberOfBuckets);
			}
			
			if (bucketList[j] == 2*sizeClass) {
				bucketsPerClass.put(sizeClass, numberOfBuckets);
				sizeClass*=2;
				numberOfBuckets = 1;
			}
			j++;
		}
		
		/*bucketsPerClass.forEach ((size, number) -> {
			System.out.println("countBucketsPerClass: " + "Größenklasse: " + size + ", Anzahl: " + number);
		});*/
		
		return bucketsPerClass;
	}
	
	/**
	 * This method returns the lowest size class, that contains too many buckets (according to the algorithm, more than errParam/2).
	 * In the first method "updateHistogram", this method is called as long there exist such size classes. 
	 * The method "mergeBuckets" below merges the last two buckets of each size class (when nescessary) until this method returns 0.
	 * @param bucketsPerClass	Gets the number of buckets per class from the above method (called in updateHistogram)
	 * @param errParam		The error parameter that determines if a merge is required.
	 * @return			The lowest size class, where a merge of buckets is required.
	 */
	private static int bucketSizeMergeRequired (HashMap <Integer, Integer> bucketsPerClass, int errParam) {
		int bucketSizeMergeRequired = 0;
		
		//Condition to merge: check the number of Buckets in each size class
		for (Map.Entry <Integer, Integer> set : bucketsPerClass.entrySet()) {
			if (set.getKey() == 1 && set.getValue() > errParam) {bucketSizeMergeRequired = 1;}
			if (set.getKey() != 1 && set.getValue() > (int) errParam/2) {bucketSizeMergeRequired = set.getKey();}
		}
		
		return bucketSizeMergeRequired;
	}
	
	/**
	 * Merges the two buckets with the highest timestamp in a size class, i.e. removes them and adds a new bucket
	 * with doubled sized and the lower timestamp of the old buckets.
	 * @param histogram			Current histogram
	 * @param bucketSizeMergeRequired	The size class in which the merge should be performed.
	 * @return				Histogram after one merge. 
	 * This process is repeated until every size class has the correct number of buckets.
	 */
	private static ArrayList <Bucket> mergeBuckets (ArrayList <Bucket> histogram, int bucketSizeMergeRequired) {
		
		//If there are no buckets in the histogram, don´t make changes
		if (histogram.size() == 0) {
			return histogram;
		}
		
		//If every size class already contains the correct number, don´t make changes
		if (bucketSizeMergeRequired == 0) {
			return histogram;
		}
		
		/*Buckets to merge:*/		
		int secondLastTimestamp = 0;
		int lastTimestamp = 0;
		Iterator <Bucket> iterator = histogram.iterator();
		while (iterator.hasNext()) {
			Bucket temporaryBucket = iterator.next();
			
			int timestamp = temporaryBucket.timestamp;			
			int bucketSize = temporaryBucket.bucketSize;
			
			if (bucketSize == bucketSizeMergeRequired && timestamp > lastTimestamp) {
				lastTimestamp = timestamp;
			}
		}
		
		Iterator <Bucket> secondIterator = histogram.iterator();
		while (secondIterator.hasNext()) {
			Bucket temporaryBucket = secondIterator.next();
			
			int timestamp = temporaryBucket.timestamp;			
			int bucketSize = temporaryBucket.bucketSize;
			
			if (bucketSize == bucketSizeMergeRequired && timestamp > secondLastTimestamp && timestamp < lastTimestamp) {
				secondLastTimestamp = timestamp;
			}
		}
		
		final int finalSecondLastTimestamp = secondLastTimestamp;
		final int finalLastTimestamp = lastTimestamp;
		
		histogram.removeIf(n -> n.timestamp == finalSecondLastTimestamp);
		histogram.removeIf(n -> n.timestamp == finalLastTimestamp);
		
		Bucket newBucket = new Bucket(finalSecondLastTimestamp, 2*bucketSizeMergeRequired);
		histogram.add(newBucket);
		
		return histogram;
	}
	
	/*----------------------------------------------------------------------------------*/
	/**
	 * Returns the number of ones in the Sliding Window according to the algorithm, based on the datastructure that is created by the algorithm.
	 * @param histArr	The histogram as an array (transfered from ArrayList to multidimensional array by the method histToMultArray())
	 * @return		Sum of the bucket sizes minus the half of the size of the last bucket (defined by the algorithm).
	 */
	public static int estimatedNumberOfOnes (int [][] histArr) {
		int count = 0;
		for (int i = 0; i < histArr.length; i++) {
			count += histArr[i][1];
		}
		if (histArr.length > 0) count = count - (histArr[0][1])/2;
		return count;
	}

	/**
	 * Method for console output
	 * @param histogram	histogram
	 * @return		2 dimensional, sorted array of buckets with (timestamp, size)
	 */
	
	public static int[][] histToMultArray (ArrayList <Bucket> histogram) {
		//Number of buckets in the histogram
		int histSize = histogram.size();
		
		//Create a 2 dimensional array
		int [][] histArr = new int [histSize][2];
		Iterator <Bucket> histIterator = histogram.iterator();
		int bucketIndex = 0;
		while (histIterator.hasNext()) {
			Bucket bucket = histIterator.next();
			int timestamp = bucket.timestamp;
			int bucketSize = bucket.bucketSize;
			histArr[bucketIndex][0] = timestamp;
			histArr[bucketIndex][1] = bucketSize;
			bucketIndex++;
		}
		
		//Sort in descending order
		Arrays.sort(histArr, (a, b) -> Integer.compare(b[0], a[0]));
		
		return histArr;
	}

    
	// Funktion für Konsolenoutput
	
	public static int [] histToArray (ArrayList <Bucket> histogram) {
		int histSize = histogram.size();
		
		//Erstelle ein 2-D-Array aus dem Histogramm
		int [][] histArr = new int [histSize][2];
		Iterator <Bucket> histIterator = histogram.iterator();
		int bucketIndex = 0;
		while (histIterator.hasNext()) {
			Bucket bucket = histIterator.next();
			int timestamp = bucket.timestamp;
			int bucketSize = bucket.bucketSize;
			histArr[bucketIndex][0] = timestamp;
			histArr[bucketIndex][1] = bucketSize;
			bucketIndex++;
		}
		Arrays.sort(histArr, new java.util.Comparator <int []>() {
		    public int compare(int [] a, int [] b) {
		        return Integer.compare(a[0], b[0]);
		    }
		});
		
		int [] newHistArr = new int[histSize];
		for (int i = 0; i < histSize; i++) {
			newHistArr[i] = histArr[i][1];
		}
		
		return newHistArr;
	}
}
	