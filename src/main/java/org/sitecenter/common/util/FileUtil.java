package org.sitecenter.common.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Helper, often useful class for working with files, zip archives, etc.
 */
public class FileUtil {
    public static final int COPY_BUFFER_SIZE = 65536;

    public static String loadFileAsString(String filename) {
        File fl = new File(filename);
        return loadFileAsString(fl);
    }

    public static String loadFileAsString(File fl) {
        return loadFileAsString(fl, System.getProperty("file.encoding"));
    }

    public static String loadFileAsString(File fl, String encoding) {
        if (!fl.exists()) return null;

        StringBuilder sb = new StringBuilder((int) fl.length());
        try {
            // first, read the file and store the changes
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fl), encoding));
            String line = in.readLine();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = in.readLine();
            }
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static void createFileFromString(File fl, String content) throws IOException {
        FileWriter fw = new FileWriter(fl);
        fw.write(content);
        fw.close();
    }

    public static boolean createFolder(String directory) {
        File dir = new File(directory);
        boolean result = false;
        try {
            result = dir.mkdirs();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static void copyFileToDir(File source, File destDir) throws IOException {
        if ((source == null) || (!source.exists())) return;

        copyFile(source, new File(destDir, source.getName()));
    }
    public static void copyFile(File source, File destFile) throws IOException {
        Path copied = source.toPath();
        Path originalPath = destFile.toPath();
        Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void copyFileBuffer(File source, File destFile) throws IOException {
        if (!source.exists()) return;
        //Сделано для поддержки старого метода, в котором destfile Был просто директорией.
        if (destFile.exists() && destFile.isDirectory()) destFile = new File(destFile, source.getName());

        if (destFile.exists()) {
            throw new IOException("Cannot create file: " + destFile.getPath() + ", file already exists.");
        }
        FileInputStream in = null;
        FileOutputStream out = null;
        BufferedInputStream inb = null;
        BufferedOutputStream outb = null;
        try {
            in = new FileInputStream(source);
            inb = new BufferedInputStream(in, COPY_BUFFER_SIZE);
            out = new FileOutputStream(destFile);
            outb = new BufferedOutputStream(out, COPY_BUFFER_SIZE);

            // Перекачка
            byte b[] = new byte[512];
            int len = 0;
            while ((len = inb.read(b)) != -1) {
                outb.write(b, 0, len);
            }
            inb.close();
            outb.close();
        } finally {
            if (inb != null) inb.close();
            if (in != null) in.close();
            if (outb != null) outb.close();
            if (out != null) out.close();
        }

    }

    public static void deleteFilesInDir(File dir, boolean clearsubdirs) {
        if (!dir.exists() || dir.isFile()) return;
        File[] fla = dir.listFiles();
        File curr = null;
        for (int i = 0; i < fla.length; i++) {
            curr = fla[i];
            if (curr.isDirectory()) {
                if (clearsubdirs) deleteFilesInDir(curr, clearsubdirs);
            } else {
                curr.delete();
            }
        }
    }

    ;

    public static void deleteDir(File dir) {
        if (!dir.exists() || dir.isFile()) return;
        deleteFilesInDir(dir, true);
        dir.delete();
    }

    public static void copyDir(File srcdir, File destdir) throws Exception {
        try {
            createFolder(destdir.getAbsolutePath());
            File[] fa = srcdir.listFiles();
            for (int i = 0; i < fa.length; i++)
                try {
                    if (fa[i].isDirectory()) {
                        File newdir = new File(destdir, fa[i].getName());
                        copyDir(fa[i], newdir);
                    }
                    copyFile(fa[i], destdir);
                } catch (Exception ex) {
                    System.err.println("problem copying file:from dir:" + fa[i].getAbsolutePath() + " to:" + destdir.getAbsolutePath());

                    ex.printStackTrace();
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
};