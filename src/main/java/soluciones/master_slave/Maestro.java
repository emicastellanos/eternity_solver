package soluciones.master_slave;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;
import utilidades.LoggerCustom;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Maestro {
    private Tablero tablero;
    private List<Ficha> fichas;
    private int nroThread = 0;
    private LoggerCustom loggerCustom;
    final Logger threadsLogger = Logger.getLogger("threadLogger");
    private Logger logger;

    public Maestro(Tablero tablero, List<Ficha> fichas) {
        this.tablero = tablero;
        this.fichas = fichas;
        this.loggerCustom = new LoggerCustom();
        logger = loggerCustom.getLogger("Maestro");
    }

    //Ubica las primeras N fichas en orden en el tablero. Modifica la lista de fichas borrando las que haya usado
    /*public void ubicarPrimeras(int n){
        int puestas=0;
        int a=0;
        List<Ficha> aBorrar = new ArrayList<>();
        while (puestas<n){
            Ficha ficha = fichas.get(a);
            if(!tablero.estaUsada(ficha)){
                tablero.insertarFinal(ficha, logger);
                aBorrar.add(ficha);
                puestas ++;
            }else {
                aBorrar.add(ficha);
            }
            a++;
        }
        fichas.removeAll(aBorrar);
    }

    public class EstructuraAuxiliar{
        private Tablero tablero;
        private ArrayList<Ficha> fichas;

        public EstructuraAuxiliar(Tablero t, ArrayList<Ficha> f){
            this.tablero = t;
            this.fichas = f;
        }

        public Tablero getTablero(){
            return this.tablero;
        }

        public ArrayList<Ficha> getFichas(){
            return this.fichas;
        }
    }*/

    public void back(List<Ficha> fichas,Ficha f, Integer nivel,int nivelCorte){
        List<Ficha> aux ;

        if(nivel==nivelCorte){
            if(tablero.entra(f)){
                ArrayList<Ficha> clonacion = new ArrayList<>(Arrays.asList(f));
                clonacion.addAll(fichas);
                String nombreThread = "Thread-"+nroThread;
                Logger l = loggerCustom.getLogger(nombreThread);
                TareaRunnable t =  new TareaRunnable(tablero.clone(),clonacion,nivelCorte,l);
                Thread thread = new Thread(t);
                thread.setName(nombreThread);
                this.logger.info("--------------- SE ENVIA A TRABAJAR AL :" + nombreThread +"-----------" + f.imprimirse());
                thread.start();
                nroThread++;

                //estructura.add(new EstructuraAuxiliar(tablero.clone(),clonacion));
            }
        }else{
            if(!tablero.estaUsada(f)) {
                tablero.insertarFinal(f, logger);
                if (tablero.esSolucion()){
                    for (Ficha proxima : fichas) {
                        if(!tablero.estaUsada(proxima)){
                            aux = new ArrayList<>();
                            for (Ficha e : fichas) {
                                if (!e.getId().equals(proxima.getId()) && !tablero.estaUsada(e) )
                                    aux.add(e);
                            }
                            nivel += 1;
                            back(aux, proxima, nivel, nivelCorte);
                            nivel -= 1;
                        }
                    }
                }
                tablero.eliminarUltima(logger);
            }
        }
    }



    public void generarHilos(int nivelCorte){

        int nivel = 1;
        ArrayList<Ficha> aux;
        //List<EstructuraAuxiliar> estructuraAuxiliar = new ArrayList<>();
        for(Ficha f: fichas){
            aux = new ArrayList<>();
            for(Ficha e: fichas){
                if(!e.getId().equals(f.getId()))
                    aux.add(e);
            }
            back(aux,f,nivel,nivelCorte);
        }




        /*ubicarPrimeras(nivelCorte);
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
        }*/

    }
}