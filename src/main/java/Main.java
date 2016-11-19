
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;


public class Main {
    public static ArrayList<Ficha> fichas;
    public static ArrayList<Ficha> fichastemp;
    public static Boolean[] libres;
    private static final int N = 3;
    public static SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy_hh_mm_ss");
    static final Logger fichasLog = Logger.getLogger("debugLogger");
    static final Logger resultLog = Logger.getLogger("resultadoLogger");


    public static void inicializarLibres(Boolean[] libres) {
        int M = N * N;
        for (int i = 0; i < M; i++) {
            libres[i] = true;
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {

        libres = new Boolean[N * N];
        inicializarLibres(libres);
        GeneradorFichas generadorFichas = new GeneradorFichas(N);
        fichas = generadorFichas.getFichasUnicas();


        /*Collections.shuffle(fichas);*/
        fichasLog.info("-----Fichas-----");
        for (Ficha f : fichas) {
            fichasLog.info("F" + f.getId() + ": " + f.imprimirse());
        }

        Tablero tablero = new Tablero(N);

        SolverByBacktrack solucion = new SolverByBacktrack(N, fichas, tablero, libres);

        resultLog.info("-----BACKTRACKING-----");
        long startTime = System.nanoTime();
        solucion.solucionRichi(fichas);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("tiempo " + duration);

        resultLog.info("tiempo : " + duration);


    }
}

