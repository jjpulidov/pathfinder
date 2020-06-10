package es.unex.cum.iiisa.io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Clase que define el fichero de entrada y el procesamiento del mismo.
 */
public class Entrada {
    /**
     * Fichero de entrada obtenido desde el servlet.
     */
    private final File fichero;
    /**
     * Identificador.
     */
    private String identificador;
    /**
     * Tipo de valores de la matriz ["similaridad", "distancias"].
     */
    private String tipoValores;
    /**
     * Número de nodos.
     */
    private int n;
    /**
     * Número de decimales de los valores de la matriz.
     */
    private int numDecimales;
    /**
     * Valor mínimo de los valores de la matriz.
     */
    private double valorMin;
    /**
     * Valor máximo de los valores de la matriz.
     */
    private double valorMax;
    /**
     * Número de pares (número de valores de la matriz distintos de 0).
     */
    private int numPares;
    /**
     * Tipo de matriz ["simetrica", "asimetrica"].
     */
    private String tipoMatriz;
    /**
     * Matriz de pesos generada a raíz de los pares de valores del fichero.
     */
    private double[][] matriz;

    /**
     * Constructor por defecto. Recibe el fichero de entrada.
     *
     * @param fichero Fichero de entrada.
     */
    public Entrada(File fichero) {
        this.fichero = fichero;
    }

    /**
     * Constructor parametrizado.
     *
     * @param identificador Identificador.
     * @param tipoValores   Tipo de valores de la matriz.
     * @param n             Número de nodos.
     * @param numDecimales  Número de dígitos decimales de los valores de la matriz.
     * @param valorMin      Valor mínimo de los valores de la matriz.
     * @param valorMax      Valor máximo de los valores de la matriz.
     * @param numPares      Número de pares.
     * @param tipoMatriz    Tipo de matriz.
     * @param matriz        Matriz de pesos.
     */
    public Entrada(String identificador, String tipoValores, int n, int numDecimales, double valorMin, double valorMax, int numPares, String tipoMatriz, double[][] matriz) {
        this.fichero = null;
        this.identificador = identificador;
        this.tipoValores = tipoValores;
        this.n = n;
        this.numDecimales = numDecimales;
        this.valorMin = valorMin;
        this.valorMax = valorMax;
        this.numPares = numPares;
        this.tipoMatriz = tipoMatriz;
        this.matriz = matriz;
    }

    /**
     * Método que procesa el fichero, obteniendo todos los atributos de la clase y la matriz de pesos que necesita el algoritmo.
     *
     * @throws IOException
     */
    public void procesarFichero() throws IOException {
        // Lectura del fichero de entrada
        Charset charset = Charset.defaultCharset();
        List<String> filas = Files.readAllLines(Objects.requireNonNull(fichero).toPath(), charset);
        List<String> valoresString = new ArrayList<>();

        // Instanciación línea a línea del fichero de entrada de los atributos
        int cont = 0;
        for (String fila : filas) {
            switch (cont) {
                case 0:
                    identificador = fila.trim();
                    break;
                case 1:
                    tipoValores = fila.trim();
                    break;
                case 2:
                    n = Integer.parseInt(fila.trim());
                    break;
                case 3:
                    numDecimales = Integer.parseInt(fila.trim());
                    break;
                case 4:
                    valorMin = Double.parseDouble(fila.trim());
                    break;
                case 5:
                    valorMax = Double.parseDouble(fila.trim());
                    break;
                case 6:
                    break;
                case 7:
                    numPares = Integer.parseInt(fila.trim());
                    break;
                case 8:
                    tipoMatriz = fila.trim();
                    break;
                default:
                    valoresString.add(fila.trim());
                    break;
            }
            cont++;
        }

        // Instanciación de la matriz de pesos. Se instancian a 0 todas las posiciones menos la de los pares
        matriz = new double[n][n];
        double maximo = Double.NEGATIVE_INFINITY;
        double minimo = Double.POSITIVE_INFINITY;
        for (String fila : valoresString) {
            double[] temp = Stream.of(fila.split("\\s+")).mapToDouble(Double::parseDouble).toArray();

            // Si el valor del par se encuentra entre el mínimo y el máximo valor
            if (temp[2] >= valorMin && temp[2] <= valorMax)
                matriz[(int) temp[0] - 1][(int) temp[1] - 1] = temp[2];

            // Se obtienen también el máximo y mínimo valor de la matriz por si la matriz es de similaridad y hay que pasarla a pesos
            if (temp[2] > maximo)
                maximo = temp[2];
            if (temp[2] < minimo)
                minimo = temp[2];
        }

        // Si la matriz es simétrica se instancia la triangular inferior
        if (tipoMatriz.equals("simetrica")) {
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {
                    if (j < i)
                        matriz[i][j] = matriz[j][i];
                }
            }
        }

        // Si la matriz es de similaridad se pasa a distancias
        if (tipoValores.equals("similaridad")) {
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {
                    if (matriz[i][j] != 0)
                        matriz[i][j] = maximo - matriz[i][j] + minimo;
                }
            }
        }

        // Muestra la matriz de pesos por consola
        for (double[] doubles : matriz) {
            for (double aDouble : doubles) {
                System.out.print(aDouble + " ");
            }
            System.out.println();
        }
    }

    public String getIdentificador() {
        return identificador;
    }

    public String getTipoValores() {
        return tipoValores;
    }

    public int getN() {
        return n;
    }

    public int getNumDecimales() {
        return numDecimales;
    }

    public double getValorMin() {
        return valorMin;
    }

    public double getValorMax() {
        return valorMax;
    }

    public String getTipoMatriz() {
        return tipoMatriz;
    }

    public double[][] getMatriz() {
        return matriz;
    }
}
