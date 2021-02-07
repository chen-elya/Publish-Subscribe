package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;

import java.util.List;


public class AgentsAvailableEvent implements Event<Object[]> {
    private List<String> agents;
    private Future<Integer> future;

    public AgentsAvailableEvent(List<String> agents, Future<Integer> future) {
        this.agents = agents;
        this.future = future;
    }

    public List<String> getSerials() {
        return agents;
    }

    public Future<Integer> getFuture() {
        return future;
    }
}






