package org.redisson.example.locks;

import static java.lang.Thread.currentThread;

import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

public class LockExamples {

    public static void main(String[] args) throws InterruptedException {
        // connects to 127.0.0.1:6379 by default
        RedissonClient redisson = Redisson.create();

        // 主线程获取锁 并 加锁
        RLock lock = redisson.getLock("lock");

        System.out.println(currentThread() + " lock");
        lock.lock(2, TimeUnit.SECONDS);

        // 子线程获取同样的锁，然后加锁、解锁
        Thread t = new Thread() {
            public void run() {
                RLock lock1 = redisson.getLock("lock");
                System.out.println(currentThread() + " lock");
                lock1.lock();
                System.out.println(currentThread() + " unlock");
                lock1.unlock();
            };
        };

        t.start();
        t.join(); // 子线程阻塞直到执行完成后唤醒主线程

        System.out.println(currentThread() + " unlock");
        // 主线程尝试解除name为lock的锁
        // 此时锁已被释放，抛出java.lang.IllegalMonitorStateException: attempt to unlock lock, not locked by current thread
        lock.unlock();
        
        redisson.shutdown();
    }
    
}
