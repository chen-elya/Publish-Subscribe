package bgu.spl.mics;



import java.util.concurrent.TimeUnit;

/**
 * A Future object represents a promised result - an object that will
 * eventually be resolved to hold a result of some operation. The class allows
 * Retrieving the result once it is available.
 * <p>
 * Only private methods may be added to this class.
 * No public constructor is allowed except for the empty constructor.
 */
public class Future<T> {
    T result;



    /**
     * This should be the the only public constructor in this class.
     */
    public Future() {
        //TODO: implement this
        result = null;
    }

    /**
     * retrieves the result the Future object holds if it has been resolved.
     * This is a blocking method! It waits for the computation in case it has
     * not been completed.
     * <p>
     *
     * @return return the result of type T if it is available, if not wait until it is available.
     */
	//If there are 2 threads that want to do use get and the result is not Done yet we will get into a situation that
	//two threads will wait for resolve instead of one, WASTE
    public synchronized T get() {
        try {
            if (!isDone()) {
                synchronized (this) {
                    this.wait();
                    
                }
            }
        } catch (InterruptedException e) {
        }
        return result;
    }

    /**
     * Resolves the result of this Future object.
     */
    //We use Synch: if 2 threads want to do different resolves, one of the resolves will be overridden
    public  void resolve(T result) {
        this.result = result;
        synchronized (this) {
            this.notifyAll();
        }
    }

    /**
     * @return true if this object has been resolved, false otherwise
     */
    public boolean isDone() {
        boolean isDone;
        if(result==null)
        {
            isDone=false;
        }
        else isDone = true;
        return isDone;
    }

    /**
     * retrieves the result the Future object holds if it has been resolved,
     * This method is non-blocking, it has a limited amount of time determined
     * by {@code timeout}
     * <p>
     *
     * @param timeout the maximal amount of time units to wait for the result.
     * @param unit    the {@link TimeUnit} time units to wait.
     * @return return the result of type T if it is available, if not,
     * wait for {@code timeout} TimeUnits {@code unit}. If time has
     * elapsed, return null.
     */
    //In computer science, non-blocking synchronization ensures that threads competing for a shared resource do not have
	// their execution indefinitely (forever) postponed by mutual exclusion.
	//We use sych becuase of this example:
	//If there are 2 threads that want to do use get and the result is not Done yet we will get into a situation that
	//two threads will wait for resolve instead of one, WASTE
    public synchronized T get(long timeout, TimeUnit unit) {
        long time = unit.convert(timeout, unit); //convert time to the real time it should wait
        {
            try {
                if (!isDone()) {
                    wait(time);
                }
            } catch (InterruptedException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
            return result; //returns a result if we resolved and null otherwise
        }
    }
}

