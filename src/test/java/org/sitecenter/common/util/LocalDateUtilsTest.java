package org.sitecenter.common.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LocalDateUtilsTest {
    @Test
    void getDate() {

        assertEquals(LocalDateTime.of(2023, 4,30,12,22,36), LocalDateUtils.getDate("2023-04-30T12:22:36Z"));
        assertEquals(LocalDateTime.of(2023, 1,13,18,17,48), LocalDateUtils.getDate("2023.01.13T18:17:48+0000"));
        assertEquals(LocalDateTime.of(2023,1,13,18,17,48), LocalDateUtils.getDate("2023-01-13T18:17:48+0000"));
        assertEquals(LocalDateTime.of(2003,6,29,18,24,40), LocalDateUtils.getDate("2003-06-29T18:24:40Z"));
        assertEquals(LocalDateTime.of(2003,6,29,18,24,40), LocalDateUtils.getDate("2003-06-29T18:24:40"));
        assertEquals(LocalDateTime.of(2003,6,29,0,0), LocalDateUtils.getDate("2003-06-29"));
        assertEquals(LocalDateTime.of(2024,3,29,0,0), LocalDateUtils.getDate("2024.03.29"));
        assertEquals(LocalDateTime.of(2022,8,24,2,17,5), LocalDateUtils.getDate("2022-08-24 02:17:05"));
        assertEquals(LocalDateTime.of(2023,6,9,0,13,45), LocalDateUtils.getDate("2023-06-09T00:13:45+03:00"));
        assert(LocalDateUtils.getDate("2023-06-fdlkjnsdfljk09T00:13:45+03:00") == null);
        assertEquals(LocalDateTime.of(2023,5,31,10,19,3), LocalDateUtils.getDate("2023-05-31T10:19:03Z "));
        assertEquals(LocalDateTime.of(2023,3,3,20,43,29), LocalDateUtils.getDate("Fri Mar 03 17:43:29 UTC 2023"));



    }

}