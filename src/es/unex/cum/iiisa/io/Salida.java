package es.unex.cum.iiisa.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase que define la salida del algoritmo a ficheros.
 */
public class Salida {
    /**
     * Formato de fecha hora.
     */
    private final DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    /**
     * Formato de fecha.
     */
    private final DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
    /**
     * Formato de hora.
     */
    private final DateFormat df3 = new SimpleDateFormat("hh:mm:ss");
    /**
     * Matriz de pesos.
     */
    private final double[][] matriz;
    /**
     * Fecha de inicio del algoritmo.
     */
    private final Date fechaInicio;
    /**
     * Fecha de fin del algoritmo.
     */
    private final Date fechaFin;
    /**
     * Nombre del fichero de salida de estadísticas.
     */
    private final String fichEst;
    /**
     * Nombre del fichero de salida.
     */
    private final String fichSal;
    /**
     * Objeto con los atributos de la entrada.
     */
    private final Entrada entrada;

    /**
     * Constructor parametrizado.
     *
     * @param entrada     Atributos de entrada del algoritmo.
     * @param matriz      Matriz de pesos.
     * @param fechaInicio Fecha de inicio del algoritmo.
     * @param fechaFin    Fecha de finalización del algoritmo.
     * @param id          Identificador.
     * @param pathSalida  Path de los ficheros de salida.
     */
    public Salida(Entrada entrada, double[][] matriz, Date fechaInicio, Date fechaFin, String id, String pathSalida) {
        this.entrada = entrada;
        this.matriz = matriz;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fichEst = pathSalida + File.separator + "estadistica_" + id + ".txt";
        this.fichSal = pathSalida + File.separator + "salida_" + id + ".txt";
    }

    /**
     * Procesamiento de los resultados del algoritmo para generar los ficheros de salida.
     */
    public void run() {
        // Muestra la PFNET en consola
        System.out.println();
        for (double[] doubles : matriz) {
            for (double aDouble : doubles) {
                System.out.print(aDouble + " ");
            }
            System.out.println();
        }

        // Escribe el fichero de salida de estadísticas
        try {
            FileWriter myWriter = new FileWriter(fichEst);
            myWriter.write(df2.format(fechaInicio) + "\n" + entrada.getIdentificador() + "\n" + df2.format(fechaInicio) + "\n" + df3.format(fechaInicio) + "\n" + df2.format(fechaFin) + "\n" + df3.format(fechaFin));
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // Obtención de los pares
        List<String> pares = new ArrayList<>();
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                // Si el valor es distinto de 0
                if (matriz[i][j] != 0) {
                    // Si la matriz es simétrica solo se generan los pares de la matriz triangular superior
                    if (entrada.getTipoMatriz().equals("simetrica") && j > i)
                        pares.add((i + 1) + "\t" + (j + 1) + "\t" + matriz[i][j]);
                        // Si es asimétrica se sacan todos los pares != 0
                    else if (entrada.getTipoMatriz().equals("asimetrica"))
                        pares.add((i + 1) + "\t" + (j + 1) + "\t" + matriz[i][j]);
                }
            }
        }

        // Lista con las líneas del fichero de salida
        List<String> lineas = new ArrayList<>();
        lineas.add(entrada.getIdentificador() + "_sal");
        lineas.add(entrada.getTipoValores());
        lineas.add(String.valueOf(entrada.getN()));
        lineas.add(String.valueOf(entrada.getNumDecimales()));
        lineas.add(String.valueOf(entrada.getValorMin()));
        lineas.add(String.valueOf(entrada.getValorMax()));
        lineas.add("List");
        lineas.add(String.valueOf(pares.size()));
        lineas.add(entrada.getTipoMatriz());
        lineas.addAll(pares);

        // Escritura del fichero de salida línea a línea
        try {
            FileWriter myWriter = new FileWriter(fichSal);
            for (String linea : lineas)
                myWriter.write(linea + "\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public String getFichEst() {
        return fichEst;
    }

    public String getFichSal() {
        return fichSal;
    }
}
