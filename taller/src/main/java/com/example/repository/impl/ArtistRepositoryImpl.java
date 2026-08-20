package com.example.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.example.model.Artist;
import com.example.repository.IArtistRepository;

public class ArtistRepositoryImpl implements IArtistRepository {
    
    private List<Artist> artists = new ArrayList<>();
    private int nextId = 1;
    private Logger logger = Logger.getLogger(ArtistRepositoryImpl.class.getName());

    @Override
    public void init(){
        logger.info("Bean inicializandose");
    }

    @Override
    public void destroy() {
        logger.info("Bean a punto de destruirse");
    }

    @Override
    public Artist create(Artist artist){
        if(artist == null) {
            return null;
        }

        artist.setId(nextId);
        nextId++;
        artist.add(artist);

        return artist;
    }

    @Override
    public List<Artist> findAll() {
        return artists;
    }
}
