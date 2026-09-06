package com.example.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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

@WebServlet(name = "TrackCreateServlet", urlPatterns = "/tracks/create")
public class TrackCreateServlet extends HttpServlet {

    private ITrackService trackService;
    private IArtistService artistService;

    @Override
    public void init() throws ServletException {
        super.init();
        ApplicationContext context = AppContextListener.getSpringContext(getServletContext());
        if (context != null) {
            trackService = context.getBean("trackService", ITrackService.class);
            artistService = context.getBean("artistService", IArtistService.class);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        renderView(req, resp, null, null);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String title = req.getParameter("title");
        String genre = req.getParameter("genre");
        String duration = req.getParameter("duration");
        String albumTitle = req.getParameter("albumTitle");
        String[] artistIdsParam = req.getParameterValues("artistIds");

        String alertMessage;
        String alertType;

        if (title == null || title.trim().isEmpty() ||
            genre == null || genre.trim().isEmpty() ||
            duration == null || duration.trim().isEmpty() ||
            albumTitle == null || albumTitle.trim().isEmpty()) {
            alertMessage = "⚠️ Por favor complete todos los campos obligatorios.";
            alertType = "danger";
        } else {
            List<Integer> artistIds = new ArrayList<>();
            if (artistIdsParam != null) {
                for (String idStr : artistIdsParam) {
                    try {
                        artistIds.add(Integer.parseInt(idStr.trim()));
                    } catch (NumberFormatException ignored) {
                    }
                }
            }

            try {
                Track created = trackService.create(title.trim(), genre.trim(), duration.trim(), albumTitle.trim(), artistIds);
                alertMessage = "✅ Canción <strong>" + created.getTitle() + "</strong> (#" + created.getId() + ") creada exitosamente con " + created.getArtists().size() + " artista(s) asignado(s).";
                alertType = "success";
            } catch (Exception e) {
                alertMessage = "❌ Error al crear la canción: " + e.getMessage();
                alertType = "danger";
            }
        }

        renderView(req, resp, alertMessage, alertType);
    }

    private void renderView(HttpServletRequest req, HttpServletResponse resp, String alertMessage, String alertType) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String contextPath = req.getContextPath();

        List<Artist> artists = (artistService != null) ? artistService.findAll() : List.of();

        StringBuilder body = new StringBuilder();
        body.append("<div class=\"page-header\">\n");
        body.append("  <h1 class=\"page-title\">➕ Registrar Nueva Canción</h1>\n");
        body.append("  <a href=\"").append(contextPath).append("/tracks\" class=\"btn btn-secondary\">⬅️ Volver a Canciones</a>\n");
        body.append("</div>\n");

        body.append("<div class=\"card\" style=\"max-width: 700px; margin: 0 auto;\">\n");
        body.append("  <form method=\"POST\" action=\"").append(contextPath).append("/tracks/create\">\n");
        
        body.append("    <div class=\"form-group\">\n");
        body.append("      <label for=\"title\">Título de la Canción *</label>\n");
        body.append("      <input type=\"text\" id=\"title\" name=\"title\" placeholder=\"Ej. Bohemian Rhapsody, Billie Jean...\" required>\n");
        body.append("    </div>\n");

        body.append("    <div style=\"display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;\">\n");
        body.append("      <div class=\"form-group\">\n");
        body.append("        <label for=\"genre\">Género Musical *</label>\n");
        body.append("        <input type=\"text\" id=\"genre\" name=\"genre\" placeholder=\"Ej. Rock, Pop, Jazz...\" required>\n");
        body.append("      </div>\n");
        body.append("      <div class=\"form-group\">\n");
        body.append("        <label for=\"duration\">Duración (mm:ss) *</label>\n");
        body.append("        <input type=\"text\" id=\"duration\" name=\"duration\" placeholder=\"Ej. 3:45, 5:20...\" required>\n");
        body.append("      </div>\n");
        body.append("    </div>\n");

        body.append("    <div class=\"form-group\">\n");
        body.append("      <label for=\"albumTitle\">Nombre del Álbum *</label>\n");
        body.append("      <input type=\"text\" id=\"albumTitle\" name=\"albumTitle\" placeholder=\"Ej. Thriller, A Night at the Opera...\" required>\n");
        body.append("    </div>\n");

        body.append("    <div class=\"form-group\">\n");
        body.append("      <label>Asignar Artistas (Seleccione 1 o varios)</label>\n");
        if (artists.isEmpty()) {
            body.append("      <p style=\"color: var(--text-muted); font-size: 0.85rem;\">No hay artistas registrados para asociar. Cree un artista primero.</p>\n");
        } else {
            body.append("      <div class=\"checkbox-list\">\n");
            for (Artist artist : artists) {
                body.append("        <label class=\"checkbox-item\">\n");
                body.append("          <input type=\"checkbox\" name=\"artistIds\" value=\"").append(artist.getId()).append("\">\n");
                body.append("          <span><strong>").append(artist.getName()).append("</strong> (").append(artist.getNationality()).append(")</span>\n");
                body.append("        </label>\n");
            }
            body.append("      </div>\n");
        }
        body.append("    </div>\n");

        body.append("    <div style=\"display: flex; justify-content: flex-end; gap: 0.75rem; margin-top: 1.5rem;\">\n");
        body.append("      <a href=\"").append(contextPath).append("/tracks\" class=\"btn btn-secondary\">Cancelar</a>\n");
        body.append("      <button type=\"submit\" class=\"btn btn-primary\">Guardar Canción</button>\n");
        body.append("    </div>\n");
        body.append("  </form>\n");
        body.append("</div>\n");

        String html = HtmlTemplate.renderPage(contextPath, "Crear Canción", "track-create", body.toString(), alertMessage, alertType);
        out.print(html);
    }
}
