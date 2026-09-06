package com.example.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.example.model.Artist;
import com.example.model.Track;
import com.example.services.ITrackService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "TrackListServlet", urlPatterns = "/tracks")
public class TrackListServlet extends HttpServlet {

    private ITrackService trackService;

    @Override
    public void init() throws ServletException {
        super.init();
        ApplicationContext context = AppContextListener.getSpringContext(getServletContext());
        if (context != null) {
            trackService = context.getBean("trackService", ITrackService.class);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String contextPath = req.getContextPath();

        List<Track> tracks = (trackService != null) ? trackService.findAll() : List.of();

        StringBuilder body = new StringBuilder();
        body.append("<div class=\"page-header\">\n");
        body.append("  <div>\n");
        body.append("    <h1 class=\"page-title\">🎶 Canciones Registradas</h1>\n");
        body.append("    <p style=\"color: var(--text-muted);\">Total pistas en la plataforma: ").append(tracks.size()).append("</p>\n");
        body.append("  </div>\n");
        body.append("  <div style=\"display: flex; gap: 0.5rem;\">\n");
        body.append("    <a href=\"").append(contextPath).append("/tracks/create\" class=\"btn btn-primary\">➕ Nueva Canción</a>\n");
        body.append("    <a href=\"").append(contextPath).append("/tracks/delete\" class=\"btn btn-danger\">🗑️ Eliminar Canción</a>\n");
        body.append("  </div>\n");
        body.append("</div>\n");

        body.append("<div class=\"card\">\n");
        if (tracks.isEmpty()) {
            body.append("<p style=\"text-align: center; color: var(--text-muted);\">No hay canciones registradas actualmente.</p>\n");
        } else {
            body.append("<table>\n");
            body.append("  <thead>\n");
            body.append("    <tr>\n");
            body.append("      <th>ID</th>\n");
            body.append("      <th>Título</th>\n");
            body.append("      <th>Género</th>\n");
            body.append("      <th>Duración</th>\n");
            body.append("      <th>Álbum</th>\n");
            body.append("      <th>Artistas Asociados</th>\n");
            body.append("    </tr>\n");
            body.append("  </thead>\n");
            body.append("  <tbody>\n");

            for (Track track : tracks) {
                body.append("    <tr>\n");
                body.append("      <td><strong>#").append(track.getId()).append("</strong></td>\n");
                body.append("      <td><strong style=\"color: #fff;\">").append(track.getTitle()).append("</strong></td>\n");
                body.append("      <td><span class=\"badge\">").append(track.getGenre()).append("</span></td>\n");
                body.append("      <td>").append(track.getDuration()).append("</td>\n");
                body.append("      <td><em>").append(track.getAlbumTitle()).append("</em></td>\n");
                body.append("      <td>\n");

                List<Artist> artists = track.getArtists();
                if (artists == null || artists.isEmpty()) {
                    body.append("<span style=\"color: var(--text-muted); font-size: 0.85rem;\">Sin artista</span>\n");
                } else {
                    for (Artist artist : artists) {
                        body.append("<a href=\"").append(contextPath).append("/artists/search?name=").append(artist.getName()).append("\" class=\"badge badge-green\" style=\"text-decoration: none;\">").append(artist.getName()).append("</a> ");
                    }
                }

                body.append("      </td>\n");
                body.append("    </tr>\n");
            }

            body.append("  </tbody>\n");
            body.append("</table>\n");
        }
        body.append("</div>\n");

        String html = HtmlTemplate.renderPage(contextPath, "Listado de Canciones", "tracks", body.toString(), null, null);
        out.print(html);
    }
}
