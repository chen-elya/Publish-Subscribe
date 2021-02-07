//package bgu.spl.mics;
//
//import bgu.spl.mics.application.messages.*;
//import bgu.spl.mics.application.subscribers.Intelligence;
//import bgu.spl.mics.application.publishers.TimeService;
//import bgu.spl.mics.application.subscribers.M;
//import bgu.spl.mics.application.subscribers.Moneypenny;
//import bgu.spl.mics.application.subscribers.Q;
//import bgu.spl.mics.example.messages.ExampleEvent;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class MessageBrokerTest {
//    MessageBroker msg = MessageBrokerImpl.getInstance();
//    M m;
//    Q q;
//    Class<ExampleEvent> e1;
//    Event<ExampleEvent> e2;
//    Callback<ExampleEvent> callback;
//    Moneypenny moneypenny;
//    Intelligence intelligence;
//    TimeService ts;
//
//    @BeforeEach
//    public void setUp() throws Exception {
//        M m = new M();
//        Moneypenny moneypenny = new Moneypenny();
//        Q q = new Q();
//        intelligence = new Intelligence();
//        ts = new TimeService();
//        msg.register(m);
//        msg.register(q);
//        msg.register(moneypenny);
//    }
//
//    @Test
//    public void test() {
//        //TODO: change this test and add more tests :)
//        fail("Not a good test");
//    }
//
//    @Test
//    public void testsubscribeEvent() {
//        //GadgetAvailableEvent.class = gets the type of the class (GadgetAvailableEvent)
//        //Checks that we don't an Exception when we subscribe the event to the correct user
//        assertDoesNotThrow(() -> {
//            msg.subscribeEvent(GadgetAvailableEvent.class, q);
//        }, "Threw an Exception when it shouldn't");
//        assertDoesNotThrow(() -> {
//            msg.subscribeEvent(MissionReceivedEvent.class, m);
//        }, "Threw an Exception when it shouldn't");
//        assertDoesNotThrow(() -> {
//            msg.subscribeEvent(AgentsAvailableEvent.class, moneypenny);
//        }, "Threw an Exception when it shouldn't");
//    }
//
//    @Test
//    public void testsubscribeBroadcast() {
//        //GadgetAvailableEvent.class = gets the type of the class (GadgetAvailableEvent)
//        //Checks that we don't an Exception when we subscribe the event to the correct user
//        assertDoesNotThrow(() -> {
//            msg.subscribeBroadcast(GadgetAvailableEventBroadcast.class, q);
//        }, "Threw an Exception when it shouldn't");
//        assertDoesNotThrow(() -> {
//            msg.subscribeBroadcast(MissionReceivedEventBroadcast.class, m);
//        }, "Threw an Exception when it shouldn't");
//        assertDoesNotThrow(() -> {
//            msg.subscribeBroadcast(AgentsAvailableEventBroadcast.class, moneypenny);
//        }, "Threw an Exception when it shouldn't");
//    }
//
//
//    @Test
//    public void testComplete() {
//        String result = "";
//        GadgetAvailableEvent event1 = new GadgetAvailableEvent();
//        assertDoesNotThrow(() -> {
//            msg.complete(event1, result);
//        }, "Threw an Exception when it shouldn't");
//    }
//
//    @Test
//    public void testsendBroadcast() {
//        //creates broadcasts
//        AgentsAvailableEventBroadcast b1 = new AgentsAvailableEventBroadcast();
//        GadgetAvailableEventBroadcast b2 = new GadgetAvailableEventBroadcast();
//        MissionReceivedEventBroadcast b3 = new MissionReceivedEventBroadcast();
//        //checks if the broadcasts don't throw exception
//        assertDoesNotThrow(() -> {
//            msg.sendBroadcast(b1);
//        }, "Failed broadcasting");
//        assertDoesNotThrow(() -> {
//            msg.sendBroadcast(b2);
//        }, "Failed broadcasting");
//        assertDoesNotThrow(() -> {
//            msg.sendBroadcast(b3);
//        }, "Failed broadcasting");
//    }
//
//
//    @Test
//    public void testSendEvent() {
//        GadgetAvailableEvent event1 = new GadgetAvailableEvent();
//        Future<String> future1 = msg.sendEvent(event1);
//        assertNotNull(future1, " Get null, although we shouldn't get null");//check it's not null, someone using it
//        msg.unregister(q);//now we unregister q
//        future1 = msg.sendEvent(event1);
//        assertNull(future1, "Did not get null, although we should get null");// we expext to get null because nobody in the register list
//
//    }
//
//
//    @Test
//    public void testRegister() {
//        assertDoesNotThrow(() -> {
//            msg.register(m);
//        }, "Failed registration");
//        assertDoesNotThrow(() -> {
//            msg.register(q);
//        }, "Failed registration");
//        assertDoesNotThrow(() -> {
//            msg.register(moneypenny);
//        }, "Failed registration");
//    }
//
//
//    @Test
//    public void testUnregister() {
//        msg.unregister(q);
//        GadgetAvailableEvent event1 = new GadgetAvailableEvent();
//        AgentsAvailableEvent event2 = new AgentsAvailableEvent();
//        MissionReceivedEvent event3 = new MissionReceivedEvent();
//        Future<String> future1 = msg.sendEvent(event1);
//        assertNull(future1, " Get null, although we shouldn't get null");//check it's not null, someone using it}
//        msg.unregister(moneypenny);
//        future1 = msg.sendEvent(event2);
//        assertNull(future1, " Get null, although we shouldn't get null");//check it's not null, someone using it}
//        msg.unregister(m);
//        future1 = msg.sendEvent(event3);
//        assertNull(future1, " Get null, although we shouldn't get null");//check it's not null, someone using it}
//    }
//
//    @Test
//    public void testAwaitMessage() throws InterruptedException {
//        assertNull(msg.awaitMessage(m), "there are no events it's suppose to be null");
//        assertNull(msg.awaitMessage(q), "there are no events it's suppose to be null");
//        assertNull(msg.awaitMessage(moneypenny), "there are no events it's suppose to be null");
//        GadgetAvailableEvent event1 = new GadgetAvailableEvent();
//        assertNotNull(msg.awaitMessage(m), "there are events it's not suppose to be null");
//        MissionReceivedEvent event3 = new MissionReceivedEvent();
//        assertNotNull(msg.awaitMessage(q), "there are events it's not suppose to be null");
//        AgentsAvailableEvent event2 = new AgentsAvailableEvent();
//        assertNotNull(msg.awaitMessage(moneypenny), "there are events it's not suppose to be null");
//    }
//}