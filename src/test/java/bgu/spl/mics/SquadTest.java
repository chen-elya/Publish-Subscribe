package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;

public class SquadTest {
    Squad squad;

    @BeforeEach
    public void setUp() throws Exception {
        squad = Squad.getInstance();
    }

    @Test
    public void TestgetInstance() {
        Object tmp_squad = Squad.getInstance();
        assertNotNull(tmp_squad);
        assertTrue(tmp_squad instanceof Squad);

        Object tmp_squad2 = Squad.getInstance();
        assertNotNull(tmp_squad2);
        assertNotNull(tmp_squad2 instanceof Squad);

        assertEquals(tmp_squad2, tmp_squad); // checks if inv and inv2 have the same values
        assertSame(tmp_squad2, tmp_squad); // checks if inv and inv2 are pointing on the same Inventory
        squad = (Squad) tmp_squad;
    }

    @Test
    public void Testload() {
        loadExceptions();
        loadOper();
    }

    void loadOper() {
        LinkedList<String> goodnumbers = new LinkedList<String>();
        LinkedList<String> badnumbers = new LinkedList<String>();
        LinkedList<String> temp = new LinkedList<String>();
        temp.add(" "); //will be deleted when checking for bad numbers
        Agent[] good = new Agent[10];
        Agent[] bad = new Agent[10];
        for (int i = 0; i < 10; i++) {
            good[i] = new Agent(); //creating "good" agents
            bad[i] = new Agent(); // creating "bad" agents
            goodnumbers.add(good[i].getSerialNumber());
            badnumbers.add(bad[i].getSerialNumber());
        }
        squad.load(good);
        //searches for all the good agents we loaded to squad (only good agents were loaded to squad)
        assertTrue(squad.getAgents(goodnumbers), "couldn't find all the Agents requested");

        //going over the bad
        for (int i = 0; i < badnumbers.size(); i++) {
            temp.remove(0); //removing the last bad we checked, (if it is the first one we are removing the empty string we defined above
            temp.add(bad[i].getSerialNumber()); //we create a linked list in the size of 1 to check only 1 bad agent each time
            if (!goodnumbers.contains(temp.getFirst())) { //checking that the bad agent is not a good agent aswell
                //checking if the bad agent is in the squad and sending an error if so
                assertFalse(squad.getAgents(temp), "FOUND BAD serial number - inserted: [" + bad[i].getSerialNumber() + "]");
            }
        }
    }

    void loadExceptions() {
        squad = Squad.getInstance();
        assertDoesNotThrow(() -> {
            squad.load(null);
        }); //checks if it doesn't throw exception if the squad is null
        assertDoesNotThrow(() -> {
            squad.load(new Agent[0]);
        });//checks if it doesn't throw exception if the inv is empty
        assertDoesNotThrow(() -> {
            squad.load(new Agent[1]);
        });//checks if it doesn't throw exception if the inv has the size of 1

        LinkedList<String> agent = new LinkedList<String>();
        agent.add("007");
        assertDoesNotThrow(() -> {
            squad.load(new Agent[10]); //loads an empty array of strings of size 10 to inventory
            squad.getAgents(agent); // returns false but NO EXCEPTION so its' ok
        });
        //we will make 4 different agents and get there serial number
        Agent agent1 = new Agent();
        String agent1Name = agent1.getSerialNumber();
        Agent agent2 = new Agent();
        String agent2Name = agent2.getSerialNumber();
        Agent agent3 = new Agent();
        String agent3Name = agent3.getSerialNumber();
        Agent agent4 = new Agent();
        String agent4Name = agent4.getSerialNumber();
        agent.removeFirst();//our agent list was not empty
        agent.add(agent1Name);
        agent.add(agent2Name);
        agent.add(agent3Name);
        agent.add(agent4Name);

        Agent[] arr = {agent1, agent2, agent3, agent4};
        squad.load(arr);
        //checks if getAgents works
        assertTrue(squad.getAgents(agent));

    }

    @Test
    public void TestreleaseAgents() {
        LinkedList<String> agent = new <String>LinkedList();
        Agent agent1 = new Agent();
        String agent1Name = agent1.getSerialNumber();
        Agent agent2 = new Agent();
        String agent2Name = agent2.getSerialNumber();
        Agent agent3 = new Agent();
        String agent3Name = agent3.getSerialNumber();
        Agent agent4 = new Agent();
        String agent4Name = agent4.getSerialNumber();
        agent.add(agent1Name);
        agent.add(agent2Name);
        agent.add(agent3Name);
        agent.add(agent4Name);
        Agent[] arr = {agent1, agent2, agent3, agent4};
        squad.load(arr);
        squad.releaseAgents(agent);
        assertFalse(squad.getAgents(agent), "didn't release all of the agents"); //gives an error if we didn't release all
    }

    @Test
    public void TestsendAgents() {
        int time = 3000; //sets the time of the operation to 3 seconds
        LinkedList<String> agent = new <String>LinkedList();
        Agent agent1 = new Agent();
        String agent1Name = agent1.getSerialNumber();
        Agent agent2 = new Agent();
        String agent2Name = agent2.getSerialNumber();
        Agent agent3 = new Agent();
        String agent3Name = agent3.getSerialNumber();
        Agent agent4 = new Agent();
        String agent4Name = agent4.getSerialNumber();
        agent.add(agent1Name);
        agent.add(agent2Name);
        agent.add(agent3Name);
        agent.add(agent4Name);
        Agent[] arr = {agent1, agent2, agent3, agent4};
        squad.load(arr);
        long start = System.currentTimeMillis(); //saves the current time
        squad.sendAgents(agent, time);
        long end = System.currentTimeMillis(); // saves the current time (should be 3 seconds after start since time = 3000
        assertTrue(end - start == 3000, "It didn't take 3 seconds (more/less)"); //checks if it really took 3 seconds
        boolean noAgents = squad.getAgents(agent); //checks if we released all the agents
        assertFalse(noAgents, "didn't release all of the agents"); //gives an error if we didn't release all
    }

    @Test
    public void TestgetAgents() {
        LinkedList<String> temp = new LinkedList<String>();
        temp.add("");
        assertFalse(squad.getAgents(temp), "Found an agent without a serial number");
        temp.removeFirst();
        Agent agent = new Agent();
        Agent agent2 = new Agent();
        //We insert 2 Agents to search in the squad BUT we add only one into the squad
        //After we check if we get False, otherwise the program thinks we found both agents but we actually inserted only one
        temp.add(agent.getSerialNumber());
        temp.add(agent2.getSerialNumber());
        Agent[] arr = {agent};
        squad.load(arr);
        assertFalse(squad.getAgents(temp), "The program thinks we found all but someone is missing");  // The check
        Agent[] arr2 = {agent2};
        squad.load(arr2);//inserting the second agent to the squad
        assertTrue(squad.getAgents(temp), "The program didn't find all the agents");//checks if all the squad is here

    }

    @Test
    public void TestgetAgentsNames() {
        LinkedList<String> agent = new <String>LinkedList();
        Agent agent1 = new Agent();
        String agent1Name = agent1.getSerialNumber();
        Agent agent2 = new Agent();
        String agent2Name = agent2.getSerialNumber();
        Agent agent3 = new Agent();
        String agent3Name = agent3.getSerialNumber();
        Agent agent4 = new Agent();
        String agent4Name = agent4.getSerialNumber();
        agent.add(agent1Name);
        agent.add(agent2Name);
        agent.add(agent3Name);
        agent.add(agent4Name);
        Agent[] arr = {agent1, agent2, agent3, agent4};
        squad.load(arr);
        List<String> names = squad.getAgentsNames(agent); //converts serial numbers to names
        for (int t = 0; t < arr.length; t++) //checks in the agents array if we found all the agents names
        {
            assertTrue(names.contains(arr[t].getName()), "We didn't find the name:" + arr[t].getName());
        }

    }
}
