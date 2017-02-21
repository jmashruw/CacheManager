/**
 * Created by Jiya on 2/21/17.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class Tester {
        public static void main(String[] args){
            ReaderList temp = new ReaderList();

            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

            List<Future<Integer>> resultList = new ArrayList<>();

            Random random = new Random();

            Integer number = random.nextInt(10);
            Writer k  = new Writer(number, temp);
            Future<Integer> result = executor.submit(k);
            resultList.add(result);

            for (int i=0; i<10; i++)
            {
                number = random.nextInt(10);
                if(i%2 == 0){
                    Reader m  = new Reader(number, temp);
                    result = executor.submit(m);
                    resultList.add(result);
                }
                else{
                    k  = new Writer(number, temp);
                    result = executor.submit(k);
                    resultList.add(result);
                }
            }

            for(Future<Integer> future : resultList)
            {
                try
                {
                    System.out.println("Future result is - " + future.get());
                }
                catch (InterruptedException | ExecutionException e)
                {
                    e.printStackTrace();
                }
            }
            //shut down the executor service now
            executor.shutdown();
        }
}
