/**
 * Created by Jiya on 2/21/17.
 */

import java.util.concurrent.Callable;

public class Writer implements Callable<Integer>{

        private int number;
        private ReaderList mywriter;

        public Writer(int number, ReaderList mywriter){
            this.number = number;
            this.mywriter = mywriter;
        }

        @Override
        public Integer call() throws Exception{
            System.out.println("\n I am in write");

            /*check if item is to be added or modified*/
            if(mywriter.search(number)){
                  mywriter.update(number);
                    System.out.println("\nNumber " + number + " modified in the singly-linkedlist");
            }
            else {
                mywriter.write(number);
                System.out.println("\nNumber " + number + " written in the singly-linkedlist");
            }

            return number;
        }
}
