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
    private int valorMin;
    private int valorMax;
    //    private String list;
    private int numPares;
    private String tipoMatriz;
    private int[][] matriz;

    public Entrada(File fichero) {
        this.fichero = fichero;
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
                    valorMin = Integer.parseInt(fila.trim());
                    break;
                case 5:
                    valorMax = Integer.parseInt(fila.trim());
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

        matriz = new int[n][n];
        for (String fila : valoresString) {
            int[] temp = Stream.of(fila.split("\\s+")).mapToInt(Integer::parseInt).toArray();
            matriz[temp[0] - 1][temp[1] - 1] = temp[2];
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

    public int[][] getMatriz() {
        return matriz;
    }
}
