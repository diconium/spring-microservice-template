package com.diconium.microservice.model.dto;

public class CustomDTO {

    private String foo;
    private String bar;

    public CustomDTO(){}

    public CustomDTO(String foo, String bar) {
        this.foo = foo;
        this.bar = bar;
    }

    public String getFoo() {
        return foo;
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }

    public String getBar() {
        return bar;
    }

    public void setBar(String bar) {
        this.bar = bar;
    }

    @Override
    public String toString() {
        return "CustomDTO{" +
            "foo='" + foo + '\'' +
            ", bar='" + bar + '\'' +
            '}';
    }
}
