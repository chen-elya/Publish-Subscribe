package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TickBroadcastTerminate;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A Publisher only.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
	List<MissionInfo> missions;
	public Intelligence (String serialnumber, Collection<MissionInfo> missions)
	{
		super(serialnumber);
		this.missions =new LinkedList<>(missions);
	}

	@Override
	protected void initialize() {
		//this.run();
		Callback<TickBroadcast> callbackName = e -> {
			for(MissionInfo info : missions) {
				if (e.getTimeTicks() == info.getTimeIssued()) {
					List<String> numbers = info.getSerialAgentsNumbers();
					String gadget = info.getGadget();
					int timeExp = info.getTimeExpired();
					int gettime = info.getTimeIssued();
					int duration = info.getDuration();
					String missionName = info.getMissionName();
					MissionReceivedEvent event = new MissionReceivedEvent(info.getSerialAgentsNumbers(), info.getGadget(),
							info.getTimeExpired(), info.getTimeIssued(), info.getDuration(), info.getMissionName());
					 getSimplePublisher().sendEvent(event);
				}

			}
		};
		subscribeBroadcast(TickBroadcast.class,callbackName);
		subscribeBroadcast(TickBroadcastTerminate.class,(b)-> {
			//When we get a TickBroadcast we save the time for Moneypenny
			terminate();
		});
	}

}
