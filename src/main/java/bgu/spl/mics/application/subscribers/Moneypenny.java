package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Event;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Only this type of Subscriber can access the squad.
 * There are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
    private int time;
    private int id1;

    public Moneypenny(String id) {
        super(id);
        id1 = Integer.parseInt(id.substring(10));
        this.time = 0;
    }

    @Override
    protected void initialize() {
//        this.run(); //We run in order to register and wait for a proper message
        // Defines the Callback of AgentAvilableEvent
        subscribeEvent(AgentsAvailableEvent.class, (e) -> {
            // whatever you want to happen when AgentsAvailableEvent Event is received
            boolean didGood = Squad.getInstance().getAgents(e.getSerials()); //checks if we succeeded or not
            int moneypenny = didGood ? id1 : -1;
            Object[] result = new Object[]{moneypenny, Squad.getInstance().getAgentsNames(e.getSerials())};
            complete(e, result); //resolves the future object accordingly
            int timeOfMission = e.getFuture().get();
            if (timeOfMission >= 0)
                Squad.getInstance().sendAgents(e.getSerials(), timeOfMission);
            else if (didGood)
                Squad.getInstance().releaseAgents(e.getSerials());
        });
        subscribeEvent(SendAgentEvent.class, (d) -> {
            // whatever you want to happen when SendAgentEvent Event is received
            Squad.getInstance().sendAgents(d.getSerials(), d.getTime());
            complete(d, time);

        });
        subscribeEvent(ReleaseAgents.class, (c) -> {
            // whatever you want to happen when ReleaseAgents Event is received
            Squad.getInstance().releaseAgents(c.getAgents());
            complete(c, true);
        });
        subscribeBroadcast(TickBroadcast.class, (b) -> {
            //When we get a TickBroadcast we save the time for Moneypenny
            time = b.getTimeTicks();
        });
        subscribeBroadcast(TickBroadcastTerminate.class, (b) -> {
            //When we get a TickBroadcast we save the time for Moneypenny
            terminate();
        });


    }
}
