package org.redisson.example.locks;

import static java.lang.Thread.currentThread;

import java.util.ArrayList;
import java.util.List;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

public class FairLockExamples {

    public static void main(String[] args) throws InterruptedException {
        // connects to 127.0.0.1:6379 by default
        RedissonClient redisson = Redisson.create();

        System.out.println(currentThread() + " fair lock");
        RLock lock = redisson.getFairLock("test");

        int size = 10;
        List<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < size; i++) {
            final int j = i;
            Thread t = new Thread() {
                public void run() {
                    System.out.println(currentThread() + " fair lock");
                    lock.lock();
                    System.out.println(currentThread() + " fair unlock");
                    lock.unlock();
                };
            };
            
            threads.add(t);
        }
        
        for (Thread thread : threads) {
            thread.start();
            thread.join(5);
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
    }
    
}
