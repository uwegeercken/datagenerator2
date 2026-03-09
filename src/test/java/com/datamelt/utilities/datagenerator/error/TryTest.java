package com.datamelt.utilities.datagenerator.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TryTest
{
    @Test
    void testSimpleMap()
    {
        Try<String> testString = Try.of(() -> "test");
        Try<String> result = testString.map(String::toUpperCase);
        assertEquals("TEST", result.getResult());
    }

    @Test
    void testSuccess()
    {
        Try<String> testString = Try.of(() -> "test--1234");
        assertTrue(testString.isSuccess());
    }

    @Test
    void testFailure()
    {
        // division by zero !
        Try<Integer> testInteger = Try.of(() -> 100/0);
        assertTrue(testInteger.isFailure());
    }
}