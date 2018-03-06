package soluciones.master_slave;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class TareaRunnable extends Thread {

    private Tablero tablero;
    private List<Ficha> fichas;
    public int nivelComienzo;
    public String nombreThread;
    final Logger threadsLogger = Logger.getLogger("threadLogger");

    public TareaRunnable(Tablero tablero, List<Ficha> fichas, String nombre) {
        this.tablero = tablero;
        this.fichas = new ArrayList<Ficha>();
        Ficha aux;
        for(Ficha f : fichas){
            aux = new Ficha(f.getIzq(),f.getArr(),f.getDer(),f.getAbj(),f.getId());
            this.fichas.add(aux);
        }
        this.nombreThread = nombre;
    }


    
    public void backRichi(List<Ficha> fichas, Integer nivel){

        for (Ficha f : fichas) {
            tablero.insertarFinal(f);
            if(tablero.esSolucionFinal() && tablero.esSolucion()){
                //threadsLogger.info("SOLUCION");
                //resultLog.info(tablero.imprimirse());
            }
            else{
                if (tablero.esSolucion() ) {
                    ArrayList<Ficha> aux = new ArrayList<Ficha>();
                    for (Ficha e : fichas) {
                        if (e.getId() != f.getId()) {
                            aux.add(e);
                        }
                    }
                    nivel += 1;
                    backRichi(aux, nivel);
                    nivel -= 1;
                }
            }
            tablero.eliminarUltima();
        }
    }

    

    @Override
    public void run() {
        threadsLogger.info("\n----- ARRANCA EL THREAD " + nombreThread);
        backRichi(this.fichas,this.nivelComienzo);
        ZonedDateTime zdt ;
        threadsLogger.info("\n----- TERMINO EL THREAD " + nombreThread+" "+ZonedDateTime.now());
        
    }
}