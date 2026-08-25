package io.sinistral.proteus.openapi.test.models;

import java.util.List;

public class PagedResponse<T> {
    public List<T> data;
    public int total;

    public PagedResponse() {}
    public PagedResponse(List<T> data, int total) {
        this.data = data;
        this.total = total;
    }
}
