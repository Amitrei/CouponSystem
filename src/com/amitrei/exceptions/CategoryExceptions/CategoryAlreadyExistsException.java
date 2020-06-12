package com.amitrei.exceptions.CategoryExceptions;

public class CategoryAlreadyExistsException extends  Exception {

    public CategoryAlreadyExistsException(String category) {
        super("Category: "+category+" already exists.");
    }
}
