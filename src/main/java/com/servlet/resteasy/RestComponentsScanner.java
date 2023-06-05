package com.servlet.resteasy;

import com.servlet.ServletFileController;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.Application;

/**
 * @Deprecated
 * see {@link ServletFileController}
 */

public class RestComponentsScanner extends Application {
    private static Set services = new HashSet();

    public RestComponentsScanner() {
        services.add(new FileController());
        services.add(new FileApiExceptionMapper());
    }

    @Override
    public Set getSingletons() {
        return services;
    }

    public static Set getServices() {
        return services;
    }
}


