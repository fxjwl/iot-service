package com.data.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUtil {

    public static String postFile(MultipartFile file,String path,boolean isUUID) {
        File dir = new File(path);
        if (!dir.exists()){
            dir.mkdirs();
        }
        String fileName = file.getOriginalFilename();

        if (isUUID) {
            int begin = file.getOriginalFilename().indexOf(".");
            int last = file.getOriginalFilename().length();

            fileName = UUID.randomUUID().toString().replace("-", "").toLowerCase() + file.getOriginalFilename().substring(begin, last);
        }
        File dest = new File(path + fileName);
        try {
            file.transferTo(dest);
            return fileName;
        } catch (IOException e) {
            return null;
        }
    }

    public static boolean deleteFile(String fileName)
    {
        File file = new File(fileName);
        return file.delete();
    }

}
