package soluciones.manager;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;
import soluciones.master_slave.TareaRunnable;
import utilidades.GeneradorFichas;
import utilidades.ListUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Manager {
    private boolean bloqueado;
    public static List<Tablero> SOLUCIONES;
    private CreadorTareas creadorTareas;
    private ArrayList<Tarea> pendientes;
    private ArrayList<Tarea> activas;
    private int windowSize ; //TODO Cambiar nombre -> refiere a la cantidad de threads que puede haber como maximo
    static final Logger resultLog = Logger.getLogger("resultadoLogger");


    public Manager () {
        creadorTareas = new CreadorTareas(this);
        activas = new ArrayList<>();
        pendientes = new ArrayList<>();
        windowSize = 2;//Runtime.getRuntime().availableProcessors() - 2 ;
        SOLUCIONES = Collections.synchronizedList(new ArrayList<>());
        bloqueado = false;
    }

    public void setBloqueado(boolean e){
        bloqueado = e;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    /**
     * toma una Tarea de la lista de activas, la subdivide.
     * */
    public void solicitarMas(){
        resultLog.info("ACTUAL " + Thread.currentThread().getName() + " solicitarMas()");
        for(Tarea tarea : activas){ //Podriamos tener alguna clase Filtro elija las tareas mas prometedoras
            if(tarea.isAlive()){// TODO EN lugar de agarrar solo uno, podriamos agarrar un par.
                bloqueado = true;
                //TODO preguntar si la tarea esa no esta finalizada
                tarea.setDividir(true);
                String msg = "";
                while (bloqueado && !tarea.isFinalizado()){ //Busy waiting
                    //do nothing
                    msg = "BLOQUEADO Thread: " + Thread.currentThread().getName() + " TAREA NAME  "+ tarea.getName() ;
                    resultLog.error(msg);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {

                    }
                }
                if(!tarea.isDividir()){
                    resultLog.info(Thread.currentThread().getName() + " SE PUDO DIVIDIR " + tarea.getName());
                    pendientes.addAll(creadorTareas.crear(tarea.getEstado()));
                    tarea.setBloqueado(false);
                    resultLog.info(Thread.currentThread().getName() + " DESBLOQUEO A " + tarea.getName());
                    break;
                }else{
                    resultLog.error("ACTUAL " + Thread.currentThread().getName() +" NOOOOOO se pudo dividir " + tarea.getName());
                }
            }
        }
        resultLog.info("SALIENDO SOLICITAR MAS: ACTUAL " +Thread.currentThread().getName() +" activadas " + cantActivadas() + " pendientes " + pendientes.size());
    }

    /**
     * cantidad de tareas de la lista de activas a las que se les dio start y todavia no terminaron
     * su metodo run()
     * */
    public int cantActivadas(){
        //isAlive() es true si al thread le dieron start y todavia no termino su metodo run
        // (no precisamente tiene que estar corriendo)

        int c = 0;
        for (Tarea t : activas ) {
            if(t.isAlive() ) {
                c++;
            }
        }
        return c;
    }

    /**
     * Pasar Estados de lista de pendientes a las Tareas que corresponda de la lista de activas.
     * */
    public void activarTareas(){
        int activadas = cantActivadas();
        int espacioLibre = windowSize - activadas;

        resultLog.info("ACTUAL " + Thread.currentThread().getName() + "  ---------- manager.activarTareas() ---------");
        resultLog.info(" HAY " + activas.size() + " activas");
        resultLog.info(" HAY " + String.valueOf(activadas) + " activadas");
        resultLog.info(" HAY " + pendientes.size() + " pendientes");

        if(pendientes.size() < espacioLibre){
            activas.addAll(pendientes);
            pendientes.clear();
        }else{
            ArrayList<Tarea> removerPendientes = new ArrayList<>();
            for(int i=0 ; i < espacioLibre; i++){
                removerPendientes.add(pendientes.get(i));
            }
            activas.addAll(removerPendientes);
            pendientes.removeAll(removerPendientes);
        }

        resultLog.info("quedaron");
        resultLog.info("activas " + activas.size());
        resultLog.info("pendientes " + pendientes.size());
        resultLog.info("SALIENDO CON EL " + Thread.currentThread().getName() + "  ---------- manager.activarTareas() ---------");
    }

    /**
     * se encarga de que todas las tareas de la lista de activas hayan recibido la señal de start()
     * */
    public void iniciarTareasActivas(){
        int log = 0;
        resultLog.info("ENTRANDO CON " + Thread.currentThread().getName() +" a IniciarTareasActivas #activas : " + activas.size());
        ArrayList<Tarea> borrar = new ArrayList<>();
        for(Tarea a : activas){
            if(!a.isAlive()){
                if (!a.isFinalizado()){
                    a.start();
                    log++;
                }else{
                    borrar.add(a);
                }
            }
        }
        //activas.removeAll(borrar);

        resultLog.info("SALIENDO CON "+ Thread.currentThread().getName() + " IniciarTareasActivas se iniciaron : " + String.valueOf(log) + " FINALIZADAS: " + borrar.size());
    }

    public boolean tieneTareas (){
        for(Tarea a: activas){
            if(a.isAlive() || !a.isFinalizado()){
                return true;
            }

        }
        return pendientes.size()>0;
    }

    /**
     * Elimina las tareas de la lista de activas que ya terminaron
     * o no ?
     * convendria usar los threads esos para evitar crear threads
     * https://stackoverflow.com/questions/5483047/why-is-creating-a-thread-said-to-be-expensive
     * */
    public void removerFinalizadas(){

    }

    //ESTE METODO PORDIA SERVIR BAJO EL CRITERIO DE QUE LA TAREA DE MENOR NIVEL ES LA QUE MAS VALE LA
    //PENA SUBDIVIR.  PARA NO AGARRAR SIEMPRE LA TAREA activas.get(0)
    public Tarea getActivaMenorNivel(){
        return null;
    }



    public void ejecutar(ArrayList <Ficha> fichas, Tablero tablero )  {
        resultLog.info("manaager.Ejecutar () ");

        pendientes = creadorTareas.crearEstadosIniciales(tablero, fichas, 2);
        resultLog.info("CrearTareasIniciales = " + pendientes.size());
        activarTareas();
        iniciarTareasActivas();
        int entradas = 0;
        while (tieneTareas()){
            entradas ++;
            resultLog.info("manager.ejecutar (entrada= " + entradas + ")");
            if(cantActivadas()+pendientes.size() < windowSize){
                solicitarMas();
            }
            if (cantActivadas() < windowSize){
                activarTareas();
            }
            iniciarTareasActivas();
            //AHORA MANDAR A DORMIR ?
        }

    }

    public static void main(String[] args){
        GeneradorFichas generadorFichas = new GeneradorFichas(10);
        ArrayList <Ficha> fichas = generadorFichas.getFichasUnicas();
        Tablero tablero = new Tablero(10);
        TareaRunnable tareaRunnable = new TareaRunnable(tablero.clone(), ListUtils.getCopia(fichas),null);
        tareaRunnable.backRichi();

        resultLog.info("LINEAL #Soluciones: " + tareaRunnable.solucion);

        /*int s = 0;
        for(Tablero t : comparacion){
            resultLog.info("SOL " + s);
            String todas ="";
            ArrayList<Ficha> todasFichas = t.getFichasUsadas();
            for(Ficha ficha : todasFichas){
                todas+= String.valueOf(ficha.getId()) + " - ";
            }
            resultLog.info(todas);
            s++;
        }*/

        Manager m = new Manager();
        m.ejecutar(fichas,tablero);

        resultLog.info("PARALELIZADO: " + SOLUCIONES.size());
        for(Tablero t : SOLUCIONES){
            String todas ="";
            ArrayList<Ficha> todasFichas = t.getFichasUsadas();
            for(Ficha ficha : todasFichas){
                todas+= String.valueOf(ficha.getId()) + "("+ ficha.getRot() +") - ";
            }
            resultLog.info(todas);
        }


        int cont = 0;
        if(tareaRunnable.solucion == SOLUCIONES.size()){
            resultLog.info("MISMA CANTIDAD ENTRE LINEAL Y PARALELIZABLE");
            /*for(Tablero t : SOLUCIONES){
                if(!comparacion.contains(t)){
                    resultLog.info("NO SON IGUALES");
                }else{
                    cont++;
                }
            }
            if (cont == SOLUCIONES.size()) {
                resultLog.info("SON IGUALES");
            }*/
        }else {
            resultLog.info("NO HAY IGUAL CANTIDAD soluciones = " + SOLUCIONES.size() +  " Lineal " + tareaRunnable.solucion) ;
        }

    }





}
