package org.redisson.example.locks;

import org.redisson.Redisson;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;

public class SemaphoreExamples {

    public static void main(String[] args) throws InterruptedException {
        // connects to 127.0.0.1:6379 by default
        RedissonClient redisson = Redisson.create();

        RSemaphore s = redisson.getSemaphore("test");
        s.trySetPermits(5);
        s.acquire(3);

        Thread t = new Thread() {
            @Override
            public void run() {
                RSemaphore s = redisson.getSemaphore("test");
                s.release();
                s.release();
            }
        };

        t.start();

        s.acquire(4);
        
        redisson.shutdown();
    }
    
}
