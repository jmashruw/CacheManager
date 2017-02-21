/**
 * Created by Jiya on 2/21/17.
 */

import java.util.concurrent.Callable;
import java.util.ArrayList;
import java.util.Iterator;

public class Reader implements Callable<Integer> {
    public int number;
    private ReaderList myreader;
    public boolean readCond = true;     //readCond to check if read needs to be done again

    public Reader(int number, ReaderList myreader){
        this.number = number;
        this.myreader = myreader;
    }

    @Override
    public Integer call() throws Exception{
        System.out.println("\n I am in read");

        /*Repeat read if and only if item is modified*/
        do{
            ArrayList<Integer> result = myreader.read(number);
            System.out.println("Numbers less than " + number + " are: ");

            Iterator<Integer> resultIterator = result.iterator();
            while (resultIterator.hasNext()) {
                System.out.print(resultIterator.next() + " ");
            }

            if(myreader.modified == false)
                readCond = false;

        }while(readCond);

        return number;
    }
}
