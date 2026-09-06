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

@WebServlet(name = "ArtistSearchServlet", urlPatterns = "/artists/search")
public class ArtistSearchServlet extends HttpServlet {

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
        processSearch(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processSearch(req, resp);
    }

    private void processSearch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String contextPath = req.getContextPath();

        String name = req.getParameter("name");
        Artist foundArtist = null;
        String alertMessage = null;
        String alertType = null;

        if (name != null && !name.trim().isEmpty()) {
            foundArtist = (artistService != null) ? artistService.findByName(name.trim()) : null;
            if (foundArtist == null) {
                alertMessage = "🔍 No se encontró ningún artista con el nombre: <strong>" + name.trim() + "</strong>.";
                alertType = "danger";
            }
        }

        StringBuilder body = new StringBuilder();
        body.append("<div class=\"page-header\">\n");
        body.append("  <h1 class=\"page-title\">🔍 Búsqueda de Artista</h1>\n");
        body.append("  <a href=\"").append(contextPath).append("/artists\" class=\"btn btn-secondary\">⬅️ Volver a Artistas</a>\n");
        body.append("</div>\n");

        // Search Bar Card
        body.append("<div class=\"card\" style=\"max-width: 700px; margin: 0 auto 2rem auto;\">\n");
        body.append("  <form method=\"GET\" action=\"").append(contextPath).append("/artists/search\" style=\"display: flex; gap: 0.75rem; align-items: flex-end;\">\n");
        body.append("    <div style=\"flex: 1;\">\n");
        body.append("      <label for=\"name\">Nombre del Artista</label>\n");
        body.append("      <input type=\"text\" id=\"name\" name=\"name\" value=\"").append(name != null ? name.trim() : "").append("\" placeholder=\"Ej. Queen, Coldplay, Shakira...\" required>\n");
        body.append("    </div>\n");
        body.append("    <button type=\"submit\" class=\"btn btn-primary\" style=\"height: 42px;\">Buscar</button>\n");
        body.append("  </form>\n");
        body.append("</div>\n");

        // Search Results
        if (foundArtist != null) {
            body.append("<div class=\"card\" style=\"max-width: 900px; margin: 0 auto;\">\n");
            body.append("  <div style=\"border-bottom: 1px solid var(--card-border); padding-bottom: 1rem; margin-bottom: 1.5rem; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 1rem;\">\n");
            body.append("    <div>\n");
            body.append("      <h2 style=\"font-size: 1.5rem; color: #fff;\">").append(foundArtist.getName()).append("</h2>\n");
            body.append("      <p style=\"color: var(--text-muted); font-size: 0.9rem;\">ID: #").append(foundArtist.getId()).append(" | Nacionalidad: <strong style=\"color: #cbd5e1;\">").append(foundArtist.getNationality()).append("</strong></p>\n");
            body.append("    </div>\n");
            body.append("    <span class=\"badge badge-pink\" style=\"font-size: 0.85rem; padding: 0.4rem 0.8rem;\">").append(foundArtist.getTracks().size()).append(" Canciones</span>\n");
            body.append("  </div>\n");

            body.append("  <h3 style=\"margin-bottom: 1rem; font-size: 1.1rem; color: #a5b4fc;\">🎶 Discografía / Canciones Registradas</h3>\n");
            List<Track> tracks = foundArtist.getTracks();
            if (tracks.isEmpty()) {
                body.append("  <p style=\"color: var(--text-muted);\">Este artista no tiene pistas asociadas actualmente.</p>\n");
            } else {
                body.append("  <table>\n");
                body.append("    <thead>\n");
                body.append("      <tr>\n");
                body.append("        <th>ID</th>\n");
                body.append("        <th>Título</th>\n");
                body.append("        <th>Género</th>\n");
                body.append("        <th>Duración</th>\n");
                body.append("        <th>Álbum</th>\n");
                body.append("        <th>Co-autores</th>\n");
                body.append("      </tr>\n");
                body.append("    </thead>\n");
                body.append("    <tbody>\n");

                for (Track track : tracks) {
                    body.append("      <tr>\n");
                    body.append("        <td>#").append(track.getId()).append("</td>\n");
                    body.append("        <td><strong style=\"color: #fff;\">").append(track.getTitle()).append("</strong></td>\n");
                    body.append("        <td><span class=\"badge\">").append(track.getGenre()).append("</span></td>\n");
                    body.append("        <td>").append(track.getDuration()).append("</td>\n");
                    body.append("        <td><em>").append(track.getAlbumTitle()).append("</em></td>\n");
                    body.append("        <td>\n");
                    for (Artist author : track.getArtists()) {
                        if (!author.equals(foundArtist)) {
                            body.append("<span class=\"badge badge-green\">").append(author.getName()).append("</span> ");
                        }
                    }
                    if (track.getArtists().size() == 1) {
                        body.append("<span style=\"color: var(--text-muted); font-size: 0.8rem;\">Solo</span>");
                    }
                    body.append("        </td>\n");
                    body.append("      </tr>\n");
                }

                body.append("    </tbody>\n");
                body.append("  </table>\n");
            }

            body.append("</div>\n");
        }

        String html = HtmlTemplate.renderPage(contextPath, "Buscar Artista", "artist-search", body.toString(), alertMessage, alertType);
        out.print(html);
    }
}
