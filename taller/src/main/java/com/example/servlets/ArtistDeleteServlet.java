package com.example.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.example.model.Artist;
import com.example.services.IArtistService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ArtistDeleteServlet", urlPatterns = "/artists/delete")
public class ArtistDeleteServlet extends HttpServlet {

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
        renderView(req, resp, null, null);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String idParam = req.getParameter("id");

        String alertMessage;
        String alertType;

        if (idParam == null || idParam.trim().isEmpty()) {
            alertMessage = "⚠️ Por favor proporcione un ID de artista válido.";
            alertType = "danger";
        } else {
            try {
                int id = Integer.parseInt(idParam.trim());
                boolean deleted = (artistService != null) && artistService.deleteById(id);
                if (deleted) {
                    alertMessage = "🗑️ Artista con ID #" + id + " eliminado correctamente del sistema.";
                    alertType = "success";
                } else {
                    alertMessage = "❌ No se encontró ningún artista con el ID #" + id + " para eliminar.";
                    alertType = "danger";
                }
            } catch (NumberFormatException e) {
                alertMessage = "⚠️ El ID debe ser un número entero válido.";
                alertType = "danger";
            } catch (Exception e) {
                alertMessage = "❌ Error al procesar la eliminación: " + e.getMessage();
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
        body.append("  <h1 class=\"page-title\">🗑️ Eliminar Artista</h1>\n");
        body.append("  <a href=\"").append(contextPath).append("/artists\" class=\"btn btn-secondary\">⬅️ Volver a Artistas</a>\n");
        body.append("</div>\n");

        body.append("<div class=\"card\" style=\"max-width: 600px; margin: 0 auto;\">\n");
        body.append("  <p style=\"color: var(--text-muted); margin-bottom: 1.5rem; font-size: 0.9rem;\">Seleccione un artista o ingrese su identificador (ID) para darlo de baja del sistema.</p>\n");
        body.append("  <form method=\"POST\" action=\"").append(contextPath).append("/artists/delete\">\n");
        
        body.append("    <div class=\"form-group\">\n");
        body.append("      <label for=\"artistSelect\">Seleccionar de la lista de Artistas</label>\n");
        body.append("      <select id=\"artistSelect\" onchange=\"document.getElementById('id').value = this.value;\">\n");
        body.append("        <option value=\"\">-- Seleccione un Artista --</option>\n");
        for (Artist artist : artists) {
            body.append("        <option value=\"").append(artist.getId()).append("\">#").append(artist.getId()).append(" - ").append(artist.getName()).append(" (").append(artist.getNationality()).append(")</option>\n");
        }
        body.append("      </select>\n");
        body.append("    </div>\n");

        body.append("    <div class=\"form-group\">\n");
        body.append("      <label for=\"id\">O Ingrese el ID del Artista manualmente *</label>\n");
        body.append("      <input type=\"number\" id=\"id\" name=\"id\" placeholder=\"Ej. 1\" min=\"1\" required>\n");
        body.append("    </div>\n");

        body.append("    <div style=\"display: flex; justify-content: flex-end; gap: 0.75rem; margin-top: 1.5rem;\">\n");
        body.append("      <a href=\"").append(contextPath).append("/artists\" class=\"btn btn-secondary\">Cancelar</a>\n");
        body.append("      <button type=\"submit\" class=\"btn btn-danger\" onclick=\"return confirm('¿Está seguro de eliminar este artista?');\">Eliminar Definitivamente</button>\n");
        body.append("    </div>\n");
        body.append("  </form>\n");
        body.append("</div>\n");

        String html = HtmlTemplate.renderPage(contextPath, "Eliminar Artista", "artist-delete", body.toString(), alertMessage, alertType);
        out.print(html);
    }
}
