package soluciones.master_slave;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class TareaRunnable extends Thread {

    private Tablero tablero;
    private ArrayList<Ficha> fichas;
    public int nivelComienzo;
    public String nombreThread;
    public static int solucion;
    final Logger threadsLogger = Logger.getLogger("threadLogger");


    public TareaRunnable(Tablero tablero, ArrayList<Ficha> fichas, String nombre) {
        this.tablero = tablero;
        this.fichas = new ArrayList<>();
        solucion = 0;
        this.fichas = fichas;
        this.nombreThread = nombre;
    }

    public void backRichi(){
        for (Ficha f : fichas) {
            if (!f.isUsada()) {
                for (int i = 0; i < 4; i++) {
                    tablero.insertarFinal(f);
                    if (tablero.esSolucion()) {
                        if (tablero.esSolucionFinal()) {
                            solucion++;
                            //threadsLogger.info("SOLUCION");
                            //resultLog.info(tablero.imprimirse());
                            //listaSoluciones.add(tablero.clone());
                        } else {
                            f.setUsada(true);
                            tablero.aumentarPosicion();
                            backRichi();
                            tablero.retrocederPosicion();
                            f.setUsada(false);
                        }
                    }
                    tablero.eliminarUltima();
                    f.rotar();
                }
            }
        }
    }

    @Override
    public void run() {
        threadsLogger.info("\n----- ARRANCA EL THREAD " + nombreThread + " " + ZonedDateTime.now());
        backRichi();
        threadsLogger.info("\n----- TERMINO EL THREAD " + nombreThread+" "+ZonedDateTime.now());
        
    }
}