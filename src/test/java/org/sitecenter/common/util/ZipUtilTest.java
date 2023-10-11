package org.sitecenter.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class ZipUtilTest {
    @Test
    public void zipFileCreate() {
        zipFileCreate(false);
    }
    private void zipFileCreate(boolean keepResult) {
        File testdir = new File("tmp/ziptest/");
        testdir.mkdirs();
        log.info("Zip file test dir:"+testdir.getAbsolutePath());
        File fl1 = new File(testdir,"1.txt");
        File fl2 = new File(testdir,"2.txt");
        try {
            FileUtil.createFileFromString(fl1, "This is first file.");
            FileUtil.createFileFromString(fl2, "This is second file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        File fl = new File("tmp/ziptest.zip");
        try {
            ZipUtil.zipFileCreate(new File("tmp/ziptest/"), fl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Zip file created in:"+fl.getAbsolutePath());
        assertEquals(268, fl.length());

        File testdir2 = new File("tmp/ziptest-extract/");
        testdir2.mkdirs();

        try {
            ZipUtil.zipFileExtract(fl, testdir2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        File efl1 = new File(testdir2,"1.txt");
        File efl2 = new File(testdir2,"2.txt");

        assertEquals(fl1.length(), efl1.length());
        assertEquals(fl2.length(), efl2.length());
        if (!keepResult) {
            fl.delete();
            fl1.delete();
            fl2.delete();
            efl1.delete();
            efl2.delete();

            testdir.delete();
            testdir2.delete();
        }
    }
}