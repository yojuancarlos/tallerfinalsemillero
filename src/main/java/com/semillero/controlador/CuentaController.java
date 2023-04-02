package com.semillero.controlador;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semillero.entidades.Cuentas;
import com.semillero.entidades.Usuarios;
import com.semillero.servicios.CuentaServicio;
import com.semillero.servicios.UsuarioServicio;
public class CuentaController extends HttpServlet{
    private CuentaServicio cuentaServicio;
    private ObjectMapper mapper;

    public CuentaController() {
        cuentaServicio = new CuentaServicio();
        mapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();
        if (path == null) {
            //List<Cuentas> cuenta = CuentaServicio.listarCuenta();
            //String json = mapper.writeValueAsString(cuenta);
            //response.setContentType("application/json");
            response.getWriter().println("tiene que escribir /listar?identificador=id de la cuenta ");
        } else {
            switch (path) {
                case "/listar":
                    String identificador = request.getParameter("identificador");
                
                    try {
                        Cuentas cuenta = cuentaServicio.buscarcCuentas(identificador);
                        String json = mapper.writeValueAsString(cuenta);
                        response.setContentType("application/json");
                        response.getWriter().println(json);
                    } catch (Exception e) {
                        response.setStatus(404);
                        Map<String, String> error = new HashMap<>();
                        error.put("error", e.getMessage());
                        String json = mapper.writeValueAsString(error);
                        response.setContentType("application/json");
                        response.getWriter().println(json);
                    }
                    break;
                case "/nose":
                    System.out.println("algo wey");
                break;
                default:
                    response.setStatus(404);
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "No se encontro el recurso");
                    String json = mapper.writeValueAsString(error);
                    response.setContentType("application/json");
                    response.getWriter().println(json);
                    break;
            }

        }

    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String content = request.getContentType();

        if (content != null && content.equals("application/json")) {
            Map<String, Object> personaMap = mapper.readValue(request.getInputStream(), HashMap.class);
            try {         
                cuentaServicio.guardarCuenta(personaMap);
                response.setStatus(HttpServletResponse.SC_CREATED);
                Map<String, String> respuesta = new HashMap<>();
                respuesta.put("mensaje", "cuenta guardada con exito");
                String json = mapper.writeValueAsString(respuesta);
                response.setContentType("application/json");
                response.getWriter().println(json);

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                Map<String, String> error = new HashMap<>();
                error.put("error", e.getMessage());
                String json = mapper.writeValueAsString(error);
                response.setContentType("application/json");
                response.getWriter().println(json);
            }

        } else {
            response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            Map<String, String> error = new HashMap<>();
            error.put("error", "El contenido debe ser JSON");
            String json = mapper.writeValueAsString(error);
            response.setContentType("application/json");
            response.getWriter().println(json);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String content = request.getContentType();
        if(content == "application/json") {
            Map <String, Object> personaMap = mapper.readValue(request.getInputStream(), HashMap.class);

            try {
                CuentaServicio.actualizarCuenta(personaMap);
                response.setStatus(HttpServletResponse.SC_OK);
                Map<String, String> respuesta = new HashMap<>();
                respuesta.put("mensaje", "Persona actualizada con exito");
                String json = mapper.writeValueAsString(respuesta);
                response.setContentType("application/json");
                response.getWriter().println(json);
                
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                Map<String, String> error = new HashMap<>();
                error.put("error", e.getMessage());
                String json = mapper.writeValueAsString(error);
                response.setContentType("application/json");
                response.getWriter().println(json);
            }
        
        } else {
            response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            Map<String, String> error = new HashMap<>();
            error.put("error", "El contenido debe ser JSON");
            String json = mapper.writeValueAsString(error);
            response.setContentType("application/json");
            response.getWriter().println(json);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String identificador = request.getParameter("identificador");
        try {
            cuentaServicio.eliminarCuenta(identificador);

            response.setStatus(HttpServletResponse.SC_OK);
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Persona eliminada con exito");
            String json = mapper.writeValueAsString(respuesta);
            response.setContentType("application/json");
            response.getWriter().println(json);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            String json = mapper.writeValueAsString(error);
            response.setContentType("application/json");
            response.getWriter().println(json);
        }
 
    }
}
