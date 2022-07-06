package io.sinistral.proteus.test.controllers;

public class GenericBean<S> {

    private S value;

    public GenericBean() {

    }

    public GenericBean(S value) {

        this.value = value;
    }

    public S getValue() {

        return value;
    }

    public void setValue(S value) {

        this.value = value;
    }

}
