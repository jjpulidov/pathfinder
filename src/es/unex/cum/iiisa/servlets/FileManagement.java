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
import java.io.File;
import java.io.IOException;

@WebServlet(name = "FileManagement")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10,    // 10 MB
        maxFileSize = 1024 * 1024 * 50,          // 50 MB
        maxRequestSize = 1024 * 1024 * 100,      // 100 MB
        location = "")
public class FileManagement extends HttpServlet {

    private static final String SAVE_DIR = "uploads";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        // gets absolute path of the web application
        String appPath = System.getProperty("user.dir");
        // constructs path of the directory to save uploaded file
        String savePath = appPath + File.separator + SAVE_DIR;
        File fileSaveDir = new File(savePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }
        System.out.println(fileSaveDir);
        try {
            //System.out.println(request.getParts());
            for (Part part : request.getParts()) {
                String fileName = extractFileName(part);
                System.out.println(savePath + File.separator + fileName);
                part.write(savePath + File.separator + fileName);
            }
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
        //TODO al terminar, llamar a new Entrada con el path del fichero en el servidor
        Entrada e = new Entrada(new File(savePath+ "aqui el nombre del fichero generado"));
        try {
            e.procesarFichero();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        // TODO nombres1 tendría que venir en el fichero
        // Llamada al algoritmo con los valores r y q que entran por teclado, al igual que los nombres
        // PathFinder pfnet = new PathFinder(2, 6, e.getMatriz(), nombres1, e.getIdentificador());
        // TODO el metodo execute deberia devolver un listado de files que se guardan en la carpeta downloads en el server
        // Este método directamente genera los dos archivos de salida en la raíz del proyecto. Son los que habría que descargar
        //pfnet.execute();

        System.out.println("Fichero guardado!");
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("GET BIEEEEN");
    }
}
