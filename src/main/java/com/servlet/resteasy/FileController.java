package com.servlet.resteasy;

import com.servlet.SPI.FileStorage;
import com.servlet.ServletFileController;
import com.servlet.exception.FileConstraintException;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @Deprecated
 * see {@link ServletFileController}
 */

@Path("/v2")
public class FileController {

    private static final int MAX_FILE_SIZE_BYTES = 100000;
    private static final String[] FILE_TYPES = {"txt", "csv"};

    @Produces("application/json")
    @GET
    @Path("/echo")
    public Response echoGet() {
        return Response.status(200).build();
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(MultipartFormDataInput input) throws IOException, FileConstraintException {
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        String fileName = uploadForm.get("fileName").get(0).getBodyAsString();
        List<InputPart> inputParts = uploadForm.get("attachment");


        for (InputPart inputPart : inputParts) {
            InputStream inputStream = inputPart.getBody(InputStream.class, null);
            byte[] bytes = IOUtils.toByteArray(inputStream);
            if (bytes.length > MAX_FILE_SIZE_BYTES) {
                throw new FileConstraintException(
                        String.format("File size greater than %d bytes", MAX_FILE_SIZE_BYTES));
            }

            if (Arrays.stream(FILE_TYPES).noneMatch(f -> f.equals(inputPart.getMediaType().getSubtype()))) {
                throw new FileConstraintException(
                        String.format("Required types: %s", Collections.singleton(FILE_TYPES)));
            }

            ServiceLoader<FileStorage> serviceLoader = ServiceLoader.load(FileStorage.class);
        }
        return Response.status(200).entity("Uploaded file name : " + fileName).build();
    }
}
