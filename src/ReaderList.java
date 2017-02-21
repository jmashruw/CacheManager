/**
 * Created by Jiya on 2/21/17.
 */

import java.lang.*;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Iterator;

 /*
     * Two kinds of threads share access to a singly-linked list:
     * readers and writers. Readers merely examine the list;
     * hence they can execute concurrently with each other. Writers add
     * new items to the front of the list; write must be mutually exclusive
     * to preclude two writers from writeing new items at about
     * the same time. However, one write can proceed in parallel with
     * any number of reades.
     *
     * No data races between concurrent writers and readers!
     */

public class ReaderList {

    private int noOfWrites; /* counter to track number of current write operations */

    private final ReentrantLock myLock; /* Lock object to synchronize access */

    private final Condition writeCond; /* noOfWrites > 0 */

    private volatile Node first;

    public boolean modified = false;

    HashMap<Integer, List<ReaderList>> readers = new HashMap<>();

    public ReaderList() {
        first = null;
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
     * Inserts the given item into the list.
     *
     * Precondition:  item != null
     *
     * @param item
     * @throws InterruptedException
     */
    public void write(int item) throws InterruptedException{
        start_write();
        try{
            first = new Node(item, first);
        }
        finally{
            end_write();
        }
    }

    /**
     * Updates the given item into the list by multiplying it with itself.
     *
     * Precondition:  item != null
     *
     * @param item
     * @throws InterruptedException
     */
    public void update(int item) throws InterruptedException{
        start_write();
        try{
            for(Node curr = first;  curr != null ; curr = curr.next){
                if (item == curr.item) {
                    curr.item = item * item + 1;
                    List<ReaderList> activeReaders = readers.get(curr.item);
                    Iterator<ReaderList> activeReadersIterator = activeReaders.iterator();
                    while (activeReadersIterator.hasNext()){
                        ReaderList currReader = activeReadersIterator.next();
                        if(currReader != null) {
                            currReader.modified = true;
                        }
                    }
                }
            }
        }
        finally{
            end_write();
        }
    }

    /**
     * Saves session
     *
     * @throws InterruptedException
     */
    public void addToSession(int item) throws InterruptedException{
        myLock.lock();
        try{
            if(readers.containsKey(item)){
                List<ReaderList> h = readers.get(item);
                readers.remove(item);
                h.add(this);
                readers.put(item, h);
            }
            else {
                List<ReaderList> h = new ArrayList<>();
                h.add(this);
                readers.put(item, h);
            }
        }
        finally {
            myLock.unlock();
        }
    }

    /**
     * Removes session
     *
     * @throws InterruptedException
     */
    public void removeSession(int item) throws InterruptedException{
        myLock.lock();
        try{
            if(readers.containsKey(item) && readers.get(item).size() > 0){
                List<ReaderList> h = readers.get(item);
                readers.remove(item);
                h.remove(this);
                readers.put(item, h);
            }
            else
                readers.remove(item);
        }
        finally {
            myLock.unlock();
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
    public ArrayList<Integer> read(int item) throws InterruptedException{
        addToSession(item);
        ArrayList<Integer> arr = new ArrayList<>();
            for(Node curr = first;  curr != null ; curr = curr.next){
                if (item >= curr.item )
                    arr.add(curr.item);
            }
        removeSession(item);
        return arr;
    }

    /**
     * Determines whether or not the given item is in the list
     *
     * @param item
     * @return  true if item is in the list, false otherwise.
     * @throws InterruptedException
     */
    public boolean search(int item) throws InterruptedException{
            for(Node curr = first;  curr != null ; curr = curr.next){
                if (item == curr.item)
                    return true;
            }
            return false;
    }
}
