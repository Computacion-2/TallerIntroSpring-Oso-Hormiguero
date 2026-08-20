package com.example.repository;

import com.example.model.Artist;
import java.util.List;

public interface IArtistRepository {
    void init();
    void destroy();
    Artist create(Artist Artist);
    List<Artist> findAll();
}
