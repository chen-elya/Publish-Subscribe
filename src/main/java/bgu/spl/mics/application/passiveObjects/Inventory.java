package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.application.Printer;
import java.util.LinkedList;
import java.util.List;

/**
 * That's where Q holds his gadget (e.g. an explosive pen was used in GoldenEye, a geiger counter in Dr. No, etc).
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {
    private List<String> gadgets;
    private static Inventory instance;//to implement thread-safe singleton
    // singleton part 1 of 3
    private static class SingletonHolder {
        private static final Inventory instance = new Inventory();

    }

    //singleton part 2 of 3
    private Inventory() {
        gadgets = new LinkedList<>();
    }


    /**
     * Retrieves the single instance of this class.
     */
    //To make a singleton class thread-safe, getInstance() method is made synchronized so
    //that multiple threads can’t access it simultaneously.

    //singleton part 3 of 3
    public static Inventory getInstance() {
        return SingletonHolder.instance;
    }


    /**
     * Initializes the inventory. This method adds all the items given to the gadget
     * inventory.
     * <p>
     *
     * @param inventory Data structure containing all data necessary for initialization
     *                  of the inventory.
     */

    public void load(String[] agents) {
        for (String str : agents) {
            gadgets.add(str);
        }
    }


    /**
     * acquires a gadget and returns 'true' if it exists.
     * <p>
     *
     * @param gadget Name of the gadget to check if available
     * @return ‘false’ if the gadget is missing, and ‘true’ otherwise
     */
    public boolean getItem(String gadget) {
        boolean found = false;
        //searches for the item in gadgets
        for (int i=0;i<gadgets.size();i++) {
            if (gadget.equals(gadgets.get(i))) {
                found = true;
            }
            if (found) // if we found the gadget, we need to remove
            {
                    gadgets.remove(gadget);
                }
            }
        return found;
    }

    /**
     * <p>
     * Prints to a file name @filename a serialized object List<String> which is a
     * list of all the of the gadgeds.
     * This method is called by the main method in order to generate the output.
     */
    public void printToFile(String filename) {
        Printer.print(this,filename);
}
}



