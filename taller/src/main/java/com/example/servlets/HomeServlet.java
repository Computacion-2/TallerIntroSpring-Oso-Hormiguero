package com.example.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.example.model.Artist;
import com.example.model.Track;
import com.example.services.IArtistService;
import com.example.services.ITrackService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home", "/index.html"})
public class HomeServlet extends HttpServlet {

    private IArtistService artistService;
    private ITrackService trackService;

    @Override
    public void init() throws ServletException {
        super.init();
        ApplicationContext context = AppContextListener.getSpringContext(getServletContext());
        if (context != null) {
            artistService = context.getBean("artistService", IArtistService.class);
            trackService = context.getBean("trackService", ITrackService.class);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String contextPath = req.getContextPath();

        List<Artist> artists = (artistService != null) ? artistService.findAll() : List.of();
        List<Track> tracks = (trackService != null) ? trackService.findAll() : List.of();

        StringBuilder body = new StringBuilder();
        body.append("<div style=\"text-align: center; margin-bottom: 2.5rem;\">\n");
        body.append("  <h1 style=\"font-size: 2.2rem; font-weight: 800; color: #fff; margin-bottom: 0.5rem;\">🎵 Sistema de Discografía Musical</h1>\n");
        body.append("  <p style=\"color: var(--text-muted); font-size: 1.1rem;\">Gestión de artistas, canciones y discografías con Spring Core (XML) y Jakarta Servlets.</p>\n");
        body.append("</div>\n");

        // KPI Cards
        body.append("<div style=\"display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 1.5rem; margin-bottom: 2.5rem;\">\n");
        
        body.append("  <div class=\"card\" style=\"display: flex; align-items: center; gap: 1.25rem; margin-bottom: 0;\">\n");
        body.append("    <div style=\"background: rgba(99, 102, 241, 0.2); width: 60px; height: 60px; border-radius: 12px; display: flex; align-items: center; justify-content: center; font-size: 1.8rem;\">👥</div>\n");
        body.append("    <div>\n");
        body.append("      <h2 style=\"font-size: 2rem; font-weight: 700; color: #fff;\">").append(artists.size()).append("</h2>\n");
        body.append("      <p style=\"color: var(--text-muted); font-size: 0.9rem;\">Artistas Registrados</p>\n");
        body.append("    </div>\n");
        body.append("  </div>\n");

        body.append("  <div class=\"card\" style=\"display: flex; align-items: center; gap: 1.25rem; margin-bottom: 0;\">\n");
        body.append("    <div style=\"background: rgba(236, 72, 153, 0.2); width: 60px; height: 60px; border-radius: 12px; display: flex; align-items: center; justify-content: center; font-size: 1.8rem;\">🎶</div>\n");
        body.append("    <div>\n");
        body.append("      <h2 style=\"font-size: 2rem; font-weight: 700; color: #fff;\">").append(tracks.size()).append("</h2>\n");
        body.append("      <p style=\"color: var(--text-muted); font-size: 0.9rem;\">Canciones en Plataforma</p>\n");
        body.append("    </div>\n");
        body.append("  </div>\n");

        body.append("</div>\n");

        // Quick Navigation Cards
        body.append("<div style=\"display: grid; grid-template-columns: 1fr 1fr; gap: 1.5rem;\">\n");

        body.append("  <div class=\"card\">\n");
        body.append("    <h3 style=\"font-size: 1.25rem; margin-bottom: 1rem; color: #a5b4fc;\">👥 Gestión de Artistas</h3>\n");
        body.append("    <p style=\"color: var(--text-muted); font-size: 0.9rem; margin-bottom: 1.25rem;\">Consulte el catálogo de artistas, registre nuevos talentos o busque la discografía completa por nombre.</p>\n");
        body.append("    <div style=\"display: flex; flex-direction: column; gap: 0.5rem;\">\n");
        body.append("      <a href=\"").append(contextPath).append("/artists\" class=\"btn btn-primary\">Ver Todos los Artistas</a>\n");
        body.append("      <a href=\"").append(contextPath).append("/artists/create\" class=\"btn btn-secondary\">Crear Nuevo Artista</a>\n");
        body.append("      <a href=\"").append(contextPath).append("/artists/search\" class=\"btn btn-secondary\">Buscar por Nombre</a>\n");
        body.append("      <a href=\"").append(contextPath).append("/artists/delete\" class=\"btn btn-secondary\">Eliminar Artista</a>\n");
        body.append("    </div>\n");
        body.append("  </div>\n");

        body.append("  <div class=\"card\">\n");
        body.append("    <h3 style=\"font-size: 1.25rem; margin-bottom: 1rem; color: #f472b6;\">🎶 Gestión de Canciones</h3>\n");
        body.append("    <p style=\"color: var(--text-muted); font-size: 0.9rem; margin-bottom: 1.25rem;\">Explore todas las pistas musicales registradas o añada nuevos tracks con asignación múltiple de artistas.</p>\n");
        body.append("    <div style=\"display: flex; flex-direction: column; gap: 0.5rem;\">\n");
        body.append("      <a href=\"").append(contextPath).append("/tracks\" class=\"btn btn-primary\">Ver Todas las Canciones</a>\n");
        body.append("      <a href=\"").append(contextPath).append("/tracks/create\" class=\"btn btn-secondary\">Crear Nueva Canción</a>\n");
        body.append("      <a href=\"").append(contextPath).append("/tracks/delete\" class=\"btn btn-secondary\">Eliminar Canción</a>\n");
        body.append("    </div>\n");
        body.append("  </div>\n");

        body.append("</div>\n");

        String html = HtmlTemplate.renderPage(contextPath, "Inicio", "home", body.toString(), null, null);
        out.print(html);
    }
}
