package com.example.repository;

import com.example.model.Artist;
import java.util.List;
import java.util.Optional;

public interface IArtistRepository {
    void init();
    void destroy();
    Artist create(Artist artist);
    List<Artist> findAll();
    Optional<Artist> findById(Integer id);
    Optional<Artist> findByName(String name);
    boolean deleteById(Integer id);
}
