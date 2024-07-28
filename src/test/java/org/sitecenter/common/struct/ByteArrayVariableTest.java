package org.sitecenter.common.struct;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ByteArrayVariableTest {

    @Test
    void addRemoveContains() {
        ByteArrayVariable vba = new ByteArrayVariable(10, 5);
        vba.add(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        assertTrue(vba.contains(0, 8, new byte[]{4,5,6}));
        assertFalse(vba.contains(0, 8, new byte[]{4,6}));
        vba.remove(2, 4); // Removes elements at index 2, 3, and 4
        assertTrue(vba.contains(0, 8, new byte[]{2,6,7}));
        assertEquals(6, vba.getSize());
        vba.add(new byte[]{9,8,7,6,5,4,3,2,1,0});
        assertEquals(16, vba.getSize());
        assertTrue(vba.contains(0, 16, new byte[]{4,3,2}));
        assertEquals(11, vba.indexOf(0, 16, new byte[]{4,3,2}));
        assertEquals(0, vba.indexOf(0, 16, new byte[]{1,2}));
        byte[] result = vba.cloneArray();
        for (byte b : result) {
            System.out.print(b + " ");
        }

    }

}