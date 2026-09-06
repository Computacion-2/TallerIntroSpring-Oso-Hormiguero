package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.example.model.Artist;
import com.example.model.Track;
import com.example.services.IArtistService;
import com.example.services.ITrackService;

/**
 * Prueba unitaria para validar la Versión 2: Inyección de dependencias basada en Annotations
 * (@Repository, @Service, @Component, @Autowired, @PostConstruct, @PreDestroy)
 * cargada a través de applicationContext-annotations.xml / component-scan.
 */
public class AnnotationContextTest {

    private ApplicationContext context;
    private IArtistService artistService;
    private ITrackService trackService;

    @BeforeEach
    public void setUp() {
        context = new ClassPathXmlApplicationContext("applicationContext-annotations.xml");
        artistService = context.getBean("artistService", IArtistService.class);
        trackService = context.getBean("trackService", ITrackService.class);
    }

    @Test
    public void testAnnotationContextAndInitialData() {
        assertNotNull(artistService, "artistService debe haber sido inyectado vía @Service");
        assertNotNull(trackService, "trackService debe haber sido inyectado vía @Service");

        List<Artist> artists = artistService.findAll();
        List<Track> tracks = trackService.findAll();

        assertEquals(10, artists.size(), "DataInitializer (@Component + @PostConstruct) debe haber cargado 10 artistas");
        assertEquals(50, tracks.size(), "DataInitializer (@Component + @PostConstruct) debe haber cargado 50 canciones");
    }

    @Test
    public void testFindArtistByNameWithTracks() {
        Artist queen = artistService.findByName("Queen");
        assertNotNull(queen, "Queen debe existir");
        assertEquals("Reino Unido", queen.getNationality());
        assertEquals(5, queen.getTracks().size(), "Queen debe tener 5 canciones asignadas");
    }

    @Test
    public void testCreateArtist() {
        int initialSize = artistService.findAll().size();
        Artist created = artistService.create("Dua Lipa", "Reino Unido");
        assertNotNull(created);
        assertTrue(created.getId() > 0);
        assertEquals("Dua Lipa", created.getName());
        assertEquals(initialSize + 1, artistService.findAll().size());
    }

    @Test
    public void testCreateTrackWithMultipleArtists() {
        Artist a1 = artistService.findByName("Queen");
        Artist a2 = artistService.findByName("David Bowie");
        if (a2 == null) {
            a2 = artistService.create("David Bowie", "Reino Unido");
        }

        Track underPressure = trackService.create(
                "Under Pressure", "Rock", "4:08", "Hot Space",
                Arrays.asList(a1.getId(), a2.getId())
        );

        assertNotNull(underPressure);
        assertEquals(2, underPressure.getArtists().size());
        assertTrue(a1.getTracks().contains(underPressure));
        assertTrue(a2.getTracks().contains(underPressure));
    }

    @Test
    public void testDeleteTrack() {
        Track track = trackService.findAll().get(0);
        int trackId = track.getId();
        int initialTrackCount = trackService.findAll().size();

        boolean deleted = trackService.deleteById(trackId);
        assertTrue(deleted);
        assertEquals(initialTrackCount - 1, trackService.findAll().size());
    }

    @Test
    public void testDeleteArtist() {
        Artist artist = artistService.findByName("Miles Davis");
        assertNotNull(artist);
        int artistId = artist.getId();
        int initialArtistCount = artistService.findAll().size();

        boolean deleted = artistService.deleteById(artistId);
        assertTrue(deleted);
        assertEquals(initialArtistCount - 1, artistService.findAll().size());
    }
}
