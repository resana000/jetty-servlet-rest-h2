package com.servlet;

import com.servlet.SPI.FileStorage;
import com.servlet.dto.FileInfo;
import com.servlet.exception.FileConstraintException;
import com.servlet.service.FileConstraintsCheckService;
import com.servlet.service.FileRequestService;
import com.servlet.utils.JsonUtils;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.ServiceLoader;

import static org.mockito.ArgumentMatchers.any;

public class FileServiceTest {

    @Mock
    FileRequestService fileRequestService;

    @Mock
    JsonUtils jsonUtils;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    ServletFileUpload servletFileUpload;

    @InjectMocks
    ServletFileController servletFileController;

    @Before
    public void setUp() {
        servletFileController = new ServletFileController();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetFilesSucces() throws FileUploadException, ServletException, IOException {
        Mockito.when(fileRequestService.getAll()).thenReturn(Collections.singletonList(new FileInfo()));
        servletFileController.doGet(request, response);
        Mockito.verify(request, Mockito.atLeast(1)).getPathInfo();
    }

    @Test
    public void testPostFilesSucces() throws FileUploadException, ServletException, IOException {
        Mockito.when(fileRequestService.getAll()).thenReturn(Collections.singletonList(new FileInfo()));
        servletFileController.doPost(request, response);
        Mockito.verify(request, Mockito.atLeast(1)).getPathInfo();
        Mockito.verify(servletFileUpload, Mockito.atLeast(1)).parseRequest((HttpServletRequest) any());
        Mockito.verify(jsonUtils, Mockito.atLeast(1)).sendAsJson(any(), any());
    }

    @Test(expected = FileConstraintException.class)
    public void checkFileType_ShouldBeThrow() {
        FileConstraintsCheckService.check(new FileInfo(1L, "test", 1000L, "mp3"));
    }

    @Test
    public void testServiceConstructorContract() {
        Assert.assertNotNull(new FileRequestService().getFileRepository());
    }

    @Test
    public void checkSPIImplementation() throws Exception {
        ServiceLoader<FileStorage> serviceLoader = ServiceLoader.load(FileStorage.class);
        Assert.assertNotNull(serviceLoader.iterator().next());
    }

}