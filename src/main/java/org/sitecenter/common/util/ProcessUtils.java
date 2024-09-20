package org.sitecenter.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;

public class ProcessUtils {
    private static final Logger logger = Logger.getLogger(ProcessUtils.class.getName());

    private static final String[] EMPTY_STRING = {};
    public static String executeCommandToString(String cmd) {
        StringBuilder result = new StringBuilder();
//        log.debug("\n executeCommandToString: " + cmd + " ===========");
        BufferedReader stdInput = null;
        BufferedReader stdError = null;
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String s;
            // read the output from the command
            while ((s = stdInput.readLine()) != null) {
                result.append(s);result.append("\n");
            }
            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
                logger.warning(s);
            }
//            log.debug("\n executeCommandToString output catched: " + cmd + " ===========");
//            int exitValue = p.waitFor();
            p.destroy();
//            log.debug("\n executeCommandToString process destroyed: " + cmd + " ===========");
            return result.toString();
        } catch (IOException e) {
//            e.printStackTrace();
        }
//        catch (InterruptedException e) {
////            throw new RuntimeException(e);
//        }
        finally {
            close(stdError);
            close(stdInput);
//            log.debug("\n executeCommandToString streams closed: " + cmd + " ===========");
        }
        return null;
    }
    public static String[] executeCommandToStrArr(String cmd) {
        ArrayList<String> result = new ArrayList<>(5);
//        log.debug("\n executeCommandToStrArr: " + cmd + " ===========");
        BufferedReader stdInput = null;
        BufferedReader stdError = null;
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
//            int exitValue = p.waitFor();
            String s;
            // read the output from the command
            while ((s = stdInput.readLine()) != null) {
                result.add(s);
            }
            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
                logger.warning(s);
            }
//            log.debug("\n executeCommandToStrArr: Finishing command: " + cmd + " ===========");
            p.destroy();
//            log.debug("\n executeCommandToStrArr: Destroying process: " + cmd + " ===========");
            return result.toArray(new String[0]);
        } catch (IOException e) {
//            e.printStackTrace();
        }
//        catch (InterruptedException e) {
////            throw new RuntimeException(e);
//        }
        finally {
            close(stdError);
            close(stdInput);
//            log.debug("\n executeCommandToStrArr: Exiting from command: " + cmd + " ===========");
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
