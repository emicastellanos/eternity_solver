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

    public void generarHilos(int nivelCorte){

        //como vienen ordenadas no me preocupo de ver si entran o no, directamente, inserto
        int puestas=0;
        int a=0;
        List<Ficha> aBorrar = new ArrayList<>();
        while (puestas<nivelCorte){
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
        ArrayList<Ficha> restantes = new ArrayList<>(fichas);
        Collections.shuffle(restantes);

        List<Thread> listaTrabajos = new ArrayList<>();

        threadsLogger.info("TABLERO ACTUAL: \n " + tablero.imprimirse());
        for(int i=0; i<restantes.size();i++){
            if (tablero.entra(restantes.get(0))){
                TareaRunnable t =  new TareaRunnable((Tablero)tablero.clone(),new ArrayList<>(restantes),nivelCorte);
                Thread thread = new Thread(t);
                threadsLogger.info(thread.getName());
                threadsLogger.info(t.getInfo());
                listaTrabajos.add(thread);
            }
            restantes.add(restantes.get(0));
            restantes.remove(0);
        }


        for(Thread t : listaTrabajos){
            t.start();
        }

    }
}