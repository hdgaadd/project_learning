package org.codeman;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hdgaadd
 * created on 2022/10/30
 *
 * description: volatile可保证线程可见性，但无法保证高并发操作下的原子性，AtomicInteger可以
 *
 * knowledge: volatile保证当前读取的可见性，读取后瞬时的值改变，之前读取的值是不会随之改变的
 */
@Slf4j
public class Volatile implements Runnable {

    private volatile int valA = 0;

    AtomicInteger valB = new AtomicInteger(0);

    @Override
    public void run() {
        // 如果把1000改为6，volatile不会出现高并发带来的数据不一致性
        for (int i = 0; i < 1000; i++) {
            valA++;
            valB.getAndAdd(1);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Volatile v = new Volatile();
        Thread thread1 = new Thread(v);
        Thread thread2 = new Thread(v);
        thread1.start();
        thread2.start();
        // 等线程执行完，再执行打印操作
        thread1.join();
        thread2.join();

        log.info("valA is {} , valB is {}", v.valA, v.valB);
    }
}
