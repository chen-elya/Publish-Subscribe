package bgu.spl.mics.application.passiveObjects;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

    private Map<String, Agent> agents;
    private static Squad instance;//to implement thread-safe singleton

    // singleton part 1 of 3
    private static class SingletonHolder {
        private static final Squad instance = new Squad();
    }

    //singleton part 2 of 3
    private Squad() {
        agents = new ConcurrentHashMap<>();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static Squad getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Initializes the squad. This method adds all the agents to the squad.
     * <p>
     *
     * @param agents Data structure containing all data necessary for initialization
     *               of the squad.
     */
    public void load(Agent[] agents) {
        this.agents = new HashMap<>();
        for (Agent agent : agents)
            this.agents.put(agent.getSerialNumber(), agent);
    }


    /**
     * Releases agents.
     */
    //If 2 threads will try to release an agent it will be a problem because one will change it to available status
    //and the other one to unavailable ->it's not true
    public void releaseAgents(List<String> serials) {
        for (String serialNum : serials)//go through the list
        {
            if (agents.containsKey(serialNum) == true)//check if this agent in the squad
            {
                Agent agent = agents.get(serialNum);
                agent.release();//change agent available to true in case mission is aborted
                synchronized (agent) {
                    agent.notifyAll();
                }
            }
        }
    }

    /**
     * simulates executing a mission by calling sleep.
     *
     * @param time milliseconds to sleep
     */
    public void sendAgents(List<String> serials, int time) {
        try {
            Thread.sleep(time*100);
        } catch (InterruptedException ignored) {
        }
        releaseAgents(serials);
    }

    /**
     * acquires an agent, i.e. holds the agent until the caller is done with it
     *
     * @param serials the serial numbers of the agents
     * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
     */
    //we added synchronized
    public synchronized boolean getAgents(List<String> serials) {
        if (!agents.keySet().containsAll(serials))
            return false;
        serials.sort(Comparator.naturalOrder());

        for (String serial : serials)//go through the agents and change there available
        {
            Agent agent = agents.get(serial);
            agent.acquire();
            // If an agent is in the Squad, but is already acquired for some
            //other mission, the function will wait until the agent becomes available.
//            try {
//                synchronized (agent) {
//                    while (!agent.isAvailable())
//                    {
//                        agent.wait();
//                    }
//                    agent.acquire();
//                }
//            } catch (Exception e) {
//            }
        }
        return true;

    }


    /**
     * gets the agents names
     *
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials) {
        boolean found = false;
        List<String> listAgentsName = new LinkedList<>();//list of agents name
        for (String serialNum : serials)//go through the agents list (the first coordinate)
        {
            if (agents.containsKey(serialNum) == true) {
                found = true;
                Agent agent = agents.get(serialNum);
                if (found)
                    listAgentsName.add(agent.getName());//add all the name of the relevant agent to the list
            }
        }
        return listAgentsName;
    }


}
