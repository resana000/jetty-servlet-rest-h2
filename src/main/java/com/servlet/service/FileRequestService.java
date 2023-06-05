package com.servlet.service;

import com.servlet.dto.FileInfo;
import com.servlet.h2.FileRepository;
import lombok.Data;
import org.apache.commons.fileupload.FileItem;

import java.util.List;

@Data
public class FileRequestService {

    private FileRepository fileRepository;

    public FileRequestService() {
        this.fileRepository = new FileRepository();
    }

    public FileInfo mapToEntity(FileItem fileItem) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setName(fileItem.getName());
        fileInfo.setSize(fileItem.getSize());
        fileInfo.setType(fileItem.getContentType());
        return fileInfo;
    }

    public FileInfo saveDB(FileInfo fileInfo) {
        fileRepository.create(fileInfo);
        return fileInfo;
    }

    public FileInfo getById(Long id) {
        return fileRepository.getById(id);
    }

    public List<FileInfo> getAll() {
        return fileRepository.getAll();
    }
}
