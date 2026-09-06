package com.example.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.example.model.Artist;
import com.example.model.Track;
import com.example.services.IArtistService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ArtistListServlet", urlPatterns = "/artists")
public class ArtistListServlet extends HttpServlet {

    private IArtistService artistService;

    @Override
    public void init() throws ServletException {
        super.init();
        ApplicationContext context = AppContextListener.getSpringContext(getServletContext());
        if (context != null) {
            artistService = context.getBean("artistService", IArtistService.class);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String contextPath = req.getContextPath();

        List<Artist> artists = (artistService != null) ? artistService.findAll() : List.of();

        StringBuilder body = new StringBuilder();
        body.append("<div class=\"page-header\">\n");
        body.append("  <div>\n");
        body.append("    <h1 class=\"page-title\">👥 Listado de Artistas</h1>\n");
        body.append("    <p style=\"color: var(--text-muted);\">Total registrados: ").append(artists.size()).append("</p>\n");
        body.append("  </div>\n");
        body.append("  <div style=\"display: flex; gap: 0.5rem;\">\n");
        body.append("    <a href=\"").append(contextPath).append("/artists/create\" class=\"btn btn-primary\">➕ Nuevo Artista</a>\n");
        body.append("    <a href=\"").append(contextPath).append("/artists/search\" class=\"btn btn-secondary\">🔍 Buscar</a>\n");
        body.append("  </div>\n");
        body.append("</div>\n");

        body.append("<div class=\"card\">\n");
        if (artists.isEmpty()) {
            body.append("<p style=\"text-align: center; color: var(--text-muted);\">No hay artistas registrados actualmente.</p>\n");
        } else {
            body.append("<table>\n");
            body.append("  <thead>\n");
            body.append("    <tr>\n");
            body.append("      <th>ID</th>\n");
            body.append("      <th>Nombre</th>\n");
            body.append("      <th>Nacionalidad</th>\n");
            body.append("      <th>Canciones Asociadas</th>\n");
            body.append("      <th>Acciones</th>\n");
            body.append("    </tr>\n");
            body.append("  </thead>\n");
            body.append("  <tbody>\n");

            for (Artist artist : artists) {
                body.append("    <tr>\n");
                body.append("      <td><strong>#").append(artist.getId()).append("</strong></td>\n");
                body.append("      <td><strong style=\"color: #fff;\">").append(artist.getName()).append("</strong></td>\n");
                body.append("      <td>").append(artist.getNationality()).append("</td>\n");
                body.append("      <td>\n");

                List<Track> tracks = artist.getTracks();
                if (tracks == null || tracks.isEmpty()) {
                    body.append("<span style=\"color: var(--text-muted); font-size: 0.85rem;\">Sin canciones</span>\n");
                } else {
                    body.append("<span class=\"badge badge-green\">").append(tracks.size()).append(" canciones</span> ");
                    for (int i = 0; i < Math.min(tracks.size(), 3); i++) {
                        body.append("<span class=\"badge\">").append(tracks.get(i).getTitle()).append("</span> ");
                    }
                    if (tracks.size() > 3) {
                        body.append("<span class=\"badge badge-pink\">+").append(tracks.size() - 3).append(" más</span>");
                    }
                }

                body.append("      </td>\n");
                body.append("      <td>\n");
                body.append("        <a href=\"").append(contextPath).append("/artists/search?name=").append(artist.getName()).append("\" class=\"btn btn-secondary\" style=\"padding: 0.35rem 0.7rem; font-size: 0.8rem;\">Ver detalles</a>\n");
                body.append("      </td>\n");
                body.append("    </tr>\n");
            }

            body.append("  </tbody>\n");
            body.append("</table>\n");
        }
        body.append("</div>\n");

        String html = HtmlTemplate.renderPage(contextPath, "Listado de Artistas", "artists", body.toString(), null, null);
        out.print(html);
    }
}
