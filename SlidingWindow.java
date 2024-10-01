import java.util.Random;

/**
 * This class contains some help functions that are needed for the datastream and the window.
 * 
 * @author Sven Winkler
 */

public class SlidingWindow {
	
	/**
	 * Generates the element that enters the sliding window.
	 * 
	 * @return	Zero or one.
	 */
	public static int generateNewElement () {

		Random random = new Random();
		int randomNumber = random.nextInt(10);
		if (randomNumber < 5) {
			return 0;
		} else {
			return 1;
		}
	}
	
	/**
	 * Generates an initial window, that only contains zeros.
	 * 
	 * @param windowSize	Size of that window, that is determined by the user.
	 * @return		Array with the length of the windowSize, that only contains zeros 
	 */
	public static int [] initialWindow (int windowSize) {
		
		int [] initialWindow = new int [windowSize];
		for (int i = 0; i < windowSize; i++) {
			initialWindow [i] = 0;
		}
		
		return initialWindow;
	}
	
	/**
	 * Updates the window in every iteration step.
	 * 
	 * @param windowSize		Size of that window, that is determined by the user.
	 * @param windowLastStep	Sliding window (int array) of the last iteration step.
	 * @param newSign		The new element that enters the window.
	 * @return			Returns the window with the newest element on the right side (highest index of the array).
	 * 				The other elements of the old array are shifted to the left, the one with index 0 of the last step drops out.
	 */
	public static int [] updateWindow (int windowSize, int [] windowLastStep, int newElement) {
		
		int [] newWindow = new int [windowSize];
		newWindow [windowSize - 1] = newElement;
		
		for (int i = windowSize - 2; i >= 0; i--) {
			newWindow [i] = windowLastStep[i+1];
		}

		return newWindow;
	}

	/**
	 * Reverses an array for demonstration purpose (matter of taste).
	 * @param arr	Some array.
	 * @return		Reversed array.
	 */
	public static int [] reverseArray (int[] arr) {

		for (int i = 0; i < arr.length/2; i++) {
			int temp = arr[i];
			arr[i] = arr[arr.length - i - 1];
			arr[arr.length - i - 1] = temp;
		}

		return arr;
	}
	
	/**
	 * Calculates the number of ones in the sliding window.
	 * 
	 * @param slidingWindow		The current sliding window.
	 * @return			Number of ones.
	 */
	public static int exactSumOfOnes (int [] slidingWindow) {
		int count = 0;
		for (int i = 0; i < slidingWindow.length; i++) {
			if (slidingWindow[i] == 1) count++;
		}
		return count;
	}
}
