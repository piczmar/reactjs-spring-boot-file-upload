package com.example.uploadform.utils;

import java.io.File;
import java.util.Arrays;

public class TestUtils {
    public static void cleanFolderRecursive(File folder) {
        if (folder.exists() && folder.isDirectory()) {
            Arrays.asList(folder.listFiles()).forEach(File::delete);
        }
        folder.delete();
    }

    public static void cleanFolderRecursive(String folderPath) {
        TestUtils.cleanFolderRecursive(new File(folderPath));
    }
}
