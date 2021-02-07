package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;

public class GadgetAvailableEvent implements Event<Integer> {
    private String gadget;
    private int time;
    private int Q;


    public GadgetAvailableEvent(String gadget, int time) {
        this.gadget = gadget;
        this.time = time;
        this.Q = -1;
    }

    public int getTime() {
        return time;
    }

    public String getGadget() {
        return gadget;
    }

}