package com.servlet.service;

import com.servlet.SPI.FileStorage;
import com.servlet.h2.FileRepository;
import org.apache.commons.fileupload.FileItem;

import javax.servlet.ServletException;
import javax.ws.rs.ext.Provider;
import java.io.File;

@Provider
public class LinuxFileStorageImpl implements FileStorage {

    private static final String FILES_DIR = "files";
    private FileRepository fileRepository;

    public LinuxFileStorageImpl() {
        this.fileRepository = new FileRepository();
    }

    @Override
    public void writeFile(FileItem fileItem) {
        new File(FILES_DIR).mkdir();
        File file = new File(FILES_DIR + File.separator + fileItem.getName());
        try {
            fileItem.write(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public File readFile(String fileName) throws ServletException {
        if (fileName == null ) {
            throw new ServletException("File name can't be null");
        }
        File file = new File(FILES_DIR + File.separator + fileName);
        if (!file.exists()) {
            throw new ServletException("File doesn't exists on server.");
        }
        return file;
    }
}
