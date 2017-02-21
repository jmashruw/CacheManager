/**
 * Created by Jiya on 2/21/17.
 */
import java.util.Date;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

 /*
     * Two kinds of threads share access to a singly-linked list:
     * readers and writers. Readers merely examine the list;
     * hence they can execute concurrently with each other. Writers add
     * new items to the front of the list; writeions must be mutually exclusive
     * to preclude two writers from writeing new items at about
     * the same time. However, one write can proceed in parallel with
     * any number of reades.
     *
     * No data races between concurrent writers and readers!
     */

public class ReaderList<T> {

    private int noOfReads; /* counter to track number of current read operations */
    private int noOfWrites; /* counter to track number of current write operations */

    private final ReentrantLock myLock; /* Lock object to synchronize access */

    private final Condition writeCond; /* noOfWrites > 0 */

    private volatile Node<T> first;

    public ReaderList() {
        first = null;
        noOfReads = 0;
        noOfWrites = 0;
        myLock = new ReentrantLock();
        writeCond = myLock.newCondition();
    }

    /**
     * Acquire the lock to write if there are no concurrent write
     *
     * Precondition: noOfWrites == 0
     *
     * @throws InterruptedException
     */
    private void start_write() throws InterruptedException{
        myLock.lock();
        try{
            while(noOfWrites > 0){
                writeCond.await();
            }
            noOfWrites++ ;
        }
        finally{
            myLock.unlock();
        }
    }

    /**
     * Release the lock and signal all waiting write
     */
    private void end_write(){
        myLock.lock();
        try{
            noOfWrites-- ;
            if(noOfWrites == 0){
                writeCond.signalAll();
            }
        }
        finally{
            myLock.unlock();
        }
    }

    /**
     * Acquire the lock to read
     *
     * @throws InterruptedException
     */
    private void start_read() throws InterruptedException{
        myLock.lock();
        try{
            noOfReads++;
        }
        finally{
            myLock.unlock();
        }
    }

    /**
     * Release the lock and signal all waiting remove operations
     */
    private void end_read() throws InterruptedException{
        myLock.lock();
        try{
            noOfReads--;
        }
        finally{
            myLock.unlock();
        }
    }

    /**
     * Inserts the given item into the list.
     *
     * Precondition:  item != null
     *
     * @param item
     * @throws InterruptedException
     */
    public void write(T item) throws InterruptedException{
        assert item != null: "Error in ReaderList write:  Attempt to write null";
        start_write();
        try{
            first = new Node<T>(item, new Date(), first);
        }
        finally{
            end_write();
        }
    }

    /**
     * Determines whether or not the given item is in the list
     *
     * Precondition:  item != null
     *
     * @param item
     * @return  true if item is in the list, false otherwise.
     * @throws InterruptedException
     */
    public boolean read(T item) throws InterruptedException{
        assert item != null: "Error in ReaderList write:  Attempt to read for null";
        start_read();
        try{
            for(Node<T> curr = first;  curr != null ; curr = curr.next){
                if (item.equals(curr.item)) return true;
            }
            return false;
        }
        finally{
            end_read();
        }
    }
}
