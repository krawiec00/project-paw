package com.tss.repositories;

import com.tss.entities.Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    
    @Override
    List<Image> findAll();
}
