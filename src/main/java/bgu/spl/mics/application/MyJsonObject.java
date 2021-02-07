package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Agent;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MyJsonObject {
    String[] inventory;
    Agent[] squad;
    MyJsonServices services;

    public class MyJsonGadget {
        String name;
    }

    public class MyJsonServices {
        MyJsonIntelligence[] intelligence;
        int M;
        int Moneypenny;
        int time;

        class MyJsonIntelligence {
            MyJsonMission[] missions;

            class MyJsonMission {
                public String name;
                public String[] serialAgentsNumbers;
                public int duration;
                public int timeExpired;
                public String gadget;
                public int timeIssued;
            }
        }
    }
}
