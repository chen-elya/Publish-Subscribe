package bgu.spl.mics.application.subscribers;
import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Report;
import java.util.LinkedList;
import java.util.List;


/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
    private List<String> serials;
    private List<String> names;
    private int time;
	private String id;
    private int moneypenny;
    private int Qtime;

    public M(String id) {
        super(id);
        this.id = id;
        this.serials = new LinkedList<String>();
        this.names = new LinkedList<String>();
        this.time = 0;
		this.moneypenny = -1;
        this.Qtime = -1;

    }

    @Override
    protected void initialize() {
        //this.run(); //We run in order to register and wait for a proper message
		// Defines the Callback of AgentAvilableEvent
        subscribeEvent(MissionReceivedEvent.class, (e) -> {
            // When we receive MissionReceivedEvent we create a AgentsAvailableEven
			Future<Integer> futureToSend=new Future<>();
            AgentsAvailableEvent event = new AgentsAvailableEvent(e.getAgentList(),futureToSend);
            Future<Object[]> future = getSimplePublisher().sendEvent(event); //we send the AgentsAvailableEvent
            Integer M1 = (Integer)future.get()[0]; //Returns the number of Moneypenny handling the event (-1 = event failed)
            if (M1!=null && M1 != -1) { //If the call was succeeded
                names = (LinkedList) future.get()[1];
                serials = e.getAgentList();
                moneypenny = M1;
                GadgetAvailableEvent gevent = new GadgetAvailableEvent(e.getGadget(), time); //we create a GadgetAvailableEvent
                Future<Integer> future2 = getSimplePublisher().sendEvent(gevent);//we send the GadgetAvailableEvent
                if (future2!=null && future2.get()!= -1) { //If the call was succeeded
                    Qtime = time;
                    if (e.getTimeExpired() > Qtime) {
                        futureToSend.resolve(e.getDuration());
                        SendAgentEvent send = new SendAgentEvent(serials, e.getDuration());
                        Future future3 = getSimplePublisher().sendEvent(send);
                        complete(e, null);
                        Report report = new Report();
                        report.setAgentsNames(names);
                        report.setAgentsSerialNumbersNumber(serials);
                        report.setGadgetName(e.getGadget());
                        report.setMissionName(e.getMissionName());
                        report.setM(Integer.parseInt(id.substring(1, id.length())));
                        report.setMoneypenny(moneypenny);
                        report.setQTime(Qtime);
                        report.setTimeIssued(e.getTimeIssued());
                        report.setTimeCreated(time);
                        Diary.getInstance().addReport(report);
                        return;
                    }
                }
            }
			futureToSend.resolve(-1);
            Diary.getInstance().incrementTotal();

            if(M1==null) {
                terminate();
            }
        });
        subscribeBroadcast(TickBroadcast.class, (b) -> {
            //When we get a TickBroadcast we save the time for M
            time = b.getTimeTicks();
        });
        subscribeBroadcast(TickBroadcastTerminate.class, (b) -> {
            //When we get a TickBroadcast we save the time for Moneypenny
            terminate();
        });
    }
}






