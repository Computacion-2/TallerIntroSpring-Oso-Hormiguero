package com.example.repository;

import com.example.model.Track;
import java.util.List;
import java.util.Optional;

public interface ITrackRepository {
    void init();
    void destroy();
    Track create(Track track);
    List<Track> findAll();
    Optional<Track> findById(Integer id);
    boolean deleteById(Integer id);
}
