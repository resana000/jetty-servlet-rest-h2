package com.servlet.service;

import com.servlet.dto.FileInfo;
import com.servlet.exception.FileConstraintException;

import java.util.Arrays;

public class FileConstraintsCheckService {
    private static final String[] FILE_TYPES = {"txt", "csv"};
    private static final Long FILE_SIZE_BYTES = 10000L;

    public static void check(FileInfo fileInfo) {

        if (Arrays.stream(FILE_TYPES).noneMatch(f -> fileInfo.getType().contains(f))) {
            throw new FileConstraintException(String.format("File type can not be %s", fileInfo.getType()));
        }

        if (fileInfo.getSize() > FILE_SIZE_BYTES) {
            throw new FileConstraintException(String.format("File size can not be greater than %d", FILE_SIZE_BYTES));
        }
    }
}
