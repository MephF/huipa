package com.huipa.huipa.repository;

import com.huipa.huipa.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
     
    /**
     * Busca una categoría por su nombre
     * @param nombre Nombre de la categoría
     * @return Optional con la categoría encontrada
     */
    Optional<Category> findByNombre(String nombre);
    
    /**
     * Verifica si existe una categoría con el nombre especificado
     * @param nombre Nombre de la categoría
     * @return true si existe, false en caso contrario
     */
    boolean existsByNombre(String nombre);
    
    /**
     * Verifica si existe una categoría con el nombre especificado excluyendo un ID
     * Útil para validaciones en actualizaciones
     * @param nombre Nombre de la categoría
     * @param categoryId ID de la categoría a excluir
     * @return true si existe, false en caso contrario
     */
    boolean existsByNombreAndCategoryIdNot(String nombre, Long categoryId);
}

