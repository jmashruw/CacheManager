/**
 * Created by Jiya on 2/21/17.
 */

import java.util.concurrent.Callable;

public class Writer implements Callable<Integer>{

        private Integer number;
        private ReaderList mywriter;

        public Writer(Integer number, ReaderList mywriter){
            this.number = number;
            this.mywriter = mywriter;
        }

        @Override
        public Integer call() throws Exception{
            System.out.println("\n I am in write");
            mywriter.write(number);
            System.out.println("\nNumber "+number + " written in the singly-linkedlist");
            return number;
        }
}
