package bgu.spl.mics.application;

import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {
        LinkedList<Thread> threads = new LinkedList<>();
        LinkedList<Subscriber> services = new LinkedList<>();
        try {
            //region initialize
            int id = 0;
            //Json
            FileReader reader = new FileReader(args[0]);
            //Inventory
            Gson gson = new Gson();
            MyJsonObject obj = (MyJsonObject) (gson.fromJson(reader, MyJsonObject.class));
            LinkedList<Subscriber> subscribers = new LinkedList<>();
            //endregion
            //region Inventory
            Inventory inventory = Inventory.getInstance();
            String[] gadgetsNames = new String[obj.inventory.length];
            String[] myJsonGadgets = obj.inventory;
            for (int i = 0; i < myJsonGadgets.length; i++) {
                gadgetsNames[i] = myJsonGadgets[i];
            }
            inventory.load(gadgetsNames);
            //endregion Inventory
            //region Squad
            Squad.getInstance().load(obj.squad);
            //endregion
            //region Intelligences
            MyJsonObject.MyJsonServices.MyJsonIntelligence[] js_ints = obj.services.intelligence;

            for (MyJsonObject.MyJsonServices.MyJsonIntelligence js_int : js_ints) {
                List<MissionInfo> missions = new LinkedList<>();
                MyJsonObject.MyJsonServices.MyJsonIntelligence.MyJsonMission[] js_miss = js_int.missions;
                for (MyJsonObject.MyJsonServices.MyJsonIntelligence.MyJsonMission jsMiss : js_miss) {
                    MissionInfo mi = new MissionInfo();
                    mi.setTimeIssued(jsMiss.timeIssued);
                    mi.setTimeExpired(jsMiss.timeExpired);
                    mi.setMissionName(jsMiss.name);
                    mi.setDuration(jsMiss.duration);
                    mi.setGadget(jsMiss.gadget);
                    String[] agents = jsMiss.serialAgentsNumbers;
                    LinkedList<String> anames = new LinkedList<>();
                    if (agents != null)
                        for (int i = 0; i < agents.length; i++)
                            anames.add(agents[i]);
//                    else
//                        anames.add(jsMiss.agent);
                    mi.setSerialAgentsNumbers(anames);

                    missions.add(mi);
                }

                subscribers.add(new Intelligence("Intelligence" + id++, missions));
            }
            //endregion
            //region Q
            subscribers.add(new Q());
            //endregion
            //region M
            for (int i = 0; i < obj.services.M; i++)
                subscribers.add(new M("M" + id++));
            //endregion
            //region Monneypenny
            for (int i = 0; i < obj.services.Moneypenny; i++)
                subscribers.add(new Moneypenny("Moneypenny" + id++));
            //endregion
            int i = 0;
            //region run threads
            for (Subscriber subscriber : subscribers)
                threads.add(new Thread(subscriber, subscriber.getName()));
            for (Thread thread : threads)
                thread.start();
            //endregion
            /*
            to wait until they all passed {@code: initialize} called in all subscribers

             */
            //region run TimeService
            Thread.sleep(1000);//todo: ask yuval
            TimeService clock = new TimeService(obj.services.time);
            Thread threadClock = new Thread(clock, "##clock##");
            threadClock.start();

            //endregion
            //region join them all!!
            threadClock.join();
            for (Thread thread : threads)
                thread.join();
            Inventory.getInstance().printToFile(args[1]);
            Diary.getInstance().printToFile(args[2]);

            //endregion
        } catch (FileNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}



