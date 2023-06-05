package com.servlet.SPI;

import org.apache.commons.fileupload.FileItem;

import javax.servlet.ServletException;
import java.io.File;

public interface FileStorage {
    void writeFile(FileItem fileItem);
    File readFile(String fileName) throws ServletException;
}
