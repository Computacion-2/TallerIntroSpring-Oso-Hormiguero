package com.example.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.example.model.Track;
import com.example.services.ITrackService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "TrackDeleteServlet", urlPatterns = "/tracks/delete")
public class TrackDeleteServlet extends HttpServlet {

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
        renderView(req, resp, null, null);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String idParam = req.getParameter("id");

        String alertMessage;
        String alertType;

        if (idParam == null || idParam.trim().isEmpty()) {
            alertMessage = "⚠️ Por favor proporcione un ID de canción válido.";
            alertType = "danger";
        } else {
            try {
                int id = Integer.parseInt(idParam.trim());
                boolean deleted = (trackService != null) && trackService.deleteById(id);
                if (deleted) {
                    alertMessage = "🗑️ Canción con ID #" + id + " eliminada correctamente del sistema.";
                    alertType = "success";
                } else {
                    alertMessage = "❌ No se encontró ninguna canción con el ID #" + id + " para eliminar.";
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

        List<Track> tracks = (trackService != null) ? trackService.findAll() : List.of();

        StringBuilder body = new StringBuilder();
        body.append("<div class=\"page-header\">\n");
        body.append("  <h1 class=\"page-title\">🗑️ Eliminar Canción</h1>\n");
        body.append("  <a href=\"").append(contextPath).append("/tracks\" class=\"btn btn-secondary\">⬅️ Volver a Canciones</a>\n");
        body.append("</div>\n");

        body.append("<div class=\"card\" style=\"max-width: 600px; margin: 0 auto;\">\n");
        body.append("  <p style=\"color: var(--text-muted); margin-bottom: 1.5rem; font-size: 0.9rem;\">Seleccione una pista musical o ingrese su identificador (ID) para darla de baja.</p>\n");
        body.append("  <form method=\"POST\" action=\"").append(contextPath).append("/tracks/delete\">\n");
        
        body.append("    <div class=\"form-group\">\n");
        body.append("      <label for=\"trackSelect\">Seleccionar de la lista de Canciones</label>\n");
        body.append("      <select id=\"trackSelect\" onchange=\"document.getElementById('id').value = this.value;\">\n");
        body.append("        <option value=\"\">-- Seleccione una Canción --</option>\n");
        for (Track track : tracks) {
            body.append("        <option value=\"").append(track.getId()).append("\">#").append(track.getId()).append(" - ").append(track.getTitle()).append(" (").append(track.getAlbumTitle()).append(")</option>\n");
        }
        body.append("      </select>\n");
        body.append("    </div>\n");

        body.append("    <div class=\"form-group\">\n");
        body.append("      <label for=\"id\">O Ingrese el ID de la Canción manualmente *</label>\n");
        body.append("      <input type=\"number\" id=\"id\" name=\"id\" placeholder=\"Ej. 10\" min=\"1\" required>\n");
        body.append("    </div>\n");

        body.append("    <div style=\"display: flex; justify-content: flex-end; gap: 0.75rem; margin-top: 1.5rem;\">\n");
        body.append("      <a href=\"").append(contextPath).append("/tracks\" class=\"btn btn-secondary\">Cancelar</a>\n");
        body.append("      <button type=\"submit\" class=\"btn btn-danger\" onclick=\"return confirm('¿Está seguro de eliminar esta canción?');\">Eliminar Definitivamente</button>\n");
        body.append("    </div>\n");
        body.append("  </form>\n");
        body.append("</div>\n");

        String html = HtmlTemplate.renderPage(contextPath, "Eliminar Canción", "track-delete", body.toString(), alertMessage, alertType);
        out.print(html);
    }
}
