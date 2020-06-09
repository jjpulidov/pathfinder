package es.unex.cum.iiisa.io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Entrada {
    private final File fichero;
    private String identificador;
    private String tipoValores;
    private int n;
    private int numDecimales;
    private double valorMin;
    private double valorMax;
    //    private String list;
    private int numPares;
    private String tipoMatriz;
    private double[][] matriz;

    public Entrada(File fichero) {
        this.fichero = fichero;
    }

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

    public void procesarFichero() throws IOException {
        Charset charset = Charset.defaultCharset();
        List<String> filas = Files.readAllLines(fichero.toPath(), charset);
        List<String> valoresString = new ArrayList<>();

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

        matriz = new double[n][n];
        for (String fila : valoresString) {
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

        for (int i = 0; i< matriz.length; i++){
            for (int j = 0; j < matriz[i].length; j++){
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println();
        }
    }

    public File getFichero() {
        return fichero;
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

    public int getNumPares() {
        return numPares;
    }

    public String getTipoMatriz() {
        return tipoMatriz;
    }

    public double[][] getMatriz() {
        return matriz;
    }
}
