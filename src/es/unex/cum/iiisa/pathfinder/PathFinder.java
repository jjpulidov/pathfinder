package es.unex.cum.iiisa.pathfinder;

import es.unex.cum.iiisa.io.Salida;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PathFinder {
    private final int q, r, n;
    private final List<String> nombres;
    private final int[][] matrizPesos;
    private final String id;
    private int[][] pfnet;

    public PathFinder(int r, int q, int[][] matrizPesos, List<String> nombres, String id) {
        this.r = r;
        this.q = q;
        this.matrizPesos = matrizPesos;
        this.nombres = nombres;
        this.n = this.matrizPesos.length;
        this.id = id;
    }

    public void execute() {
        Date fechaInicio = new Date();
        long tiempoInicio = System.nanoTime();
        // Definición de la matriz de distancias
        int[][] matrizDistancias = new int[n][n];

        // Inicialización de la matriz de distancias a 0
        matrizDistancias = inicializarMatriz(matrizDistancias);

        // Copia de los valores de la matriz de pesos en la matriz de distancias
        matrizDistancias = copiarMatriz(matrizPesos, matrizDistancias);

        // Definición de la matriz de mayor orden
        int[][] matrizOrdenSuperior = new int[n][n];

        // Inicialización de la matriz de distancias a 0
        matrizOrdenSuperior = inicializarMatriz(matrizOrdenSuperior);

        // Copia de los valores de la matriz de pesos en la matriz de mayor orden
        matrizOrdenSuperior = copiarMatriz(matrizPesos, matrizOrdenSuperior);

        for (int i = 1; i < q; i++) {
            matrizOrdenSuperior = generarMatrizOrdenSuperior(matrizOrdenSuperior);
            matrizDistancias = generarMatrizDistanciasMinimas(matrizDistancias, matrizOrdenSuperior);
        }

        compararConMatrizPesos(matrizDistancias);

        long tiempoFin = System.nanoTime();
        Date fechaFin = new Date();

        Salida salida = new Salida(pfnet, tiempoFin - tiempoInicio, fechaInicio, fechaFin, id);
        salida.run();
    }

    public int[][] inicializarMatriz(int[][] matrix) {
        for (int[] fila : matrix) {
            Arrays.fill(fila, 0);
        }

        return matrix;
    }

    public int[][] copiarMatriz(int[][] src, int[][] dst) {
        for (int i = 0; i < src.length; i++) {
            for (int j = 0; j < src[i].length; j++) {
                int valor = src[i][j];
                if (valor != 0)
                    dst[i][j] = valor;
            }
        }

        return dst;
    }

    public int[][] generarMatrizOrdenSuperior(int[][] matrizOrdenInferior) {
        int[][] matrizOrdenSuperior = new int[n][n];

        matrizOrdenSuperior = inicializarMatriz(matrizOrdenSuperior);

        if (r == 0) {   // r == infinito
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int pesoMin = Integer.MAX_VALUE;
                    for (int k = 0; k < n; k++) {
                        int temp = Math.max(matrizOrdenInferior[k][j], matrizPesos[i][k]);
                        if (temp == 0)
                            continue;
                        if (pesoMin > temp)
                            pesoMin = temp;
                    }
                    if (pesoMin != Integer.MAX_VALUE)
                        matrizOrdenSuperior[i][j] = pesoMin;
                }
            }
        } else {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int pesoMin = Integer.MAX_VALUE;
                    for (int k = 0; k < n; k++) {
                        int temp = (int) Math.pow(Math.pow(matrizPesos[i][k], r) + Math.pow(matrizOrdenInferior[k][j], r), 1.0 / r);
                        if (temp == 0)
                            continue;
                        if (pesoMin > temp)
                            pesoMin = temp;
                    }
                    if (pesoMin != Integer.MAX_VALUE)
                        matrizOrdenSuperior[i][j] = pesoMin;
                }
            }
        }

        return matrizOrdenSuperior;
    }

    public int[][] generarMatrizDistanciasMinimas(int[][] matrizDistancias, int[][] matrizOrdenSuperior) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j)
                    continue;
                int valueD = matrizDistancias[i][j];
                int valueH = matrizOrdenSuperior[i][j];
                if (valueD > 0 && valueH > 0)
                    matrizDistancias[i][j] = Math.min(valueD, valueH);
            }
        }

        return matrizDistancias;
    }

    public void compararConMatrizPesos(int[][] matrizDistancias) {
        int[][] matrizFinal = new int[n][n];

        matrizFinal = inicializarMatriz(matrizFinal);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int valueW = matrizPesos[i][j];
                int valueD = matrizDistancias[i][j];
                if (valueW == valueD)
                    matrizFinal[i][j] = valueD;
            }
        }

        pfnet = matrizFinal;
    }
}
