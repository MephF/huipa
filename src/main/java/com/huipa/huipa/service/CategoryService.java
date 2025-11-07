package com.huipa.huipa.service;

import java.util.List;

import com.huipa.huipa.dtos.CategoryRequestDTO;
import com.huipa.huipa.dtos.CategoryResponseDTO;

public interface CategoryService { 
    
    /* Crea una nueva categoría */
    CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO);
    
    /* Obtiene todas las categorías */
    List<CategoryResponseDTO> getAllCategories();
    
    /* Obtiene una categoría por su ID */
    CategoryResponseDTO getCategoryById(Long id);
    
    /** Actualiza una categoría existente */
    CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO);
    /* Elimina una categoría por su ID     */
    void deleteCategory(Long id);
}
