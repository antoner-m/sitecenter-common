package org.sitecenter.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

@Slf4j
public class ProcessUtils {
    private static final String[] EMPTY_STRING = {};
    public static String executeCommandToString(String cmd) {
        StringBuilder result = new StringBuilder();
//        log.debug("\n=========== Executing command: " + cmd + " ===========");
        BufferedReader stdInput = null;
        BufferedReader stdError = null;
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            int exitValue = p.waitFor();
            String s;
            // read the output from the command
            while ((s = stdInput.readLine()) != null) {
                result.append(s);
            }
            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
                log.error(s);
            }
            p.destroy();
            return result.toString();
        } catch (IOException e) {
//            e.printStackTrace();
        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
        } finally {
            close(stdError);
            close(stdInput);
        }
        return null;
    }
    public static String[] executeCommandToStrArr(String cmd) {
        ArrayList<String> result = new ArrayList<>(5);
//        log.debug("\n=========== Executing command: " + cmd + " ===========");
        BufferedReader stdInput = null;
        BufferedReader stdError = null;
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            int exitValue = p.waitFor();
            String s;
            // read the output from the command
            while ((s = stdInput.readLine()) != null) {
                result.add(s);
            }
            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
                log.error(s);
            }
            p.destroy();
            return result.toArray(new String[0]);
        } catch (IOException e) {
//            e.printStackTrace();
        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
        } finally {
            close(stdError);
            close(stdInput);
        }
        return EMPTY_STRING;
    }
    private static void close(InputStream is) {
        if (is == null) return;
        try {
            is.close();
        } catch (Exception ignore) {
            //ignore
        }
    }
    private static void close(BufferedReader is) {
        if (is == null) return;
        try {
            is.close();
        } catch (Exception ignore) {
            //ignore
        }
    }

}
