/**
 * In this class the datatype "Bucket" is defined. 
 * Each Bucket has a timestamp, which is incremented in each iteration step, and a size, which stands for the number of ones in it.
 * A Sliding Window Histogram contains Buckets with exponentionally increasing size (i.e. 1,2,4,8,16,...).
 * 
 * @author Sven Winkler
 */

public class Bucket {
	int timestamp;
	int bucketSize;
	
	Bucket (int timestamp, int bucketSize) {
		this.timestamp = timestamp;
		this.bucketSize = bucketSize;
	}
	
    /**
     * Merges two Buckets of the same size according to the algorithm.
     * @param bucket1
     * @param bucket2
     * @return          Bucket of double the size with the smaller timestamp.
     */
	public static Bucket mergeTwoBuckets (Bucket bucket1, Bucket bucket2) {
		Bucket newBucket = new Bucket(0,0);
		newBucket.bucketSize = bucket1.bucketSize * 2;
		
		if (bucket1.timestamp < bucket2.timestamp) {
			newBucket.timestamp  = bucket1.timestamp;
		} else {
			newBucket.timestamp  = bucket2.timestamp;
		}
		
		return newBucket;
	}
	
    /**
     * Method that is used for printing the Buckets timestamp and size.
     * @param bucket    Gets one Bucket as parameter.
     * @return          An 1-D Array with the timestamp and the size of the Bucket as entries.
     */
	public static int [] bucketToArray (Bucket bucket) {
		int [] arr = new int[2];
		arr [0] = bucket.timestamp;
		arr [1] = bucket.bucketSize;
		
		return arr;
	}
}


