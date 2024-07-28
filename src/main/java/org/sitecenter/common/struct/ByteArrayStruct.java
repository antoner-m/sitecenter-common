package org.sitecenter.common.struct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents a variable-sized byte array data structure that stores internal record structures of fixed size.
 */
public class ByteArrayStruct {
    private ByteArrayVariable byteArrayVariable;
    private int structSize = 1; //size of internal record in array

    public ByteArrayStruct(int initialCapacityElements, int incrementElements, int structSize) {
        byteArrayVariable = new ByteArrayVariable(initialCapacityElements * structSize, incrementElements * structSize);
        if (structSize <= 0) {
            throw new IllegalArgumentException("Struct size must be positive.");
        }
        this.structSize = structSize;
    }

    public void add(byte[] bytes) {
        if (bytes == null || bytes.length != structSize)
            throw new IllegalArgumentException("value length is not equal to structSize.");
        byteArrayVariable.add(bytes);
    }

    public void remove(int elementIdx) {
        int absIdx = elementIdx * structSize;
        byteArrayVariable.remove(absIdx, absIdx + structSize);
    }

    public boolean contains(byte[] searchData) {
        if (searchData.length != structSize)
            throw new IllegalArgumentException("searchData size is not eqaual to structSize.");
        return indexOf(searchData) != -1;
    }

    /**
     * Searches for the first occurrence of a byte array within the data structure.
     *
     * @param searchData the byte array to search for
     * @return the index of the first occurrence of the search data, or -1 if not found
     * @throws IllegalArgumentException if searchData is null or its size is not equal to structSize
     */
    public int indexOf(byte[] searchData) {
        if (searchData == null)
            throw new IllegalArgumentException("searchData is null.");
        if (searchData.length != structSize)
            throw new IllegalArgumentException("searchData size is not equal to structSize.");

        byteArrayVariable.getLock().readLock().lock();
        try {
            byte[] data = byteArrayVariable.getData();
            int size = byteArrayVariable.getSize();
            if (searchData.length == 0) {
                return -1;
            }
            if (data.length < structSize) {
                return -1;
            }

            for (int i = 0; i <= size - searchData.length; i += structSize) {
                if (Arrays.equals(data, i, i + structSize, searchData, 0, structSize))
                    return i / structSize;
            }
            return -1;
        } finally {
            byteArrayVariable.getLock().readLock().unlock();
        }
    }

    /**
     * Searches for occurrences of a byte array within the data structure
     *
     * @param searchData the byte array to search for
     * @return a list of integer indices where the search data is found
     * @throws IllegalArgumentException if searchData is null or its size is greater than the structSize
     */
    public List<Integer> findContains(byte[] searchData) {
        if (searchData == null)
            throw new IllegalArgumentException("searchData is null.");
        if (searchData.length > structSize)
            throw new IllegalArgumentException("searchData size is more than structSize.");
        byteArrayVariable.getLock().readLock().lock();
        try {
            ArrayList<Integer> result = new ArrayList<>();
            byte[] data = byteArrayVariable.getData();
            for (int i = 0; i <= data.length - structSize; i += structSize) {
                if (arrayContains(data, i, i + structSize, searchData))
                    result.add(i / structSize);
            }
            return result;
        } finally {
            byteArrayVariable.getLock().readLock().unlock();
        }
    }
    public ByteArrayVariable getVariableByteArray() {
        return byteArrayVariable;
    }

    public int getStructSize() {
        return structSize;
    }

    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Determines whether a byte array contains another byte array within a specified range.
     *
     * @param outer      the byte array to search within
     * @param fromIdx    the start index of the range (inclusive)
     * @param toIdx      the end index of the range (inclusive)
     * @param searchData the byte array to search for
     * @return true if the search data is found within the range, false otherwise
     */
    public static boolean arrayContains(byte[] outer, int fromIdx, int toIdx, byte[] searchData) {
        if (searchData.length == 0) {
            return false;
        }
        if (outer.length < searchData.length) {
            return false;
        }

        for (int i = fromIdx; i <= toIdx - searchData.length; i++) {
            if (Arrays.equals(outer, i, i + searchData.length, searchData, 0, searchData.length))
                return true;
        }
        return false;
    }
}
