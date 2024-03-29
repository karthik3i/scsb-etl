package org.recap.model.etl;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by rajeshbabuk on 22/6/16.
 */
public class EtlLoadRequest {

    private String inputDirectoryPath;
    private Integer numberOfThreads;
    private Integer batchSize;
    private MultipartFile file;
    private String userName;

    public String getInputDirectoryPath() {
        return inputDirectoryPath;
    }

    public void setInputDirectoryPath(String inputDirectoryPath) {
        this.inputDirectoryPath = inputDirectoryPath;
    }

    public Integer getNumberOfThreads() {
        return numberOfThreads;
    }

    public void setNumberOfThreads(Integer numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
