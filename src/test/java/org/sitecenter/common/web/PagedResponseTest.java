package org.sitecenter.common.web;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PagedResponseTest {

    @Test
    public void testGetContent() {
        PagedResponse<String> pagedResponse = new PagedResponse<>();

        pagedResponse.setContent(Arrays.asList("test1", "test2", "test3"));
        assertEquals(3, pagedResponse.getContent().size());
        assertEquals("test1", pagedResponse.getContent().get(0));
        assertEquals("test2", pagedResponse.getContent().get(1));
        assertEquals("test3", pagedResponse.getContent().get(2));
    }

    @Test
    public void testPagedConstructor() {
        List<String> fullcollection =Arrays.asList("test1", "test2", "test3", "test4", "test5");
        PagedResponse<String> pagedResponse = new PagedResponse<>(fullcollection, 1, 2);

        assertEquals(2, pagedResponse.getContent().size());
        assertEquals("test3", pagedResponse.getContent().get(0));
        assertEquals("test4", pagedResponse.getContent().get(1));

        PagedResponse<String> pagedResponse2 = new PagedResponse<>(fullcollection, 2, 2);
        assertEquals(1, pagedResponse2.getContent().size());
        assertEquals(3, pagedResponse2.getTotalPages());
        assertEquals(5, pagedResponse2.getTotalElements());
        assertEquals("test5", pagedResponse2.getContent().get(0));
        assertTrue(pagedResponse2.isLast());

    }

    @Test
    public void testGetContentWhenEmpty() {
        PagedResponse<String> pagedResponse = new PagedResponse<>();

        assertEquals(0, pagedResponse.getContent().size());
    }

    @Test
    public void testGetContentWithFilteredElements() {
        PagedResponse<String> pagedResponse = new PagedResponse<>(Arrays.asList("test1", "test2", "test3"), 1, 1);

        assertEquals(1, pagedResponse.getContent().size());
        assertEquals("test2", pagedResponse.getContent().get(0));
    }

    @Test
    public void testGetContentWithExceededPageIndex() {
        PagedResponse<String> pagedResponse = new PagedResponse<>(Arrays.asList("test1", "test2", "test3"), 3, 1);

        assertEquals(0, pagedResponse.getContent().size());
    }
}