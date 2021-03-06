/**
* Class Stopwatch measures time elapsed.
*
*/

public class Stopwatch
{
    private long startTime;
    private long stopTime;

    public static final double NANOS_PER_SEC = 1000000000.0;

	/**
	* Start the stop watch.
	*/
	public void start() {	
		System.gc();
		startTime = System.nanoTime();
	}

	/**
	* Stop the stop watch.
	*/
	public void stop() {
		stopTime = System.nanoTime();	
	}

	/**
	* Elapsed time in secods.
	* @return the time recorded on the stopwatch in seconds.
	*/
	public double time() {	
		return (stopTime - startTime) / NANOS_PER_SEC;	
	}

	public String toString() {   
		return "elapsed time: " + time() + " seconds.";
	}

	/**
	* Elapsed time in nanoseconds.
	* @return the time recorded on the stopwatch in nanoseconds.
	*/
	public long timeInNanoseconds() {	
		return (stopTime - startTime);	
	}
}

