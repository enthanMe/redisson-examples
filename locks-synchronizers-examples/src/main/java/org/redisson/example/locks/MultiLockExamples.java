package org.redisson.example.locks;

import static java.lang.Thread.currentThread;

import org.redisson.Redisson;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

public class MultiLockExamples {

    public static void main(String[] args) throws InterruptedException {
        // connects to 127.0.0.1:6379 by default
        RedissonClient client = Redisson.create();

        System.out.println(currentThread() + " lock");
        RLock lock1 = client.getLock("lock1");
        RLock lock2 = client.getLock("lock2");
        RLock lock3 = client.getLock("lock3");
        
        Thread t = new Thread() {
            public void run() {
                RedissonMultiLock lock = new RedissonMultiLock(lock1, lock2, lock3);
                System.out.println(currentThread() + " lock");
                lock.lock();
                
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }

                System.out.println(currentThread() + " unlock");
                lock.unlock();
            };
        };
        t.start();
        t.join(1000);

        RedissonMultiLock lock = new RedissonMultiLock(lock1, lock2, lock3);
        System.out.println(currentThread() + " lock");
        lock.lock();
        System.out.println(currentThread() + " unlock");
        lock.unlock();
    }
    
}
