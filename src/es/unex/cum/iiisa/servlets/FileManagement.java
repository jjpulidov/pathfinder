package es.unex.cum.iiisa.servlets;

import es.unex.cum.iiisa.io.Entrada;
import es.unex.cum.iiisa.pathfinder.PathFinder;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "/FileManagement")
@MultipartConfig
public class FileManagement extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("action");
        System.out.println(name);
        if (name.equals("fichero")) {
            try {
                leerFichero(request);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
        } else if (name.equals("teclado")) {
            leerTeclado(request);
        }
    }

    private void leerTeclado(HttpServletRequest request) {
        int r = Integer.parseInt(request.getParameter("r"));
        int q = Integer.parseInt(request.getParameter("q"));
        String[] nodos = request.getParameter("nodos").split(",");
        String[] pares = request.getParameter("pares").split("\n");
        System.out.println("Teclado!");
        System.out.println("" + r);
        System.out.println("" + q);
        System.out.println("" + nodos.length);
    }

    private void leerFichero(HttpServletRequest request)
            throws ServletException, IOException {
        final Part filePart = request.getPart("file");
        final String fileName = getFileName(filePart);
        OutputStream out = null;
        InputStream filecontent = null;
        String path = request.getServletContext().getRealPath("") + File.separator
                + fileName;
        try {
            System.out.println(path);
            out = new FileOutputStream(new File(path));
            filecontent = filePart.getInputStream();

            int read;
            final byte[] bytes = new byte[1024];

            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
        } catch (FileNotFoundException ignore) {

        } finally {
            if (out != null) {
                out.close();
            }
            if (filecontent != null) {
                filecontent.close();
            }
        }
        int r = Integer.parseInt(request.getParameter("r"));
        int q = Integer.parseInt(request.getParameter("q"));
        String[] nodos = request.getParameter("nodos").split(",");
        List<String> nombres = new ArrayList<>(Arrays.asList(nodos));
        File input = new File(path);
        startPathfinder(input, r, q, nombres, request.getServletContext().getRealPath(""));
    }

    private void startPathfinder(File input, int r, int q, List<String> nombres, String pathSalida) {
        System.out.println("r= " + r);
        System.out.println("q= " + q);
        System.out.println("file path= " + input.getAbsolutePath());
        System.out.println("nombres= " + nombres.size());
        Entrada entrada = new Entrada(input);
        try {
            entrada.procesarFichero();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        PathFinder pathFinder = new PathFinder(entrada, r, q, nombres);
        pathFinder.execute1(pathSalida);
    }

    private String getFileName(final Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("GET BIEN");
    }
}
