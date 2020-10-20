package com.maolabs.maobank.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "/temp/";

    public String getLocation(String subfolder) {
        return location + subfolder;
    }

    public String getLocation(String subfolder, String fileName) {
        return location + subfolder + "/" + fileName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
