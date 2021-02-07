package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class MissionReceivedEvent implements Event<Void> {
    private List <String> agentList;
    private String gadget;
    private int timeExpired;
    private int timeIssued;
    private int duration;
    private String missionName;
    public MissionReceivedEvent (List <String> agentList,String gadget,int timeExpired,int timeIssued,int duration,String missionName)
    {
     this.agentList = agentList;
     this.gadget  = gadget;
     this.timeExpired = timeExpired;
     this.timeIssued = timeIssued;
     this.duration = duration;
     this.missionName = missionName;
    }
    public String getGadget()
    {
        return gadget;
    }
    public List <String> getAgentList()
    {
        return agentList;
    }

    public int getDuration() {
        return duration;
    }

    public int getTimeIssued() {
        return timeIssued;
    }

    public int getTimeExpired() {
        return timeExpired;
    }

    public String getMissionName() {
        return missionName;
    }
}
