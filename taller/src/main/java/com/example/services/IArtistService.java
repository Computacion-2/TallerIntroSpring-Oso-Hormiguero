package com.example.services;

import java.util.List;

import com.example.model.Artist;

public interface IArtistService {

    Artist create(String name, String nationality);
    List<Artist> findAll();
    
}
