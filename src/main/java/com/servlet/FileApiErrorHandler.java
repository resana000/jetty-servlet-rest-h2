package com.servlet;

import com.servlet.exception.ApiException;
import com.servlet.exception.FileConstraintException;
import org.eclipse.jetty.server.handler.ErrorHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Objects;

public class FileApiErrorHandler extends ErrorHandler {

    private static final Class[] HANDLE_EXCEPTIONS = {
            FileConstraintException.class,
            ServletException.class
    };

    @Override
    protected void writeErrorPageHead(HttpServletRequest request, Writer writer, int code, String message) throws IOException {
        writer.write("BAD_REQUEST \n");
    }

    @Override
    protected void writeErrorPageBody(HttpServletRequest request, Writer writer, int code, String message, boolean showStacks) throws IOException {
        Object ex = request.getAttribute("javax.servlet.error.exception");

        if (Objects.isNull(ex)) {
            return;
        }

        if (Arrays.stream(HANDLE_EXCEPTIONS).anyMatch(e -> e.equals(ex.getClass()))) {
            writer.write("\n" + ((ApiException) ex).getMessage());
        }

    }
}
