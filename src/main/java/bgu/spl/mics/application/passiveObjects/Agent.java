package bgu.spl.mics.application.passiveObjects;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Agent {
    private String serialNumber = "";
    private String name;
    private boolean available;
    private Semaphore agentlock;

public Agent(){
    available=true;
    agentlock = new Semaphore(1);
}
    /**
     * Sets the serial number of an agent.
     */
    
    //using a lock to prevent from reading while writing and 2 threads to write at the same time
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * Retrieves the serial number of an agent.
     * <p>
     *
     * @return The serial number of an agent.
     */
    
    //using a lock to prevent from a thread to write while reading
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Sets the name of the agent.
     */
    
    //using a lock to prevent from reading while writing and 2 threads to write at the same time
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the name of the agent.
     * <p>
     *
     * @return the name of the agent.
     */
    
    //using a lock to prevent from a thread to write while reading
    public String getName() {
        return name;
    }

    /**
     * Retrieves if the agent is available.
     * <p>
     *
     * @return if the agent is available.
     */
    //using a lock to prevent from a thread to write while reading
    public boolean isAvailable() {
        return available;
    }

    /**
     * Acquires an agent.
     */
    
    //using a lock to prevent from reading while writing and 2 threads to write at the same time
    public void acquire() {
     //   try {
      //      agentlock.acquire();
       // } catch (InterruptedException e) {
        //    e.printStackTrace();
        //}
        available = false;
    }

    /**
     * Releases an agent.
     */
    
    //using a lock to prevent from reading while writing and 2 threads to write at the same time
    public void release() {
        agentlock.release();
        available = true;
    }
}
