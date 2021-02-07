package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast <T> implements Broadcast {
    private static int timeTicks =0;
    public TickBroadcast()
    {
        timeTicks++;
    }

    public int getTimeTicks()
    {
        return timeTicks;
    }
}
