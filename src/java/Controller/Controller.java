/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Davi Oliveira
 */
@WebServlet(name = "Controller", urlPatterns = {"/Controller", "/login-page", "/main-page", "/futebol-page", "/music-page", "/car-page", "/loginAuthentication", "/logout"})
public class Controller extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Controller</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Controller at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        HttpSession session = request.getSession();

        if (action.equals("/main-page")) {
            String username = (String) session.getAttribute("username");
            if (username == null) {
                response.sendRedirect("index.html");
            } else {
                response.sendRedirect("main-page.html");
            }
        } else if (action.equals("/futebol-page")) {
            incrementPageCount(request, response, "futebolPageCount");
            String username = (String) session.getAttribute("username");
            if (username == null) {
                response.sendRedirect("index.html");
            } else {
                response.sendRedirect("futebol-page.html");
            }
        } else if (action.equals("/music-page")) {
            incrementPageCount(request, response, "musicPageCount");
            String username = (String) session.getAttribute("username");
            if (username == null) {
                response.sendRedirect("index.html");
            } else {
                response.sendRedirect("music-page.html");
            }
        } else if (action.equals("/car-page")) {
            incrementPageCount(request, response, "carPageCount");
            String username = (String) session.getAttribute("username");
            if (username == null) {
                response.sendRedirect("index.html");
            } else {
                response.sendRedirect("car-page.html");
            }
        } else if (action.equals("/logout")) {
            session.invalidate();
            response.sendRedirect("index.html");
        } else {
            // Redirecionar para index.html se o endpoint não for válido
            response.sendRedirect("index.html");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(10);

        String action = request.getServletPath();
        
        if (action.equals("/loginAuthentication")) {
            //if (Authenticator.authenticate(username, password)) {
            if (username.equals("superadmin") && generateMD5(username).equals("17c4520f6cfd1ab53d8745e84681eb49")) {
                System.out.println(generateMD5("superadmin"));
                session.setAttribute("username", username);
                response.sendRedirect("main-page.html");
            } else {
                response.sendRedirect("index.html?error=1");
            }
        }
    }

    public static String generateMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

private void incrementPageCount(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        Cookie[] cookies = request.getCookies();
        Cookie pageCountCookie = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    pageCountCookie = cookie;
                    break;
                }
            }
        }

        if (pageCountCookie != null) {
            int pageCount = Integer.parseInt(pageCountCookie.getValue());
            pageCount++;
            pageCountCookie.setValue(String.valueOf(pageCount));
        } else {
            pageCountCookie = new Cookie(cookieName, "1");
        }

        pageCountCookie.setMaxAge(60 * 60 * 24 * 7); // Definir a validade do cookie para 7 dias
        response.addCookie(pageCountCookie);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
