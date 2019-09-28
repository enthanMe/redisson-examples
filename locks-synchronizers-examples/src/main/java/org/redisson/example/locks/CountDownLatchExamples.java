package org.redisson.example.locks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RedissonClient;

public class CountDownLatchExamples {

    public static void main(String[] args) throws InterruptedException {
        // connects to 127.0.0.1:6379 by default
        RedissonClient redisson = Redisson.create();

        ExecutorService executor = Executors.newFixedThreadPool(2);

        final RCountDownLatch latch = redisson.getCountDownLatch("latch1");
        latch.trySetCount(1);

        executor.execute(new Runnable() {

            @Override
            public void run() {
                latch.countDown();
            }
            
        });

        executor.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    latch.await(550, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
        });

        
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

    }
    
}
