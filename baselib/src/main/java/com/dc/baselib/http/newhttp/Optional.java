package com.dc.baselib.http.newhttp;

import java.util.NoSuchElementException;

public class Optional<M> {
    private final M optional;//接受到的返回结果

    public Optional(M optional) {
        this.optional = optional;
    }

    public boolean isEmpty() {
        return this.optional == null;
    }

    public M get() {
        if (optional == null) {
            throw new NoSuchElementException("No value present");
        }
        return optional;
    }

    public M getIncludeNull() {
        return optional;
    }
}
