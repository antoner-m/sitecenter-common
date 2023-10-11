package org.sitecenter.common.util;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Helper. Useful class for working with files, zip archives, etc.
 */
public class ZipUtil {
     //============================================================================
    //Working with zip files

    /**
     * Expands the contents of a zip file in the specified directory
     *
     * @param vzipfile     pointer to zip file
     * @param dirtoextract pointer to the directory to expand the archive.
     * @throws Exception if I/O operations fail
     */
    public static void zipFileExtract(File vzipfile, File dirtoextract) throws Exception {
        if (vzipfile == null) throw new Exception("Zip filename is null.");
        if (!vzipfile.exists()) throw new Exception("Cannot find file : " + vzipfile.getAbsolutePath());
        if (dirtoextract == null) throw new Exception("Directory for extract doesnt set ");
        if (!dirtoextract.exists()) dirtoextract.mkdirs();

        try (ZipInputStream zin = openZipInputStream(vzipfile, false)) {
            if (zin == null) throw new Exception("Cannot open zip file " + vzipfile.getAbsolutePath());

            ZipEntry e;
            while ((e = zin.getNextEntry()) != null) {
                File currFile = unzip(zin, e, dirtoextract);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * Creates a zip file based on the given directory.
     * The file includes all sub-directories and files from the specified directory.
     *
     * @param dirtosave pointer to the directory from which to create the archive.
     * @param vzipfile pointer to the zip file to be created, the file will be overwritten.
     * @throws Exception if I/O operations fail
     */
    public static void zipFileCreate(File dirtosave, File vzipfile) throws Exception {
        if (vzipfile.exists()) vzipfile.delete();

        vzipfile.createNewFile();
        try (OutputStream out = new FileOutputStream(vzipfile);
             ZipOutputStream zout = new ZipOutputStream(out);) {
            //зипуем всю директорию
            addDirToZip(dirtosave, zout);
        } catch (Exception ex) {
            throw ex;
        }
    }

    // ===========================================================================
    // Utility Methods
    // ---------------------------------------------------------------------------
    private static void addDirToZip(File dirToAdd, ZipOutputStream zout) {
        addDirToZip(dirToAdd, dirToAdd, zout);
    }

    private static void addDirToZip(File baseDir, File dirToAdd, ZipOutputStream zout) {
        File[] fa = dirToAdd.listFiles();
        if (fa == null || fa.length == 0) return;
        String basepath = baseDir.getAbsolutePath();
        String dirpath = dirToAdd.getAbsolutePath();
        String subpath = dirpath.substring(basepath.length());
//    System.err.println("subpath="+subpath);
        if (subpath == null) subpath = "";
        for (File file : fa) {
            String fname = subpath + "/" + file.getName();

            if (file.isFile()) {
                try {
                    zip(zout, file, fname);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (file.isDirectory()) {
                ZipEntry e = new ZipEntry(fname + "/");
                try {
                    zout.putNextEntry(e);
                    addDirToZip(baseDir, file, zout);
                    zout.closeEntry();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static ZipInputStream openZipInputStream(File vzipfile, boolean createifnotexist) {
        try {
            if (!vzipfile.exists())
                if (createifnotexist) vzipfile.createNewFile();
                else return null;
            InputStream in = new BufferedInputStream(new FileInputStream(vzipfile));
            ZipInputStream zin = new ZipInputStream(in);
            return zin;
        } catch (Exception ozex) {
            return null;
        }
    }

    private static File unzip(ZipInputStream zin, ZipEntry e, File destdir) throws IOException {

        String s = e.getName();
        s = s.replace('/', File.separatorChar);
        s = s.replace('\\', File.separatorChar);

        String name = s;
        String path = destdir.getAbsolutePath() + File.separator + name;

        File f = new File(path);
        File parentdir = f.getParentFile();
        if (!parentdir.exists()) {
            parentdir.mkdirs();
        }

        if (e.isDirectory()) {
            if (!f.exists()) {
                f.mkdirs();
            }
        } else {
            if (!f.exists()) {
                f.createNewFile();
            }
            try (FileOutputStream out = new FileOutputStream(f); BufferedOutputStream bout = new BufferedOutputStream(out);) {
                byte[] b = new byte[512];
                int len = 0;
                while ((len = zin.read(b)) != -1) {
                    bout.write(b, 0, len);
                }
            } catch (Exception ex) {
                throw ex;
            }

        }
        return f;
    }

    private static void zip(ZipOutputStream zout, File file, String filename) throws IOException {
        ZipEntry e = new ZipEntry(filename);
        zout.putNextEntry(e);
        try (FileInputStream in = new FileInputStream(file); BufferedInputStream bis = new BufferedInputStream(in)) {
                byte[] b = new byte[512];
                int len = 0;
                while ((len = bis.read(b)) != -1) {
                    zout.write(b, 0, len);
                }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        zout.closeEntry();
    }
};