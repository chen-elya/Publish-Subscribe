package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.application.Printer;

import java.util.LinkedList;
import java.util.List;

/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Diary {
    private List<Report> reports;
    private int total;
    //This is our singleton variable, when we put it as null, when we will initialize it, we will change it from null

    // singleton part 1 of 3
    private static class SingletonHolder {
        private static final Diary instance = new Diary();
    }

    //singleton part 2 of 3
    private Diary() {
        reports = new LinkedList<>();
        total = 0;
    }

    /**
     * Retrieves the single instance of this class.
     */
    ////////////check which syn kind is the best for this class
    //To make a singleton class thread-safe, getInstance() method is made synchronized so
    //that multiple threads can’t access it simultaneously.

    //singleton part 3 of 3
    public static Diary getInstance() {
        return Diary.SingletonHolder.instance;
    }

    public List<Report> getReports() {
        return this.reports;
    }

    /**
     * adds a report to the diary
     *
     * @param reportToAdd - the report to add
     */
    public void addReport(Report reportToAdd) {
        synchronized (reports) {
            reports.add(reportToAdd);
            incrementTotal();
        }
    }

    /**
     * <p>
     * Prints to a file name @filename a serialized object List<Report> which is a
     * List of all the reports in the diary.
     * This method is called by the main method in order to generate the output.
     */
    public void printToFile(String filename) {
        Printer.print(this,filename);
    }

    /**
     * Gets the total number of received missions (executed / aborted) be all the M-instances.
     *
     * @return the total number of received missions (executed / aborted) be all the M-instances.
     */
    public int getTotal() {
        return total;
    }

    /**
     * Increments the total number of received missions by 1
     */
    public void incrementTotal() {
        synchronized (this) {
            total = total + 1;
        }
    }
}
