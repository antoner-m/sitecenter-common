package org.sitecenter.common.struct;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class VariableByteArray {

    protected byte[] data;
    protected int size; // The actual size of the data stored
    protected int incrementCapacity; //How many elements add to array when it's size is not enough

    public ReentrantReadWriteLock getLock() {
        return lock;
    }

    protected final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public VariableByteArray(int initialCapacity, int incrementCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive.");
        }
        if (incrementCapacity <= 0) {
            throw new IllegalArgumentException("Increment capacity must be positive.");
        }
        this.data = new byte[initialCapacity];
        this.size = 0;
        this.incrementCapacity = incrementCapacity;
    }

    public void add(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return;
        }
        lock.writeLock().lock();
        try {
            ensureCapacity(size + bytes.length);
            System.arraycopy(bytes, 0, data, size, bytes.length);
            size += bytes.length;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void remove(int fromIdx, int toIdx) {
        lock.writeLock().lock();
        try {
            if (fromIdx < 0 || toIdx >= size || fromIdx > toIdx) {
                throw new IndexOutOfBoundsException("Invalid fromIdx or toIdx.");
            }
            int numElementsToRemove = toIdx - fromIdx + 1;
            System.arraycopy(data, toIdx + 1, data, fromIdx, size - toIdx - 1);
            size -= numElementsToRemove;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public byte[] getData() {
        return data;
    }

    public int getSize() {
        lock.readLock().lock();
        try {
            return size;
        } finally {
            lock.readLock().unlock();
        }
    }

    public byte[] cloneArray() {
        lock.readLock().lock();
        try {
            byte[] result = new byte[size];
            System.arraycopy(data, 0, result, 0, size);
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean contains(int fromIdx, int toIdx, byte[] inner) {
        return indexOf(fromIdx, toIdx, inner)!=-1;
    }

    public int indexOf(int fromIdx, int toIdx, byte[] inner) {
        lock.readLock().lock();
        try {
            if (inner.length == 0) {
                return -1;
            }
            if (data.length < inner.length) {
                return -1;
            }

            for (int i = fromIdx; i <= Math.min(toIdx, size) - inner.length; i++) {
                boolean found = true;
                for (int j = 0; j < inner.length; j++) {
                    if (data[i + j] != inner[j]) {
                        found = false;
                        break;
                    }
                }
                if (found) {
                    return i;
                }
            }
            return -1;
        } finally {
            lock.readLock().unlock();
        }
    }
    public void clear() {
        lock.writeLock().lock();
        try {
            size = 0;
            data = new byte[incrementCapacity];
        } finally {
            lock.writeLock().unlock();
        }
    }

    protected void ensureCapacity(int minCapacity) {
        if (minCapacity > data.length) {
            int newCapacity = Math.max(data.length + incrementCapacity, minCapacity);
            byte[] newData = new byte[newCapacity];
            System.arraycopy(data, 0, newData, 0, size);
            data = newData;
        }
    }

}
