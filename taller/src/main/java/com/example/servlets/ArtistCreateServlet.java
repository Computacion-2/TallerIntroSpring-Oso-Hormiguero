package com.example.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.context.ApplicationContext;

import com.example.model.Artist;
import com.example.services.IArtistService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ArtistCreateServlet", urlPatterns = "/artists/create")
public class ArtistCreateServlet extends HttpServlet {

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
        String name = req.getParameter("name");
        String nationality = req.getParameter("nationality");

        String alertMessage;
        String alertType;

        if (name == null || name.trim().isEmpty() || nationality == null || nationality.trim().isEmpty()) {
            alertMessage = "⚠️ Por favor, complete todos los campos obligatorios.";
            alertType = "danger";
        } else {
            try {
                Artist created = artistService.create(name.trim(), nationality.trim());
                alertMessage = "✅ Artista <strong>" + created.getName() + "</strong> (#" + created.getId() + ") creado exitosamente.";
                alertType = "success";
            } catch (Exception e) {
                alertMessage = "❌ Error al crear el artista: " + e.getMessage();
                alertType = "danger";
            }
        }

        renderView(req, resp, alertMessage, alertType);
    }

    private void renderView(HttpServletRequest req, HttpServletResponse resp, String alertMessage, String alertType) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String contextPath = req.getContextPath();

        StringBuilder body = new StringBuilder();
        body.append("<div class=\"page-header\">\n");
        body.append("  <h1 class=\"page-title\">➕ Registrar Nuevo Artista</h1>\n");
        body.append("  <a href=\"").append(contextPath).append("/artists\" class=\"btn btn-secondary\">⬅️ Volver a Artistas</a>\n");
        body.append("</div>\n");

        body.append("<div class=\"card\" style=\"max-width: 600px; margin: 0 auto;\">\n");
        body.append("  <form method=\"POST\" action=\"").append(contextPath).append("/artists/create\">\n");
        body.append("    <div class=\"form-group\">\n");
        body.append("      <label for=\"name\">Nombre del Artista *</label>\n");
        body.append("      <input type=\"text\" id=\"name\" name=\"name\" placeholder=\"Ej. Adele, Bruno Mars...\" required>\n");
        body.append("    </div>\n");
        body.append("    <div class=\"form-group\">\n");
        body.append("      <label for=\"nationality\">Nacionalidad *</label>\n");
        body.append("      <input type=\"text\" id=\"nationality\" name=\"nationality\" placeholder=\"Ej. Reino Unido, Colombia, EE.UU...\" required>\n");
        body.append("    </div>\n");
        body.append("    <div style=\"display: flex; justify-content: flex-end; gap: 0.75rem; margin-top: 1.5rem;\">\n");
        body.append("      <a href=\"").append(contextPath).append("/artists\" class=\"btn btn-secondary\">Cancelar</a>\n");
        body.append("      <button type=\"submit\" class=\"btn btn-primary\">Guardar Artista</button>\n");
        body.append("    </div>\n");
        body.append("  </form>\n");
        body.append("</div>\n");

        String html = HtmlTemplate.renderPage(contextPath, "Crear Artista", "artist-create", body.toString(), alertMessage, alertType);
        out.print(html);
    }
}
