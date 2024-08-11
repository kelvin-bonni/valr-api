package com.valr.api.utils;

import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

@Component
public class Locker {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public <T> T withReadLock(Supplier<T> block) {
        readLock.lock();
        try {
            return block.get();
        } finally {
            readLock.unlock();
        }
    }

    public <T> T withWriteLock(Supplier<T> block) {
        writeLock.lock();
        try {
            return block.get();
        } finally {
            writeLock.unlock();
        }
    }
}
