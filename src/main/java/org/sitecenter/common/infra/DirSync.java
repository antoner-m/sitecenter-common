package org.sitecenter.common.infra;

import org.sitecenter.common.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DirSync {
    private File fileSourceDir;
    private File fileDestDir;
    private File fileMoveDir;
    private Path pathDestDirBase;

    /**
     * Class scans recursively destDir for files.
     * If file doesn't exists OR NEWER (lastmodify) in sourceDir then it will be moved to moveDir with same path relatively to destDir.
     * @param sourceDir
     * @param destDir
     * @param moveDir
     */
    public DirSync(String sourceDir, String destDir, String moveDir) {
        fileSourceDir = new File(sourceDir);
        fileDestDir = new File(destDir);
        fileMoveDir = new File(moveDir);
    }

    public void scan() {
        checkDirExists(fileSourceDir);
        checkDirExists(fileDestDir);
        checkDirExists(fileMoveDir);

        pathDestDirBase = fileDestDir.toPath();

        System.out.println("Start syncing:");
        System.out.println("Source:"+fileSourceDir.getAbsolutePath());
        System.out.println("Destination:"+fileDestDir.getAbsolutePath());
        System.out.println("Move:"+fileMoveDir.getAbsolutePath());

        scanDir(fileDestDir);
    }

    private void scanDir(File dir) {
        // Get all files from a directory.
        File[] fList = dir.listFiles();
        if(fList != null)
            for (File file : fList) {
                if (file.isFile()) {
                    checkFile(file);
                } else if (file.isDirectory()) {
                    scanDir(file);
                }
            }
        removeDirIfEmpty(dir);
    }

    private void removeDirIfEmpty(File dir) {
        // Get all files from a directory.
        File[] fList = dir.listFiles();
        if(fList == null || fList.length == 0) {
            try {
                Files.delete(dir.toPath());
                System.out.println(dir.getAbsolutePath()+" directory empty - deleted.");
            } catch (IOException e) {
                System.out.println("Cannot delete directory:"+ dir.getAbsolutePath()+":");
                e.printStackTrace();
            }
        }
    }
    private void checkFile(File file) {
        Path pathAbsolute = file.toPath();
        Path pathRelative = pathDestDirBase.relativize(pathAbsolute);

        Path pathSource= Paths.get(fileSourceDir.getAbsolutePath(), pathRelative.toString());
        File sourceFile = new File(pathSource.toUri());
        if (!sourceFile.exists() || sourceFile.lastModified() > file.lastModified()) {
            Path pathMove= Paths.get(fileMoveDir.getAbsolutePath(), pathRelative.toString());
            File destFile = new File(pathMove.toUri());
            try {
                FileUtil.createFolder(destFile.getParentFile().getAbsolutePath());
                Files.move(pathAbsolute, pathMove, StandardCopyOption.REPLACE_EXISTING);
                System.out.println(pathSource.toAbsolutePath().toString()+" moved to "+ destFile.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("Cannot move file from "+pathSource.toAbsolutePath().toString()+" to "+ destFile.getAbsolutePath()+" skipping:");
                e.printStackTrace();
            }
        }
    }

    private void checkDirExists(File file) {
        if (!file.exists()) throw new RuntimeException("Directory "+file.getAbsolutePath()+" doesn't exists.");
        if (!file.isDirectory()) throw new RuntimeException("File "+file.getAbsolutePath()+" is not a directory.");
    }



    public static void main(String args[]) {
        if (args==null || args.length != 3) {
            System.out.println("Usage: DirSync SRC DST MOVE_TO");
            System.out.println("SRC - dir where source files stored");
            System.out.println("DST - destination dir, we compare it contents with source, if file doesn't exists at source then it will be moved to MOVE_TO.");
            return;
        }
        String sourceDir = args[0];
        String destDir = args[1];
        String moveDir = args[2];
        DirSync dirSync = new DirSync(sourceDir,destDir,moveDir);
        dirSync.scan();
    }
}
