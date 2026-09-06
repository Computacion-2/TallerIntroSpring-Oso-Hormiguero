package com.example.services;

import java.util.List;
import com.example.model.Track;

public interface ITrackService {
    Track create(String title, String genre, String duration, String albumTitle, List<Integer> artistIds);
    Track create(Track track, List<Integer> artistIds);
    List<Track> findAll();
    Track findById(Integer id);
    boolean deleteById(int id);
}
