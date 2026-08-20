package com.example.repository.impl;

import java.util.ArrayList;
import java.util.List;

import com.example.model.Track;

public class TrackRepositoryImpl implements ITrackRepository {
    
    private List<Track> tracks = new ArrayList<>();
    private int nextId = 1;

    @Override
    public Track create(Track track) {
        if(track = null){
            return null;
        }

        track.setId(nextId);
        nextId++;
        tracks.add(track);

        return track;
    }

    @Override
    public List<Track> findAll() {
        return tracks;
    }
}
