package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.repository.IArtistRepository;
import com.example.repository.ITrackRepository;
import com.example.repository.impl.ArtistRepositoryImpl;
import com.example.repository.impl.TrackRepositoryImpl;
import com.example.services.IArtistService;
import com.example.services.ITrackService;
import com.example.services.impl.ArtistServiceImpl;
import com.example.services.impl.TrackServiceImpl;
import com.example.util.DataInitializer;

/*
 * VERSION 3: CONFIGURACION MEDIANTE ARCHIVO DE CONFIGURACION JAVA
 *
 * Equivale al applicationContext.xml de la rama main, pero declarando cada
 * bean con un metodo @Bean. Los ids son los mismos que en el XML para poder
 * comparar las tres versiones lado a lado.
 */
@Configuration
public class AppConfig {

    // ==================== REPOSITORIOS ====================
    @Bean(name = "artistRepository", initMethod = "init", destroyMethod = "destroy")
    public IArtistRepository artistRepository() {
        return new ArtistRepositoryImpl();
    }

    @Bean(name = "trackRepository", initMethod = "init", destroyMethod = "destroy")
    public ITrackRepository trackRepository() {
        return new TrackRepositoryImpl();
    }

    // ==================== SERVICIOS ====================
    @Bean(name = "artistService")
    public IArtistService artistService() {
        return new ArtistServiceImpl(artistRepository());
    }

    @Bean(name = "trackService")
    public ITrackService trackService() {
        return new TrackServiceImpl(trackRepository(), artistRepository());
    }

    // ==================== INICIALIZACION DE DATOS (10 Artistas, 50 Tracks) ====================
    @Bean(name = "dataInitializer", initMethod = "init")
    public DataInitializer dataInitializer() {
        return new DataInitializer(artistRepository(), trackRepository());
    }
}
