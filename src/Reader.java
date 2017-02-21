/**
 * Created by Jiya on 2/21/17.
 */

import java.util.concurrent.Callable;

public class Reader implements Callable<Integer> {
    private Integer number;
    private ReaderList myreader;

    public Reader(Integer number, ReaderList myreader){
        this.number = number;
        this.myreader = myreader;
    }

    @Override
    public Integer call() throws Exception{
        System.out.println("\n I am in read");
        Boolean result = new Boolean(myreader.read(number));
        if(result.equals(true))
            System.out.println("\nNumber "+number + " found in the singly-linkedlist");
        else
            System.out.println("\nNumber "+number + " not found in the singly-linkedlist");
        return number;
    }
}
