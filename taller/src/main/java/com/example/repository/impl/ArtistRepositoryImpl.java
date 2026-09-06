package com.example.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.stereotype.Repository;

import com.example.model.Artist;
import com.example.model.Track;
import com.example.repository.IArtistRepository;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Repository("artistRepository")
public class ArtistRepositoryImpl implements IArtistRepository {
    
    private final List<Artist> artists = new ArrayList<>();
    private int nextId = 1;
    private final Logger logger = Logger.getLogger(ArtistRepositoryImpl.class.getName());

    @Override
    @PostConstruct
    public void init() {
        logger.info("ArtistRepositoryImpl: Bean inicializado con @PostConstruct (@Repository)");
    }

    @Override
    @PreDestroy
    public void destroy() {
        logger.info("ArtistRepositoryImpl: Bean a punto de destruirse con @PreDestroy");
    }

    @Override
    public Artist create(Artist artist) {
        if (artist == null) {
            return null;
        }

        artist.setId(nextId++);
        artists.add(artist);
        return artist;
    }

    @Override
    public List<Artist> findAll() {
        return new ArrayList<>(artists);
    }

    @Override
    public Optional<Artist> findById(Integer id) {
        if (id == null) {
            return Optional.empty();
        }

        return artists.stream()
                .filter(a -> id.equals(a.getId()))
                .findFirst();
    }

    @Override
    public Optional<Artist> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Optional.empty();
        }

        String search = name.trim();
        return artists.stream()
                .filter(a -> a.getName() != null && a.getName().equalsIgnoreCase(search))
                .findFirst();
    }

    @Override
    public boolean deleteById(Integer id) {
        if (id == null) {
            return false;
        }

        Optional<Artist> artistOpt = findById(id);
        if (artistOpt.isPresent()) {
            Artist artist = artistOpt.get();
            // Desvincular de todas las pistas asociadas
            if (artist.getTracks() != null) {
                List<Track> associatedTracks = new ArrayList<>(artist.getTracks());
                for (Track track : associatedTracks) {
                    track.removeArtist(artist);
                }
            }
            artists.remove(artist);
            return true;
        }

        return false;
    }
}
