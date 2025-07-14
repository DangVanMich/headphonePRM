package com.example.headphones_ecommerce_store.repository;

public class Result<T> {
    private T data;
    private Exception error;
    private Status status;

    private Result(Status status, T data, Exception error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public Exception getError() {
        return error;
    }

    public Status getStatus() {
        return status;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(Status.SUCCESS, data, null);
    }

    public static <T> Result<T> error(Exception error, T data) { // Optionally pass stale data
        return new Result<>(Status.ERROR, data, error);
    }

    public static <T> Result<T> error(Exception error) {
        return new Result<>(Status.ERROR, null, error);
    }

    public static <T> Result<T> loading(T data) { // Optionally pass initial/stale data
        return new Result<>(Status.LOADING, data, null);
    }

    public static <T> Result<T> loading() {
        return new Result<>(Status.LOADING, null, null);
    }


    public enum Status {
        SUCCESS,
        ERROR,
        LOADING
    }
}