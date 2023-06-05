package com.servlet;

import com.servlet.SPI.FileStorage;
import com.servlet.dto.FileInfo;
import com.servlet.service.FileConstraintsCheckService;
import com.servlet.service.FileRequestService;
import com.servlet.utils.JsonUtils;
import lombok.SneakyThrows;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class ServletFileController extends HttpServlet {

    private static final String GET_ALL_POINT = "/files";
    private static final String UPLOAD_POINT = "/upload";
    private static final String DOWNLOAD_POINT = "/download";

    private ServletFileUpload servletFileUpload;
    private ServiceLoader<FileStorage> fileStorages;
    private FileRequestService fileRequestService;
    private JsonUtils jsonUtils;

    @Override
    public void init() throws ServletException {
        this.fileStorages = ServiceLoader.load(FileStorage.class);
        DiskFileItemFactory fileFactory = new DiskFileItemFactory();
        fileFactory.setRepository(new File("files"));
        this.servletFileUpload = new ServletFileUpload(fileFactory);
        this.fileRequestService = new FileRequestService();
        this.jsonUtils = new JsonUtils();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals(GET_ALL_POINT)) {
            jsonUtils.sendAsJson(response, fileRequestService.getAll());
            return;
        } else if (pathInfo.contains(DOWNLOAD_POINT) && pathInfo.split("/").length == 3) {
            Long fileId = Long.valueOf(pathInfo.split("/")[pathInfo.split("/").length - 1]);
            try {
                FileInfo fileInfo = fileRequestService.getById(fileId);
                File file = fileStorages.iterator().next().readFile(fileInfo.getName());
                FileInputStream fileInputStream = new FileInputStream(file);
                String mimeType = getServletContext().getMimeType(file.getAbsolutePath());
                response.setContentType(mimeType != null ? mimeType : "application/octet-stream");
                response.setContentLength((int) file.length());
                response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

                ServletOutputStream os = response.getOutputStream();
                byte[] bufferData = new byte[1024];
                int read = 0;
                while ((read = fileInputStream.read(bufferData)) != -1) {
                    os.write(bufferData, 0, read);
                }
                os.flush();
                os.close();
                fileInputStream.close();
                return;
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ;
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
    }

    @SneakyThrows
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals(UPLOAD_POINT)) {

            List<FileInfo> fileInfoList = new ArrayList<>();
            List<FileItem> fileItemsList = servletFileUpload.parseRequest(request);
            Iterator<FileItem> fileItemsIterator = fileItemsList.iterator();
            while (fileItemsIterator.hasNext()) {
                FileItem fileItem = fileItemsIterator.next();
                FileInfo fileInfo = fileRequestService.mapToEntity(fileItem);
                FileConstraintsCheckService.check(fileInfo);
                fileInfoList.add(fileInfo);
                fileStorages.forEach(s -> {
                    s.writeFile(fileItem);
                });
                fileRequestService.saveDB(fileInfo);
            }

            jsonUtils.sendAsJson(response, fileInfoList);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
    }
}