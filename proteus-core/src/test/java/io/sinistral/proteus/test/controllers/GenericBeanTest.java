package io.sinistral.proteus.test.controllers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GenericBeanTest {

    private static final Logger logger = LoggerFactory.getLogger(GenericBean.class.getName());

    @Override
    @BeforeAll
    protected void setUp()
    {

        super.setUp();
    }

}