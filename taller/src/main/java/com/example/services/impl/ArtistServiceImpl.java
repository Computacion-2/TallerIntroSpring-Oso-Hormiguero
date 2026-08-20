package com.example.services.impl;

import com.example.model.Artist;
import com.example.repository.IArtistRepository;
import com.example.services.IArtistService;

public class ArtistServiceImpl implements IArtistService {
    
    private IArtistRepository artistRepository;

    public ArtistServiceImpl(IArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public Artist create(String name, String nationality) {
        Artist artist = new Artist(0, name, nationality);
        return artistRepository.create(artist);
    }

    @Override
    public List<Artist> findAll(){
        return artistRepository.findAll();
    }

}
