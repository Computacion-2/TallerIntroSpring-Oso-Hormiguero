package com.example.services;

import java.util.List;
import com.example.model.Artist;

public interface IArtistService {
    Artist create(String name, String nationality);
    Artist create(Artist artist);
    List<Artist> findAll();
    Artist findById(Integer id);
    Artist findByName(String name);
    boolean deleteById(int id);
}
