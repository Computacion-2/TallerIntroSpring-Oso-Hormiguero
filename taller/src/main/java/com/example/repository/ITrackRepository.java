package com.example.repository;

import com.example.model.Track;
import java.util.List;

public class ITrackRepository {
    Track create(Track track);
    List<Track> findAll();
    
}
