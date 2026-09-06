package com.example.services.impl;

import java.util.List;

import com.example.model.Artist;
import com.example.model.Track;
import com.example.repository.IArtistRepository;
import com.example.repository.ITrackRepository;
import com.example.services.ITrackService;

public class TrackServiceImpl implements ITrackService {

    private ITrackRepository trackRepository;
    private IArtistRepository artistRepository;

    public TrackServiceImpl() {
    }

    public TrackServiceImpl(ITrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    public TrackServiceImpl(ITrackRepository trackRepository, IArtistRepository artistRepository) {
        this.trackRepository = trackRepository;
        this.artistRepository = artistRepository;
    }

    public void setTrackRepository(ITrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    public void setArtistRepository(IArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public Track create(String title, String genre, String duration, String albumTitle, List<Integer> artistIds) {
        Track track = new Track(0, title, genre, duration, albumTitle);
        return create(track, artistIds);
    }

    @Override
    public Track create(Track track, List<Integer> artistIds) {
        Track createdTrack = trackRepository.create(track);
        if (createdTrack != null && artistIds != null && artistRepository != null) {
            for (Integer artistId : artistIds) {
                if (artistId != null) {
                    artistRepository.findById(artistId).ifPresent(createdTrack::addArtist);
                }
            }
        }
        return createdTrack;
    }

    @Override
    public List<Track> findAll() {
        return trackRepository.findAll();
    }

    @Override
    public Track findById(Integer id) {
        return trackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Canción no encontrada con ID: " + id));
    }

    @Override
    public boolean deleteById(int id) {
        return trackRepository.deleteById(id);
    }
}
