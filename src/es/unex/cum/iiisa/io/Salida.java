package es.unex.cum.iiisa.io;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Salida {
    private final DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    private final DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
    private final double[][] matriz;
    private final long tiempoEjecucion;
    private final Date fechaInicio;
    private final Date fechaFin;
    private final String id;
    private final String fichEst;
    private final String fichSal;

    public Salida(double[][] matriz, long tiempoEjecucion, Date fechaInicio, Date fechaFin, String id) {
        this.matriz = matriz;
        this.tiempoEjecucion = tiempoEjecucion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.id = id;
        this.fichEst = "estadistica_" + id + ".txt";
        this.fichSal = "salida_" + id + ".txt";
    }

    public void run() {
        try {
            FileWriter myWriter = new FileWriter(fichEst);
            myWriter.write(df2.format(fechaInicio) + " " + id + " " + tiempoEjecucion + " (" + df1.format(fechaInicio) + " - " + df1.format(fechaFin) + ")");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        List<String> parejas = new ArrayList<>();

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                parejas.add(i + " " + j + " " + matriz[i][j]);
            }
        }

        try {
            FileWriter myWriter = new FileWriter(fichSal);
            for (String pareja : parejas)
                myWriter.write(pareja);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
