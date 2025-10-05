package com.chronus.app.utils;

import java.util.Objects;

public class HttpResponse<T> {
    private final int status;
    private final String message;
    private final T data;

    public HttpResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        HttpResponse<?> other = (HttpResponse<?>) obj;

        if (status != other.status) return false;
        if (!Objects.equals(message, other.message)) return false;
        return Objects.equals(data, other.data);
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(status);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }
}
