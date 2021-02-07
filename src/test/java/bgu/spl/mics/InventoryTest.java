package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {
    Inventory inv = Inventory.getInstance();

    @Test
    public void test() {
        getInstance();
        getItem();
        load();
        printToFile();
    }

    @Test
    void getInstance() {
        Object tmp_inv = Inventory.getInstance();
        assertNotNull(tmp_inv);
        assertTrue(tmp_inv instanceof Inventory);

        Object tmp_inv2 = Inventory.getInstance();
        assertNotNull(tmp_inv2);
        assertNotNull(tmp_inv2 instanceof Inventory);

        assertEquals(tmp_inv, tmp_inv2); // checks if inv and inv2 have the same values
        assertSame(tmp_inv, tmp_inv2); // checks if inv and inv2 are pointing on the same Inventory
        inv = (Inventory) tmp_inv;
    }

    private String[] getRndStrArr(String s) { //creates a random String
        Random rnd = new Random();
        String[] arr = new String[rnd.nextInt(10000)];

        arr[0] = "";
        for (int i = 1; i < arr.length; i++) {
            int start = rnd.nextInt(s.length()), end = rnd.nextInt(s.length());
            if (start > end) {
                int tmp = start;
                start = end;
                end = tmp;
            }
            arr[i] = s.substring(start, end);
        }
        return arr;
    }

    @Test
    void load() {
        loadExceptions();
        loadOper();
    }

    void loadOper() {
        String s = "neOQX^bbEa &HXf jd8Wo%mR Dv$pdmUWAr LW5#Suih ZB@cOg Y! SHV9c 1H T4y1IE,6K2T ,gSd,wF2CP yn<8kyYimQ5.b";
        String s2 = "0Q2hkMmXYo         KmI6T0dznF IoP1rAncJM IhQ8fqRrux PGVw9IWrGK RBEeHw7Y4e YEpaJFEoOH aK7dQzIYRW 8J8qbpBrzb NmNuzusQ8V";
        String[] gadgets = getRndStrArr(s);
        String[] BadGadgets = getRndStrArr(s2);

        inv.load(gadgets);
        //searches for all the gadgets we loaded to inventory
        for (String gadget : gadgets) {
            assertTrue(inv.getItem(gadget), "couldn't find loaded String - inserted: [" + Arrays.toString(gadgets) + "] searched:\"" + gadget + "\"");
        }
        //checks for all the gadgets we didn't load to inventory
        for (String bad : BadGadgets) {
            if (!Arrays.toString(gadgets).contains(bad)) //If the gadget belongs to Bad gadgets and DOESN'T belong to gadgets
                assertFalse(inv.getItem(bad), "FOUND BAD gadget - inserted: [" + Arrays.toString(gadgets) + "] and found: \"" + bad + "\"");
        }
    }

    void loadExceptions() {
        inv = Inventory.getInstance();
        assertDoesNotThrow(() -> {
            inv.load(null);
        }); //checks if it doesn't throw exception if the inv is null
        assertDoesNotThrow(() -> {
            inv.load(new String[0]);
        });//checks if it doesn't throw exception if the inv is empty
        assertDoesNotThrow(() -> {
            inv.load(new String[1]);
        });//checks if it doesn't throw exception if the inv has the size of 1
        assertDoesNotThrow(() -> {
            inv.load(new String[10]); //loads an empty array of strings of size 10 to inventory
            inv.getItem("aa"); // returns false but NO EXCEPTION so its' ok
        });
        String[] arr = {"a", "", null, "b"};
        inv.load(arr);
        //checks if getItem works
        assertTrue(inv.getItem("a"));
        assertTrue(inv.getItem(""));
        assertTrue(inv.getItem("b"));
        assertFalse(inv.getItem("c"));
    }

    @Test
    void getItem() {
        getItemExceptions();
        getItemOper();
    }

    void getItemExceptions() {
        inv = Inventory.getInstance();
        //checks that the getItem doesn't throw an Exception at any case
        assertDoesNotThrow(() -> {
            inv.getItem("a");
        });
        assertDoesNotThrow(() -> {
            inv.getItem("");
        });
        assertDoesNotThrow(() -> {
            inv.getItem(null);
        });
    }

    void getItemOper() {
        inv = Inventory.getInstance();
        assertFalse(inv.getItem("a"));
        String[] arr = {"a"};
        inv.load(arr);
        assertTrue(inv.getItem("a"));
    }


    @Test
    void printToFile() {
        inv = Inventory.getInstance();
        //checks that printtoFile doesn't throw Exceptions
        assertDoesNotThrow(() -> {
            inv.printToFile("a");
        });
        assertDoesNotThrow(() -> {
            inv.printToFile("");
        });
        assertDoesNotThrow(() -> {
            inv.printToFile(null);
        });
        assertDoesNotThrow(() -> {
            inv.printToFile("a.json");
        });
        File temp = new File("a.json"); //creates a File json
        assertTrue(temp.exists()); //checks if temp exists
    }
}