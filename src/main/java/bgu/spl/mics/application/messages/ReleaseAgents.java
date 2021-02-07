package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;


public class ReleaseAgents implements Event {
    private List<String> agents;

    public ReleaseAgents(List<String> agents) {
        this.agents = agents;
    }

    public List<String> getAgents() {
        return agents;
    }
}

