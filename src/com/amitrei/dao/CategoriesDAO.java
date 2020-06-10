package com.amitrei.dao;

import com.amitrei.beans.Category;

public interface CategoriesDAO {
    void loadOneCategories(Category category);
    void loadAllCategories();
    Boolean isCategoryExists(Category category);
}
