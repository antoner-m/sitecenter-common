package org.sitecenter.common.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LocalDateUtilsTest {
    @Test
    void getDate() {

        assertEquals(LocalDateTime.of(2023,4,30,12,22,36), LocalDateUtils.getDate("2023-04-30T12:22:36Z"));
        assertEquals(LocalDateTime.of(2023,1,13,18,17,48), LocalDateUtils.getDate("2023.01.13T18:17:48+0000"));
        assertEquals(LocalDateTime.of(2023,1,13,18,17,48), LocalDateUtils.getDate("2023-01-13T18:17:48+0000"));
        assertEquals(LocalDateTime.of(2003,6,29,18,24,40), LocalDateUtils.getDate("2003-06-29T18:24:40Z"));
        assertEquals(LocalDateTime.of(2003,6,29,18,24,40), LocalDateUtils.getDate("2003-06-29T18:24:40"));

        assertEquals(LocalDateTime.of(2003,6,29,0,0), LocalDateUtils.getDate("2003-06-29"));
        assertEquals(LocalDateTime.of(2024,3,29,0,0), LocalDateUtils.getDate("2024.03.29"));
        assertEquals(LocalDateTime.of(2022,8,24,2,17,5), LocalDateUtils.getDate("2022-08-24 02:17:05"));
        assertEquals(LocalDateTime.of(2023,6,8,21,13,45), LocalDateUtils.getDate("2023-06-09T00:13:45+03:00"));
        assert(LocalDateUtils.getDate("2023-06-fdlkjnsdfljk09T00:13:45+03:00") == null);
        assertEquals(LocalDateTime.of(2023,5,31,10,19,3), LocalDateUtils.getDate("2023-05-31T10:19:03Z "));
        assertEquals(LocalDateTime.of(2023,9,22,10,46,31), LocalDateUtils.getDate("2023-09-22T10:46:31Z"));
        assertEquals(LocalDateTime.of(1,1,1,0,0,0), LocalDateUtils.getDate("0001-01-01T00:00:00.00Z"));

        assertEquals(LocalDateTime.of(2023,5,15,7,5,50), LocalDateUtils.getDate("2023-05-15t07:05:50z"));
        assertEquals(LocalDateTime.of(2024,12,11,0,0,0), LocalDateUtils.getDate("2024-12-11T00:00:00+01:00Z"));

        assertEquals(LocalDateTime.of(2024,6,24,0,0,0), LocalDateUtils.getDate("2024-06-24 00:00:00.000"));

        //(KST) is 9 hours ahead of Coordinated Universal Time (UTC)
        assertEquals(LocalDateTime.of(2023,6,27,1,39,58), LocalDateUtils.getDate("2023-06-27 10:39:58 KST"));

        assertEquals(LocalDateTime.of(2021,9,17,8,40,10), LocalDateUtils.getDate("2021-09-17T08:40:10+0000Z"));


        assertEquals(LocalDateTime.of(2023,9,11,5,5,42,722739), LocalDateUtils.getDate("2023-09-11 15:05:42.722739+10"));

        assertEquals(LocalDateTime.of(1998,12,28,5,0,0), LocalDateUtils.getDate("1998-12-28T05:00:00Z.0Z"));

        assertEquals(LocalDateTime.of(2023,5,26,4,10,32, 593), LocalDateUtils.getDate("2023-05-26 04:10:32.593"));

        assertEquals(LocalDateTime.of(2024,11,18,9,22,59), LocalDateUtils.getDate("2024-11-18 20:22:59+11"));

        assertEquals(LocalDateTime.of(2020,9,24,19,21,27), LocalDateUtils.getDate("2020-09-24T19:21:27UTC"));

        assertEquals(LocalDateTime.of(2024,11,1,0,0,0), LocalDateUtils.getDate("01-Nov-2024"));

        assertEquals(LocalDateTime.of(2023,10,31,0,0,0), LocalDateUtils.getDate("31-OCT-2023"));

        assertEquals(LocalDateTime.of(2019,2,18,0,0,0), LocalDateUtils.getDate("20190218 #19329631"));

        assertEquals(LocalDateTime.of(2024,6,28,0,59,59), LocalDateUtils.getDate("28/06/2024 00:59:59"));

        assertEquals(LocalDateTime.of(2021,2,1,10,46,2), LocalDateUtils.getDate("01-02-2021 11:46:02 GMT+1"));

        assertEquals(LocalDateTime.of(2014,9,26,0,0,0), LocalDateUtils.getDate("26 Sep 2014"));

        assertEquals(LocalDateTime.of(2019,8,8,1,10,59), LocalDateUtils.getDate("08.08.2019 01:10:59"));

        assertEquals(LocalDateTime.of(2024,3,28,0,0,0), LocalDateUtils.getDate("20240328"));

        assertEquals(LocalDateTime.of(2025,3,29,0,0,0), LocalDateUtils.getDate("29032025"));

        assertEquals(LocalDateTime.of(2024,5,17,10,23,20), LocalDateUtils.getDate("2024/5/17 10:23:20"));

        assertEquals(LocalDateTime.of(2025,4,22,22,49,7), LocalDateUtils.getDate("04/22/2025 22:49:07"));

        assertEquals(LocalDateTime.of(2010,2,3,14,28,41), LocalDateUtils.getDate("2010.02.03 14:28:41"));

        assertEquals(LocalDateTime.of(1998,9,8,13,0,0), LocalDateUtils.getDate("1998.09.08 13:00:00"));

        assertEquals(LocalDateTime.of(2022,8,13,16,33,33), LocalDateUtils.getDate("2022.08.13 16:33:33"));

        assertEquals(LocalDateTime.of(2011,3,4,12,40,43), LocalDateUtils.getDate("0-UANIC 20110304124043"));


        assertEquals(LocalDateTime.of(2024,3,27,13,9,56), LocalDateUtils.getDate("27-Mar-2024 13:09:56 UTC"));

        assertEquals(LocalDateTime.of(2023,10,6,0,0,0), LocalDateUtils.getDate("06 OCT 2023"));

        assertEquals(LocalDateTime.of(1996,6,11,4,0,0), LocalDateUtils.getDate("1996/6/11 4:00:00"));


//        assertEquals(LocalDateTime.of(2015,9,29,4,57,40), LocalDateUtils.getDate("2015-29-09T04:57:40Z"));


    }

}