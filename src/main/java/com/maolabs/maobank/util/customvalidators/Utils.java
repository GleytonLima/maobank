package com.maolabs.maobank.util.customvalidators;

import javax.servlet.http.HttpServletRequest;

public class Utils {
    public static String getURLWithContextPath(HttpServletRequest request) {
        return new StringBuilder()
                .append(request.getScheme())
                .append("://")
                .append(request.getServerName())
                .append(":")
                .append(request.getServerPort())
                .append(request.getContextPath())
                .append("/api/v1").toString();
    }
}
