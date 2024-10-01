import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class represents a data stream of zeros and ones. These elements are entering the window in the console from the right side.
 * The user has the choice of the size of the window and the error parameter.
 * The command line output contains the current window, the exact and the estimated number of ones and the absolute and relative error.
 * 
 * @author Sven Winkler
 */
public class SlidingWindowThread {
	
	/**
	 * Asks the user about the window size and error parameter and starts the datastream as well as the algorithm that processes it.
	 */
	SlidingWindowThread () {

		System.out.println("Please insert the window size! I suggest a number between 30 and 50.");
		Scanner windowSizeScanner = new Scanner(System.in);
		int slidingWindowSize = windowSizeScanner.nextInt();
		

		System.out.println("Please insert the error parameter! A smaller number, like a tenth of the chosen window size, would make sense.");
		Scanner errParamScanner = new Scanner(System.in);
		int errParam = errParamScanner.nextInt();

		thread(slidingWindowSize, errParam);

		//Close input stream
		windowSizeScanner.close();
		errParamScanner.close();
	};

	/**
	 * Starts a series of 2000 iteration steps.
	 * @param windowSize	The (fixed) size of the sliding window, which is chosen by the user.
	 * @param errParam	The error parameter that determines the accuracy of the algorithm.
	 */
	private void thread (int windowSize, int errParam) {
		
		//Initial window: Array that only contains zeros
		int [] slidingWindow = SlidingWindow.initialWindow(windowSize);
		SlidingWindow.reverseArray(slidingWindow);
		System.out.println(Arrays.toString(slidingWindow));

		//Initial ("empty") histogram
		ArrayList <Bucket> histogram = new ArrayList <Bucket> ();
		int [] histToArr = SlidingWindowHistogram.histToArray(histogram);
		int [][] histToMultArr = SlidingWindowHistogram.histToMultArray(histogram);

		System.out.println(Arrays.toString(histToArr));
		System.out.println("Timestamp, Size : " + Arrays.deepToString(histToMultArr));

		int i = 0;
		while(i <= 2000) {
			
			//Generates the character that enters the window
			int newElement = SlidingWindow.generateNewElement();
			
			//Updates the window, based on the new character
			slidingWindow = SlidingWindow.updateWindow(windowSize, slidingWindow, newElement);
			System.out.println("Sliding Window: ");
			System.out.println(Arrays.toString(slidingWindow) + "  <-  " + "New element: " + newElement + " (entering from the right side) \n");

			//Calculates the exact number of ones in the current window
			int exactNumber = SlidingWindow.exactSumOfOnes(slidingWindow);
			System.out.println("Exact number of ones in the window: " + exactNumber + "\n");

			//Updates the histogram according to the algorithm, based on the new character
			histogram = SlidingWindowHistogram.updateHistogram(histogram, windowSize, newElement, errParam);
			histToArr = SlidingWindowHistogram.histToArray(histogram);
			histToMultArr = SlidingWindowHistogram.histToMultArray(histogram);		
			
			System.out.println("The histogram contains the following buckets in the representation [Timestamp, Size]. ");
			System.out.println(Arrays.deepToString(histToMultArr) + "\n");

			System.out.println("Representation of the bucket sizes only: ");
			System.out.println(Arrays.toString(SlidingWindow.reverseArray(histToArr)) + "\n");

			//Calculates the estimated number of ones in the window according to the algorithm
			int estNum = SlidingWindowHistogram.estimatedNumberOfOnes(histToMultArr);

			System.out.println("Estimated number of ones according to the algorithm: ");
			System.out.println(estNum + "\n");
			
			//Absolute error
			System.out.println("Deviation of the estimate by the algorithm from the true number of ones in the window: ");
			int absErr = Math.abs(estNum - exactNumber);
			System.out.println("Absolute error: " + absErr);
			
			//Relative error
			float relErr = 0;
			if (exactNumber > 0) {
				relErr = (float) absErr/exactNumber;
			} 
			
			System.out.println("Relative error: " + relErr);

			System.out.println("----------------------------------------------------------------------------------------------------");
			System.out.println("Iteration step: " + i);
			System.out.println("----------------------------------------------------------------------------------------------------");

			//Some delay for demonstration purposes
			switch (i) {
				case 1,2,3,4,5,1999,2000:
					try {
            			Thread.sleep(1500);
        			} catch (InterruptedException e) {
            			System.out.println("got interrupted!");
        			}					
					break;

				case 6,7,8,1996,1997,1998:
					try {
            			Thread.sleep(750);
        			} catch (InterruptedException e) {
            			System.out.println("got interrupted!");
        			}					
					break;

				case 9,10,11,12,13,14,15,16,1991,1992,1993,1994,1995:
					try {
            			Thread.sleep(300);
        			} catch (InterruptedException e) {
            			System.out.println("got interrupted!");
        			}					
					break;
				
				case 17,18,19,20,21,22,23,24,25,26,27,28:
					try {
            			Thread.sleep(150);
        			} catch (InterruptedException e) {
            			System.out.println("got interrupted!");
        			}					
					break;

				default:
					break;
			}

			//Termination condition, do NOT change
			i++;
		}
	}
	
}
