package com.servlet;

import com.servlet.dto.FileInfo;
import com.servlet.h2.FileRepository;
import com.servlet.service.LinuxFileStorageImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class FileRepositoryTest {

    @Before
    public void init() {
        WebServer.initH2Sql();
    }

    @Test
    public void testCreateFileInfoSuccess() {
        FileRepository fileRepository = new FileRepository();
        fileRepository.create(new FileInfo(1L, "test.txt", 100L, "txt"));
        Assert.assertNotNull(fileRepository.getById(1L));
    }
}