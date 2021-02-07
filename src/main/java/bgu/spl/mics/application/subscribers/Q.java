package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TickBroadcastTerminate;
import bgu.spl.mics.application.passiveObjects.Inventory;



/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
	//NOT SATISFIED WITH THIS!!
	private int time;
	public Q() {
		super("Q");
		this.time=-1;
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	@Override
	protected void initialize() {
		// TODO Implement this
		//this.run(); //We run in order to register and wait for a proper message
		// Defines the Callback of AgentAvilableEvent
		subscribeEvent(GadgetAvailableEvent.class,(e)->{
			// whatever you want to happen when SomeEvent is received
			boolean result = Inventory.getInstance().getItem(e.getGadget());
			if (result) {
				complete(e, time); //resolves the future object accordingly
			}
			else
			{
				complete(e,-1);
			}

		});
		subscribeBroadcast(TickBroadcast.class,(b)-> {
			//When we get a TickBroadcast we save the time for Moneypenny
			time = b.getTimeTicks();
		});
		subscribeBroadcast(TickBroadcastTerminate.class,(b)-> {
			//When we get a TickBroadcast we save the time for Moneypenny
			terminate();
		});

		// under correct implementation of this.subscribeEvent, callbackName.call will be called when
		// AgentAvailableEvent is received.
//		this.subscribeEvent(GadgetAvailableEvent.class, callbackName);

	}


}
