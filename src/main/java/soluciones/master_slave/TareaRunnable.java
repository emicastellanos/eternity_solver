package soluciones.master_slave;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;
import soluciones.manager.Tarea;

import java.security.PublicKey;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class TareaRunnable extends Thread {

    private Tablero tablero;
    private ArrayList<Ficha> fichas;
    public int nivelComienzo;
    public String nombreThread;
    final Logger threadsLogger = Logger.getLogger("threadLogger");

    public TareaRunnable (Tablero tablero){
        this.tablero = tablero;

    }

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


    
    public ArrayList<Tablero> backRichi(ArrayList<Ficha> fichas, Integer nivel){
        ArrayList<Tablero> listaSoluciones = new ArrayList<>();
        for (Ficha f : fichas) {
            tablero.insertarFinal(f);
            if(tablero.esSolucionFinal() && tablero.esSolucion()){
                //threadsLogger.info("SOLUCION");
                //resultLog.info(tablero.imprimirse());
                listaSoluciones.add(tablero.clone());
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
                    listaSoluciones.addAll(backRichi(aux, nivel));
                    nivel -= 1;
                }
            }
            tablero.eliminarUltima();
        }
        return listaSoluciones;
    }

    

    @Override
    public void run() {
        ZonedDateTime zdt ;
        threadsLogger.info("\n----- ARRANCA EL THREAD " + nombreThread + " " + ZonedDateTime.now());
        backRichi(this.fichas,this.nivelComienzo);
        threadsLogger.info("\n----- TERMINO EL THREAD " + nombreThread+" "+ZonedDateTime.now());
        
    }
}