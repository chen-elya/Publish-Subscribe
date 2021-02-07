package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TickBroadcastTerminate;
import static java.lang.Thread.sleep;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link Tick Broadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
	private int termination = 0;

	public TimeService(int duration) {
		super("Time Service");
		termination = duration;
	}

	@Override
	protected void initialize() {
	}



	@Override
	public void run() {
			// TODO Implement this
			//checks if we are before the termination
			while (termination > 0) {
				TickBroadcast tick = new TickBroadcast();
				getSimplePublisher().sendBroadcast(tick); //send the timetick
				try {
					sleep(100); //go to sleep for 0.1 seconds
					termination--;
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}

		 getSimplePublisher().sendBroadcast(new TickBroadcastTerminate<>()); //terminating the program
	}

}



