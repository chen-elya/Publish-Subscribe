package bgu.spl.mics;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FutureTest {
    Future<Integer> future1;
    Future<Integer> future2;
    Random rnd;
    ReentrantLock lock = new ReentrantLock();
    volatile long start = -1;
    volatile long end = -1;
    int res = -1;
    int expected_res = -1;

    @BeforeEach
    public void setUp() throws Exception {
        future1 = new Future<Integer>();
        future2 = new Future<Integer>();
        start = end = res = expected_res = -1;
    }

    @AfterEach
    public void tearDown() {
        if (lock.isLocked()) {
            lock.unlock();
        }
    }


    @Test
    public void testResolve() {
        assertDoesNotThrow(() -> {
            future1.resolve(1);
        }); //checks if it throws exception when resolving 1
        assertDoesNotThrow(() -> {
            future2.resolve(null);
        }); // when its' null
    }

    @Test
    public void testIsDone() {
        assertFalse(future1.isDone());
        future1.resolve(9);
        assertTrue(future1.isDone());

        future2.resolve(5);
        assertTrue(future2.isDone());
    }

    @Test
    public void testGet() {
        int tester = (int)(Math.random()*2000);
        future1.resolve(tester);
        assertEquals(tester, future1.get());
        int timeout = 1000;
        new Thread(() -> { //initialize new Thread
            start = System.currentTimeMillis();//current time
            lock.lock();//we lock to protect the value of future 2 when we use get
            res = future2.get();
            end = System.currentTimeMillis();//different current time
            lock.unlock();
        }).start();//start the Thread

        while (start == -1) {
        }//Busy wait until the Thread gets the start

        try { // sleeps for 1 second and updates the end, res values
            Thread.sleep(timeout);
        } catch (InterruptedException ignored) {
            fail("The Thread couldn't sleep");
        }

        expected_res = new Random().nextInt();
        future2.resolve(expected_res);
        //waits until the Thread ends (waits lock to unlock)
        lock.lock(); // we lock to make sure that the values we have written will not change by another thread until the end of the checks
        assertTrue((end - start) >= (timeout), "it only took:" + (end - start) + "ms < " + timeout + " - [" + start + "," + end + "]"); //if the (end-start)<timeout send the message on the right
        assertEquals(expected_res, res);
        lock.unlock();
    }

    @Test
    public void testGet2() {
        int tester = rnd.nextInt();
        future1.resolve(tester);
        assertEquals(tester, future1.get(1, TimeUnit.MILLISECONDS)); //TimeUnit.MILLISECONDS = the time units we use to check (such as seconds, minutes...)
        int timeout = 1000;
        new Thread(() -> { //initialize new Thread
            start = System.currentTimeMillis();//current time
            lock.lock();//we lock to protect the value of future 2 when we use get
            res = future2.get(1500, TimeUnit.MILLISECONDS);
            end = System.currentTimeMillis();//different current time
            lock.unlock();
        }).start();//start the Thread
        while (start == -1) {
        }//Busy wait until the Thread gets the start

        try { // sleeps for 1 second and updates the end, res values
            Thread.sleep(timeout);
        } catch (InterruptedException ignored) {
            fail("The Thread couldn't sleep");
        }

        expected_res = new Random().nextInt();
        future2.resolve(expected_res);
        //waits until the Thread ends (waits lock to unlock)
        lock.lock(); // we lock to make sure that the values we have written will not change by another thread until the end of the checks
        assertTrue((end - start) >= (timeout), "it only took:" + (end - start) + "ms < " + timeout + " - [" + start + "," + end + "]"); //if the (end-start)<timeout send the message on the right
        assertEquals(expected_res, res);
        lock.unlock();
    }
//    Integer ans = -1;
//    public void testGet3() {
//        int tester = rnd.nextInt();
//        future1.resolve(tester);
//        assertEquals(tester, future1.get(1, TimeUnit.MILLISECONDS)); //TimeUnit.MILLISECONDS = the time units we use to check (such as seconds, minutes...)
//        new Thread(() -> { //initialize new Thread
//            start = System.currentTimeMillis();//current time
//            lock.lock();//we lock to protect the value of future 2 when we use get
//            ans = future2.get(500, TimeUnit.MILLISECONDS);
//            end = System.currentTimeMillis();//different current time
//            lock.unlock();
//        }).start();//start the Thread
//        while (start == -1) {}//Busy wait until the Thread gets the start
//
//        lock.lock();
//        assertTrue((end-start)>500);
//        assertNull(null);
//        lock.unlock();
//    }
//


}
