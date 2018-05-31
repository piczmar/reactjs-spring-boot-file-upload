package com.example.uploadform.exception;

public class DataCorruptedException extends RuntimeException {
    public DataCorruptedException(Throwable thr) {
        super(thr);
    }
}
