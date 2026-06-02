package com.rentcar.view;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "MainForm", urlPatterns = {"/MainForm"})
public class MainForm extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            res.sendRedirect("index.jsp");
            return;
        }
        req.getRequestDispatcher("/main.jsp").forward(req, res);
    }
}