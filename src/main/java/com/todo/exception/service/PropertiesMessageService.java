package com.todo.exception.service;

public interface PropertiesMessageService {
    String getProperty(String source);

    String getProperty(String name, Object... params);
}