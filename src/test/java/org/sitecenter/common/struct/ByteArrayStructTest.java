package org.sitecenter.common.struct;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ByteArrayStructTest {

    @Test
    void structIndexOf() {
        ByteArrayStruct vbs = new ByteArrayStruct(1000, 10, 5);
        vbs.add(new byte[]{1,2,3,4,5});
        vbs.add(new byte[]{2,3,4,5,6});
        vbs.add(new byte[]{3,4,5,6,7});

        assertEquals(2, vbs.indexOf(new byte[]{3,4,5,6,7}));
        assertEquals(1, vbs.indexOf(new byte[]{2,3,4,5,6}));
        assertEquals(0, vbs.indexOf(new byte[]{1,2,3,4,5}));
        vbs.remove(1);
        assertEquals(1, vbs.indexOf(new byte[]{3,4,5,6,7}));
        vbs.add(new byte[]{4,5,6,7,8});
        assertEquals(2, vbs.indexOf(new byte[]{4,5,6,7,8}));
    }

    @Test
    void structContains() {
        ByteArrayStruct vbs = new ByteArrayStruct(1000, 10, 5);
        vbs.add(new byte[]{1,2,3,4,5});
        vbs.add(new byte[]{2,3,4,5,6});
        vbs.add(new byte[]{3,4,5,6,7});

        int found[] = vbs.findContains(new byte[]{4,5}).stream().mapToInt(x -> x).toArray();
        assertArrayEquals(new int[]{0, 1, 2}, found);

        int found2[] = vbs.findContains(new byte[]{2,3,4,5,6}).stream().mapToInt(x -> x).toArray();
        assertArrayEquals(new int[]{1}, found2);

        int found3[] = vbs.findContains(new byte[]{2,5,6}).stream().mapToInt(x -> x).toArray();
        assertArrayEquals(new int[]{}, found3);

        int found4[] = vbs.findContains(new byte[]{5,6}).stream().mapToInt(x -> x).toArray();
        assertArrayEquals(new int[]{ 1, 2}, found4);

    }

    @Test
    void arrayContains() {
        byte[]source = {1,2,3,4,5,6,7};

        assertTrue(ByteArrayStruct.arrayContains(source,2,4, new byte[]{3,4}));
        assertTrue(ByteArrayStruct.arrayContains(source,0,4, new byte[]{3,4}));
        assertTrue(ByteArrayStruct.arrayContains(source,0,7, new byte[]{1,2,3,4,5,6,7}));
        assertFalse(ByteArrayStruct.arrayContains(source,0,7, new byte[]{1,2,4,5}));
    }
}