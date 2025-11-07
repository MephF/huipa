package com.huipa.huipa.service.impl;

import com.huipa.huipa.dto.CategoryRequestDTO;
import com.huipa.huipa.dto.CategoryResponseDTO;
import com.huipa.huipa.entity.Category;
import com.huipa.huipa.repository.CategoryRepository;
import com.huipa.huipa.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    } 
    
    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {
        // Validar que no exista una categoría con el mismo nombre
        if (categoryRepository.existsByNombre(requestDTO.getNombre())) {
            throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + requestDTO.getNombre());
        }
        
        Category category = new Category();
        category.setNombre(requestDTO.getNombre());
        category.setDescripcion(requestDTO.getDescripcion());
        
        Category savedCategory = categoryRepository.save(category);
        return mapToResponseDTO(savedCategory);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + id));
        return mapToResponseDTO(category);
    }
    
    @Override
    @Transactional
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + id));
        
        // Validar que no exista otra categoría con el mismo nombre (excluyendo la actual)
        if (categoryRepository.existsByNombreAndCategoryIdNot(requestDTO.getNombre(), id)) {
            throw new IllegalArgumentException("Ya existe otra categoría con el nombre: " + requestDTO.getNombre());
        }
        
        category.setNombre(requestDTO.getNombre());
        category.setDescripcion(requestDTO.getDescripcion());
        
        Category updatedCategory = categoryRepository.save(category);
        return mapToResponseDTO(updatedCategory);
    }
    
    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Categoría no encontrada con ID: " + id);
        }
        categoryRepository.deleteById(id);
    }
    
    /* Mapea una entidad Category a un CategoryResponseDTO */
    private CategoryResponseDTO mapToResponseDTO(Category category) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setCategoryId(category.getCategoryId());
        dto.setNombre(category.getNombre());
        dto.setDescripcion(category.getDescripcion());
        dto.setCreatedAt(category.getCreatedAt());
        return dto;
    }
}

