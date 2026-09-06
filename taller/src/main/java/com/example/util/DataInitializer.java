package com.example.util;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.model.Artist;
import com.example.model.Track;
import com.example.repository.IArtistRepository;
import com.example.repository.ITrackRepository;

import jakarta.annotation.PostConstruct;

@Component("dataInitializer")
public class DataInitializer {

    private static final Logger logger = Logger.getLogger(DataInitializer.class.getName());

    private IArtistRepository artistRepository;
    private ITrackRepository trackRepository;

    public DataInitializer() {
    }

    @Autowired
    public DataInitializer(IArtistRepository artistRepository, ITrackRepository trackRepository) {
        this.artistRepository = artistRepository;
        this.trackRepository = trackRepository;
    }

    public void setArtistRepository(IArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public void setTrackRepository(ITrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @PostConstruct
    public void init() {
        logger.info("DataInitializer (@Component): Inicializando datos de 10 artistas y 50 canciones mediante @PostConstruct...");

        // 1. Crear 10 Artistas
        Artist a1 = artistRepository.create(new Artist(0, "Queen", "Reino Unido"));
        Artist a2 = artistRepository.create(new Artist(0, "Daft Punk", "Francia"));
        Artist a3 = artistRepository.create(new Artist(0, "Michael Jackson", "Estados Unidos"));
        Artist a4 = artistRepository.create(new Artist(0, "Shakira", "Colombia"));
        Artist a5 = artistRepository.create(new Artist(0, "Coldplay", "Reino Unido"));
        Artist a6 = artistRepository.create(new Artist(0, "The Beatles", "Reino Unido"));
        Artist a7 = artistRepository.create(new Artist(0, "Taylor Swift", "Estados Unidos"));
        Artist a8 = artistRepository.create(new Artist(0, "Rosalía", "España"));
        Artist a9 = artistRepository.create(new Artist(0, "Pink Floyd", "Reino Unido"));
        Artist a10 = artistRepository.create(new Artist(0, "Miles Davis", "Estados Unidos"));

        // 2. Crear 50 Tracks (5 por artista)
        // Queen (5)
        addTrackWithArtist("Bohemian Rhapsody", "Rock", "5:55", "A Night at the Opera", a1);
        addTrackWithArtist("Don't Stop Me Now", "Rock", "3:29", "Jazz", a1);
        addTrackWithArtist("Another One Bites the Dust", "Funk Rock", "3:36", "The Game", a1);
        addTrackWithArtist("We Will Rock You", "Rock", "2:01", "News of the World", a1);
        addTrackWithArtist("Radio Ga Ga", "Synthpop", "5:48", "The Works", a1);

        // Daft Punk (5)
        addTrackWithArtist("Get Lucky", "Disco/Funk", "4:08", "Random Access Memories", a2);
        addTrackWithArtist("One More Time", "House", "5:20", "Discovery", a2);
        addTrackWithArtist("Around the World", "House", "7:09", "Homework", a2);
        addTrackWithArtist("Harder, Better, Faster, Stronger", "Electro", "3:44", "Discovery", a2);
        addTrackWithArtist("Instant Crush", "Electropop", "5:37", "Random Access Memories", a2);

        // Michael Jackson (5)
        addTrackWithArtist("Billie Jean", "Pop/Funk", "4:54", "Thriller", a3);
        addTrackWithArtist("Thriller", "Pop/Funk", "5:57", "Thriller", a3);
        addTrackWithArtist("Beat It", "Rock/Pop", "4:18", "Thriller", a3);
        addTrackWithArtist("Smooth Criminal", "Pop", "4:17", "Bad", a3);
        addTrackWithArtist("Man in the Mirror", "Pop", "5:19", "Bad", a3);

        // Shakira (5)
        addTrackWithArtist("Hips Don't Lie", "Latin Pop", "3:38", "Oral Fixation Vol. 2", a4);
        addTrackWithArtist("Antología", "Latin Pop", "4:14", "Pies Descalzos", a4);
        addTrackWithArtist("Inevitable", "Latin Rock", "3:13", "¿Dónde Están los Ladrones?", a4);
        addTrackWithArtist("Waka Waka", "Pop", "3:22", "Sale el Sol", a4);
        addTrackWithArtist("Ojos Así", "Latin Pop", "3:57", "¿Dónde Están los Ladrones?", a4);

        // Coldplay (5)
        addTrackWithArtist("Yellow", "Alt Rock", "4:29", "Parachutes", a5);
        addTrackWithArtist("Fix You", "Alt Rock", "4:55", "X&Y", a5);
        addTrackWithArtist("Viva la Vida", "Baroque Pop", "4:02", "Viva la Vida", a5);
        addTrackWithArtist("The Scientist", "Alt Rock", "5:09", "A Rush of Blood to the Head", a5);
        addTrackWithArtist("Clocks", "Alt Rock", "5:07", "A Rush of Blood to the Head", a5);

        // The Beatles (5)
        addTrackWithArtist("Hey Jude", "Rock", "7:11", "Hey Jude", a6);
        addTrackWithArtist("Let It Be", "Rock", "4:03", "Let It Be", a6);
        addTrackWithArtist("Yesterday", "Acoustic Pop", "2:05", "Help!", a6);
        addTrackWithArtist("Come Together", "Rock", "4:19", "Abbey Road", a6);
        addTrackWithArtist("Here Comes the Sun", "Folk Rock", "3:05", "Abbey Road", a6);

        // Taylor Swift (5)
        addTrackWithArtist("Blank Space", "Synthpop", "3:51", "1989", a7);
        addTrackWithArtist("Shake It Off", "Dance Pop", "3:39", "1989", a7);
        addTrackWithArtist("All Too Well", "Country Pop", "5:29", "Red", a7);
        addTrackWithArtist("Anti-Hero", "Synthpop", "3:20", "Midnights", a7);
        addTrackWithArtist("Cardigan", "Indie Folk", "3:59", "Folklore", a7);

        // Rosalía (5)
        addTrackWithArtist("Malamente", "Flamenco Pop", "2:29", "El Mal Querer", a8);
        addTrackWithArtist("Saoko", "Reggaeton/Jazz", "2:17", "Motomami", a8);
        addTrackWithArtist("Despechá", "Mambo Pop", "2:37", "Motomami", a8);
        addTrackWithArtist("Pienso en tu mirá", "Flamenco Pop", "3:13", "El Mal Querer", a8);
        addTrackWithArtist("Candy", "R&B Pop", "3:13", "Motomami", a8);

        // Pink Floyd (5)
        addTrackWithArtist("Comfortably Numb", "Prog Rock", "6:21", "The Wall", a9);
        addTrackWithArtist("Wish You Were Here", "Prog Rock", "5:34", "Wish You Were Here", a9);
        addTrackWithArtist("Time", "Prog Rock", "6:53", "The Dark Side of the Moon", a9);
        addTrackWithArtist("Money", "Prog Rock", "6:22", "The Dark Side of the Moon", a9);
        addTrackWithArtist("Shine On You Crazy Diamond", "Prog Rock", "13:30", "Wish You Were Here", a9);

        // Miles Davis (5)
        addTrackWithArtist("So What", "Modal Jazz", "9:22", "Kind of Blue", a10);
        addTrackWithArtist("Freddie Freeloader", "Modal Jazz", "9:46", "Kind of Blue", a10);
        addTrackWithArtist("Blue in Green", "Modal Jazz", "5:37", "Kind of Blue", a10);
        addTrackWithArtist("All Blues", "Modal Jazz", "11:33", "Kind of Blue", a10);
        addTrackWithArtist("Flamenco Sketches", "Modal Jazz", "9:26", "Kind of Blue", a10);

        logger.info("DataInitializer: Inicialización completada con éxito. 10 Artistas y 50 Canciones cargadas.");
    }

    private void addTrackWithArtist(String title, String genre, String duration, String albumTitle, Artist artist) {
        Track track = new Track(0, title, genre, duration, albumTitle);
        track = trackRepository.create(track);
        if (track != null && artist != null) {
            track.addArtist(artist);
        }
    }
}
