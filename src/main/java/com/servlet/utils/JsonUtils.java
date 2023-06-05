package com.servlet.utils;

import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JsonUtils {
    public void sendAsJson(
            HttpServletResponse response,
            Object obj) throws IOException {

        response.setContentType("application/json");
        String res = new ObjectMapper().writeValueAsString(obj);
        PrintWriter out = response.getWriter();

        out.print(res);
        out.flush();
    }
}
