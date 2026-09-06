package com.example.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Artist;
import com.example.repository.IArtistRepository;
import com.example.services.IArtistService;

@Service("artistService")
public class ArtistServiceImpl implements IArtistService {

    private IArtistRepository artistRepository;

    public ArtistServiceImpl() {
    }

    @Autowired
    public ArtistServiceImpl(IArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public void setArtistRepository(IArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public Artist create(String name, String nationality) {
        Artist artist = new Artist(0, name, nationality);
        return artistRepository.create(artist);
    }

    @Override
    public Artist create(Artist artist) {
        return artistRepository.create(artist);
    }

    @Override
    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    @Override
    public Artist findById(Integer id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Artista no encontrado con ID: " + id));
    }

    @Override
    public Artist findByName(String name) {
        return artistRepository.findByName(name).orElse(null);
    }

    @Override
    public boolean deleteById(int id) {
        return artistRepository.deleteById(id);
    }
}
