package es.unex.cum.iiisa.servlets;


import es.unex.cum.iiisa.io.Entrada;
import es.unex.cum.iiisa.io.Salida;
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
import java.util.stream.Stream;

@WebServlet(name = "/FileManagement")
@MultipartConfig
public class FileManagement extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("seleccion_entrada");
        System.out.println(name);
        if (name.equals("fichero")) {
            try {
                leerFichero(request, response);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
        } else if (name.equals("teclado")) {
            leerTeclado(request, response);
        }
    }

    private void leerTeclado(HttpServletRequest request, HttpServletResponse response) {
        String identificador = request.getParameter("identificador");
        String tipoValores = request.getParameter("valor_celdas");
        int n = Integer.parseInt(request.getParameter("nodos_red"));
        int numDecimales = Integer.parseInt(request.getParameter("num_decimales"));
        double valorMin = Double.parseDouble(request.getParameter("valor_minimo"));
        double valorMax = Double.parseDouble(request.getParameter("valor_maximo"));
        int numPares = Integer.parseInt(request.getParameter("num_pares"));
        String tipoMatriz = request.getParameter("tipo_valor_matriz");
        String[] pares = request.getParameter("pares").split("\n");
        double[][] matriz = new double[n][n];
        for (String fila : pares) {
            double[] temp = Stream.of(fila.split("\\s+")).mapToDouble(Double::parseDouble).toArray();
            matriz[(int) temp[0] - 1][(int) temp[1] - 1] = temp[2];
        }

        if (tipoMatriz.equals("simetrica")) {
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {
                    if (j < i)
                        matriz[i][j] = matriz[j][i];
                }
            }
        }

        Entrada entrada = new Entrada(
                identificador, tipoValores,
                n,
                numDecimales,
                valorMin,
                valorMax,
                numPares,
                tipoMatriz,
                matriz
        );
        startPathfinder(entrada, request, response);
    }

    private void leerFichero(HttpServletRequest request, HttpServletResponse response)
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
        File input = new File(path);
        Entrada entrada = new Entrada(input);
        try {
            entrada.procesarFichero();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        startPathfinder(entrada, request, response);
    }

    private void startPathfinder(Entrada entrada, HttpServletRequest request, HttpServletResponse response) {
        int r = Integer.parseInt(request.getParameter("r"));
        int q = Integer.parseInt(request.getParameter("q"));
        String[] nodos = request.getParameter("nodos").split(",");
        List<String> nombres = new ArrayList<>(Arrays.asList(nodos));
        String pathSalida = request.getServletContext().getRealPath("");
        PathFinder pathFinder = new PathFinder(entrada, r, q, nombres);
        Salida salida = pathFinder.execute1(pathSalida);
        request.setAttribute("fichero_salida", salida.getFichSal());
        request.setAttribute("fichero_estadisticas", salida.getFichEst());
        try {
            request.getRequestDispatcher("download.jsp").forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
