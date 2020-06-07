package es.unex.cum.iiisa.pathfinder;

import es.unex.cum.iiisa.io.Entrada;
import es.unex.cum.iiisa.io.Salida;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PathFinder {
    private final int q, r, n;
    private final List<String> nombres;
    private final double[][] matrizPesos;
    private final String id;
    private double[][] pfnet;
    private Entrada entrada;

    public PathFinder(Entrada entrada, int r, int q, List<String> nombres) {
        this.entrada = entrada;
        this.r = r;
        this.q = q;
        this.matrizPesos = entrada.getMatriz();
        this.nombres = nombres;
        this.n = this.matrizPesos.length;
        this.id = entrada.getIdentificador();
    }

    public void execute() {
        Date fechaInicio = new Date();

        // Definición de la matriz de distancias
        double[][] matrizDistancias = new double[n][n];

        // Inicialización de la matriz de distancias a 0
        matrizDistancias = inicializarMatriz(matrizDistancias);

        // Copia de los valores de la matriz de pesos en la matriz de distancias
        matrizDistancias = copiarMatriz(matrizPesos, matrizDistancias);

        // Definición de la matriz de mayor orden
        double[][] matrizOrdenSuperior = new double[n][n];

        // Inicialización de la matriz de distancias a 0
        matrizOrdenSuperior = inicializarMatriz(matrizOrdenSuperior);

        // Copia de los valores de la matriz de pesos en la matriz de mayor orden
        matrizOrdenSuperior = copiarMatriz(matrizPesos, matrizOrdenSuperior);

        for (int i = 1; i < q; i++) {
            matrizOrdenSuperior = generarMatrizOrdenSuperior(matrizOrdenSuperior);
            matrizDistancias = generarMatrizDistanciasMinimas(matrizDistancias, matrizOrdenSuperior);
        }

        compararConMatrizPesos(matrizDistancias);

        Date fechaFin = new Date();

        Salida salida = new Salida(entrada, pfnet, fechaInicio, fechaFin, id);
        salida.run();
    }

    public double[][] inicializarMatriz(double[][] matrix) {
        for (double[] fila : matrix) {
            Arrays.fill(fila, 0);
        }

        return matrix;
    }

    public double[][] copiarMatriz(double[][] src, double[][] dst) {
        for (int i = 0; i < src.length; i++) {
            for (int j = 0; j < src[i].length; j++) {
                double valor = src[i][j];
                if (valor != 0)
                    dst[i][j] = valor;
            }
        }

        return dst;
    }

    public double[][] generarMatrizOrdenSuperior(double[][] matrizOrdenInferior) {
        double[][] matrizOrdenSuperior = new double[n][n];

        matrizOrdenSuperior = inicializarMatriz(matrizOrdenSuperior);

        if (r == 0) {   // r == infinito
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    double pesoMin = Double.POSITIVE_INFINITY;
                    for (int k = 0; k < n; k++) {
                        double temp = Math.max(matrizOrdenInferior[k][j], matrizPesos[i][k]);
                        if (temp == 0)
                            continue;
                        if (pesoMin > temp)
                            pesoMin = temp;
                    }
                    if (pesoMin != Double.POSITIVE_INFINITY)
                        matrizOrdenSuperior[i][j] = pesoMin;
                }
            }
        } else {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    double pesoMin = Double.POSITIVE_INFINITY;
                    for (int k = 0; k < n; k++) {
                        double temp = Math.pow(Math.pow(matrizPesos[i][k], r) + Math.pow(matrizOrdenInferior[k][j], r), 1.0 / r);
                        if (temp == 0)
                            continue;
                        if (pesoMin > temp)
                            pesoMin = temp;
                    }
                    if (pesoMin != Double.POSITIVE_INFINITY)
                        matrizOrdenSuperior[i][j] = pesoMin;
                }
            }
        }

        return matrizOrdenSuperior;
    }

    public double[][] generarMatrizDistanciasMinimas(double[][] matrizDistancias, double[][] matrizOrdenSuperior) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j)
                    continue;
                double valueD = matrizDistancias[i][j];
                double valueH = matrizOrdenSuperior[i][j];
                if (valueD > 0 && valueH > 0)
                    matrizDistancias[i][j] = Math.min(valueD, valueH);
            }
        }

        return matrizDistancias;
    }

    public void compararConMatrizPesos(double[][] matrizDistancias) {
        double[][] matrizFinal = new double[n][n];

        matrizFinal = inicializarMatriz(matrizFinal);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double valueW = matrizPesos[i][j];
                double valueD = matrizDistancias[i][j];
                if (valueW == valueD)
                    matrizFinal[i][j] = valueD;
            }
        }

        pfnet = matrizFinal;
    }
}
