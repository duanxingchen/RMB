package dzr.common.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author dzr
 * @Date 2022/10/14 18:09
 * @Version 1.0
 * @Description:
 */
public class FileUtils {

    public static List<File> getAllFilePathInDir(String fileDir) {
        List<File>  fileList = new ArrayList<>();
        File file = new File(fileDir);
        File[] files = file.listFiles();

        if (files == null) {
            return fileList;
        }

        for (File f : files) {
            if (f.isFile()) {
                fileList.add(f);
            } else if (f.isDirectory()) {
                getAllFilePathInDir(f.getAbsolutePath());
            }
        }
        return fileList;
    }


    public static List<File> getAllFilePathInDirWithName(String fileDir,String name) {
        return getAllFilePathInDirWithName(fileDir,fileDir,null);
    }


    public static List<File> getAllFilePathInDirWithName(String fileDir,String name,String suffix) {
        List<File> fileList = new ArrayList<>();
        getAllFilePathInDir(fileDir).forEach(file -> {
            if (file.getName().equals(name+suffix))
                fileList.add(file);
        });
        return fileList;
    }
}
