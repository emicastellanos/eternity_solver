import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Maestro {
    private Tablero tablero;
    private List<Ficha> fichas;
    final Logger threadsLogger = Logger.getLogger("threadLogger");

    public Maestro(Tablero tablero, List<Ficha> fichas) {
        this.tablero = tablero;
        this.fichas = fichas;
    }

    //Ubica las primeras N fichas en orden en el tablero. Modifica la lista de fichas borrando las que haya usado
    public void ubicarPrimeras(int n){
        int puestas=0;
        int a=0;
        List<Ficha> aBorrar = new ArrayList<>();
        while (puestas<n){
            Ficha ficha = fichas.get(a);
            if(!tablero.estaUsada(ficha)){
                tablero.insertarFinal(ficha);
                aBorrar.add(ficha);
                puestas ++;
            }else {
                aBorrar.add(ficha);
            }
            a++;
        }
        fichas.removeAll(aBorrar);
    }

    public void generarHilos(int nivelCorte){

        ubicarPrimeras(nivelCorte);
        ArrayList<Ficha> restantes = new ArrayList<>(fichas);
        Collections.shuffle(restantes);

        List<Thread> listaTrabajos = new ArrayList<>();

        threadsLogger.info("TABLERO ACTUAL: \n " + tablero.imprimirse());
        int nroThread = 0;
        LoggerCustom loggerCustom = new LoggerCustom();
        for(int i=0; i<restantes.size();i++){
            if (!tablero.estaUsada(restantes.get(0)) && tablero.entra(restantes.get(0))){
                String nombreThread = "Thread-"+nroThread;
                Logger l = loggerCustom.getLogger(nombreThread);
                TareaRunnable t =  new TareaRunnable(tablero.clone(l),new ArrayList<>(restantes),nivelCorte,nombreThread,l);
                Thread thread = new Thread(t);
                threadsLogger.info(nombreThread);
                threadsLogger.info(t.getInfo()); // O deberia ser una funcion local a esta clase ? getInfo(restantes)
                listaTrabajos.add(thread);
                nroThread++;
            }
            restantes.add(restantes.get(0));
            restantes.remove(0);
        }


        for(Thread t : listaTrabajos){
            t.start();
        }

    }
}