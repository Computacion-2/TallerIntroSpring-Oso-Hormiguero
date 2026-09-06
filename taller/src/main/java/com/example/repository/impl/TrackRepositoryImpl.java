package com.example.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.example.model.Artist;
import com.example.model.Track;
import com.example.repository.ITrackRepository;

public class TrackRepositoryImpl implements ITrackRepository {
    
    private final List<Track> tracks = new ArrayList<>();
    private int nextId = 1;
    private final Logger logger = Logger.getLogger(TrackRepositoryImpl.class.getName());

    @Override
    public void init() {
        logger.info("TrackRepositoryImpl: Bean inicializandose...");
    }

    @Override
    public void destroy() {
        logger.info("TrackRepositoryImpl: Bean a punto de destruirse...");
    }

    @Override
    public Track create(Track track) {
        if (track == null) {
            return null;
        }

        track.setId(nextId++);
        tracks.add(track);
        return track;
    }

    @Override
    public List<Track> findAll() {
        return new ArrayList<>(tracks);
    }

    @Override
    public Optional<Track> findById(Integer id) {
        if (id == null) {
            return Optional.empty();
        }

        return tracks.stream()
                .filter(t -> id.equals(t.getId()))
                .findFirst();
    }

    @Override
    public boolean deleteById(Integer id) {
        if (id == null) {
            return false;
        }

        Optional<Track> trackOpt = findById(id);
        if (trackOpt.isPresent()) {
            Track track = trackOpt.get();
            // Desvincular de todos los artistas asociados
            if (track.getArtists() != null) {
                List<Artist> associatedArtists = new ArrayList<>(track.getArtists());
                for (Artist artist : associatedArtists) {
                    artist.removeTrack(track);
                }
            }
            tracks.remove(track);
            return true;
        }

        return false;
    }
}
