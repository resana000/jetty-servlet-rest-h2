package com.servlet.resteasy;

import com.servlet.dto.ExceptionMessage;
import com.servlet.exception.FileConstraintException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class FileApiExceptionMapper implements ExceptionMapper<FileConstraintException> {

    @Override
    public Response toResponse(FileConstraintException e) {
        return Response.status(400).entity(new ExceptionMessage(e.getMessage())).build();
    }
}
