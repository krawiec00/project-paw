package com.tss.repositories;

import com.tss.entities.Image;
import com.tss.entities.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImageRepository extends JpaRepository<Image, Long> {
    
    @Override
    List<Image> findAll();
    @Query("SELECT i FROM Image i WHERE i.user = ?1")
    List<Image> findAllByUser(User user);
}
