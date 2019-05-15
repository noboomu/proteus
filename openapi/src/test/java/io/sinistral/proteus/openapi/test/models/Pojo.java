package io.sinistral.proteus.openapi.test.models;

public class Pojo
{

    public Long id;

    public String name;


    public Pojo(Long id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public Pojo()
    {
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
