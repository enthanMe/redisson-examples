package org.redisson.example.locks;

import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.api.RPermitExpirableSemaphore;
import org.redisson.api.RedissonClient;

public class PermitExpirableSemaphoreExamples {

    public static void main(String[] args) throws InterruptedException {
        // connects to 127.0.0.1:6379 by default
        RedissonClient redisson = Redisson.create();

        RPermitExpirableSemaphore s = redisson.getPermitExpirableSemaphore("test");
        s.trySetPermits(1);
        String permitId = s.tryAcquire(100, 2, TimeUnit.SECONDS);

        Thread t = new Thread() {
            public void run() {
                RPermitExpirableSemaphore s = redisson.getPermitExpirableSemaphore("test");
                try {
                    String permitId = s.acquire();
                    s.release(permitId);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
        };

        t.start();
        t.join();
        
        s.tryRelease(permitId);
        
    }
    
}
