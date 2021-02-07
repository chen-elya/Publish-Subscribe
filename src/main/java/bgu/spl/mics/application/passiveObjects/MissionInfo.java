package bgu.spl.mics.application.passiveObjects;

import java.util.List;
/**
 * Passive data-object representing information about a mission.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class MissionInfo {
    private String missionName;
    private List<String> agentsSerialNumbers;
    private int duration;
    private int timeExpired;
    private String gadgetName;
    private int timeIssued;


    /**
     * Sets the name of the mission.
     */
    public void setMissionName(String missionName) {
                this.missionName = missionName;

    }

    /**
     * Retrieves the name of the mission.
     */
    public String getMissionName() {
        String s = null;
                s = missionName;
        return s;
    }

    /**
     * Sets the serial agent number.
     */
    public void setSerialAgentsNumbers(List<String> serialAgentsNumbers) {
                this.agentsSerialNumbers = serialAgentsNumbers;
    }

    /**
     * Retrieves the serial agent number.
     */
    public List<String> getSerialAgentsNumbers() {
        return this.agentsSerialNumbers;
    }

    /**
     * Sets the gadget name.
     */
    public void setGadget(String gadget) {
        this.gadgetName = gadget;
    }

    /**
     * Retrieves the gadget name.
     */
    public String getGadget() {
        return this.gadgetName;
    }

    /**
     * Sets the time the mission was issued in milliseconds.
     */
    public void setTimeIssued(int timeIssued) {
		this.timeIssued=timeIssued;
    }

    /**
     * Retrieves the time the mission was issued in milliseconds.
     */
    public int getTimeIssued() {
        int s = -1;
                s = timeIssued;
        return s;
    }


    /**
     * Sets the time that if it that time passed the mission should be aborted.
     */
    public void setTimeExpired(int timeExpired) {
                this.timeExpired = timeExpired;
    }

    /**
     * Retrieves the time that if it that time passed the mission should be aborted.
     */
    public int getTimeExpired() {
        int s = -1;
        s = timeExpired;
        return s;
    }

    /**
     * Sets the duration of the mission in time-ticks.
     */
    public void setDuration(int duration) {
                this.duration = duration;
    }

    /**
     * Retrieves the duration of the mission in time-ticks.
     */
    public int getDuration() {
        int s = -1;
                s = duration;
        return s;
    }
}
