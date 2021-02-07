package bgu.spl.mics;

import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcastTerminate;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import bgu.spl.mics.example.messages.ExampleEvent;

import java.util.Map;
import java.util.concurrent.*;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
    //Not sure why we used blocking queue
    private ConcurrentHashMap<Subscriber, BlockingQueue<Message>> subMap;
    //categorizes each subscriber to the relevant event (queue)
    private ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<Subscriber>> messageMap;
    //A map that contains the events and the futures that are related to them
    private ConcurrentHashMap<Event, Future> resultMap;

    /**
     * Retrieves the single instance of this class.
     */
    // singleton part 1 of 3
    private static class SingletonHolder {
        private static final MessageBrokerImpl instance = new MessageBrokerImpl();
    }

    //singleton part 2 of 3
    private MessageBrokerImpl() {
        subMap = new ConcurrentHashMap<>();
        messageMap = new ConcurrentHashMap<>();
        resultMap = new ConcurrentHashMap<>();
    }

    /**
     * Retrieves the single instance of this class.
     */
    ////////////check which syn kind is the best for this class
    //To make a singleton class thread-safe, getInstance() method is made synchronized so
    //that multiple threads can’t access it simultaneously.

    //singleton part 3 of 3
    public static MessageBrokerImpl getInstance() {
        return MessageBrokerImpl.SingletonHolder.instance;
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
        subscribeMessage(type, m);
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
        subscribeMessage(type, m);
    }

    //Private method which subscribes a broadcast/event
    private void subscribeMessage(Class<? extends Message> type, Subscriber m) {
        messageMap.putIfAbsent(type, new ConcurrentLinkedQueue<>()); //if there is no such Queue type. create one
        messageMap.get(type).add(m);
    }

    @Override
    public <T> void complete(Event<T> e, T result) {
        Future<T> future = resultMap.get(e);
        future.resolve(result);
    }

    @Override
    public void sendBroadcast(Broadcast b) {
        ConcurrentLinkedQueue<Subscriber> list = messageMap.get(b.getClass());
        for (Subscriber subscriber : list) {
            subMap.get(subscriber).add(b);
        }
    }


    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        Future<T> future = new Future<>();
        resultMap.putIfAbsent(e, future);
        Subscriber mb;
        ConcurrentLinkedQueue<Subscriber> mb1 = messageMap.get(e.getClass()); //gets the queue of the subscribers of the specific event/broadcast
        if (mb1 == null)
            return null;

        //We used synch because we intend to remove and add a subscriber to the relevant queue
        synchronized (e.getClass()) {
            if (mb1.isEmpty())
                return null;
            mb = mb1.poll();
            mb1.add(mb);
        }

        //assert mb != null; //checking if the subscriber we want to add a mission to is null
        //We used synch: becuase we want to add a mission to a specific subscriber
        synchronized (mb) {
            BlockingQueue<Message> mb12 = subMap.get(mb);
            if (mb12 == null) // checking if the queue of missions of the subscriber exists
                return null;
            mb12.add(e);
        }
        return future;
    }

    @Override
    public void register(Subscriber m) {
        subMap.put(m, new LinkedBlockingDeque<>());
    }


    @Override
    public void unregister(Subscriber m) {
        BlockingQueue<Message> tasks;
        //We used synch on m specifically because we don't want any thread to have access to it when we remove it
        // from the subMap (such as trying to add a mission to it when we remove this subscriber)
        for (Map.Entry<Class<? extends Message>, ConcurrentLinkedQueue<Subscriber>> entry : messageMap.entrySet()) {
            synchronized (entry.getKey()) {
                entry.getValue().remove(m);
            }
        }
        synchronized (m) {
            tasks = subMap.remove(m);// takes the tasks queue of the subscriber m
        }
        // checking if the queue of m exists and if it is not empty
        //The while loop iterates on all the events/broadcasts of m and removes them
        while (tasks != null && !tasks.isEmpty()) {
            Message msg = tasks.poll();
            Future future = resultMap.get(msg); //gets the relevant future
            if (future != null)
                future.resolve(null);
        }
        //Iterates on all the Messagebroker queues (such as GadgetavailabiltyEvent queue which has all of its subscribers)
        //And delets m from all the queues it is in


    }


    @Override
    public Message awaitMessage(Subscriber m) throws InterruptedException {
        //waits untill there is a message in the message queue and returns the first one
        // it waits for a message because its a blocking queue.
        Message meg = subMap.get(m).take();
        return meg;
    }

}


