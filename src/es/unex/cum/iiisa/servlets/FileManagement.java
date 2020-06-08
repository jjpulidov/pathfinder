package es.unex.cum.iiisa.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;

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
        //TODO al terminar, llamar a new Entrada con el path del fichero en el servidor
        //Entrada e = new Entrada(new File(savePath + "aqui el nombre del fichero generado"));
        //try {
        //    e.procesarFichero();
        //} catch (IOException ioException) {
        //    ioException.printStackTrace();
        //}

        // TODO nombres1 tendría que venir en el fichero
        // Llamada al algoritmo con los valores r y q que entran por teclado, al igual que los nombres
        // PathFinder pfnet = new PathFinder(2, 6, e.getMatriz(), nombres1, e.getIdentificador());
        // TODO el metodo execute deberia devolver un listado de files que se guardan en la carpeta downloads en el server
        // Este método directamente genera los dos archivos de salida en la raíz del proyecto. Son los que habría que descargar
        //pfnet.execute();
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
        System.out.println("GET BIEEEEN");
    }
}
