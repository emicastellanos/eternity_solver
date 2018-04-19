package soluciones.manager;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;
import utilidades.GeneradorFichas;
import utilidades.GeneradorFichasAleatorio;
import utilidades.GeneradorFichasUnicas;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Manager {
    private long bloqueado; // Contiene el id del thread por el cual se bloqueo
    public static List<Tablero> SOLUCIONES;
    private CreadorTareas creadorTareas;
    private ArrayList<Tarea> pendientes;
    private ArrayList<Tarea> activas;
    private int windowSize ; //TODO Cambiar nombre -> refiere a la cantidad de threads que puede haber como maximo
    static final Logger resultLog = Logger.getLogger("resultadoLogger");
    private long contadorThreads;
    private static int N = 9;
    private static int NIVEL_BACK_INICIAL = 2;



    public Manager () {
        creadorTareas = new CreadorTareas(this);
        activas = new ArrayList<>();
        pendientes = new ArrayList<>();
        windowSize = Runtime.getRuntime().availableProcessors() - 2;
        SOLUCIONES = Collections.synchronizedList(new ArrayList<>());
        bloqueado = 0;
        contadorThreads = 0;
    }

    public int getWindowSize() {
        return windowSize;
    }

    public void setBloqueado(long e){
        bloqueado = e;
    }

    public long getBloqueado() {
        return bloqueado;
    }

    public Long getContadorThreads(){
        return contadorThreads;
    }

    public int getCantActivados(){
        return activas.size();
    }

    public long getNextContador(){
        contadorThreads +=1;
        return contadorThreads;
    }

    /**
     * toma una Tarea de la lista de activas, la subdivide.
     * */
    public void solicitarMas(){
        //resultLog.info("ACTUAL " + Thread.currentThread().getName() + " solicitarMas()");
        for(Tarea tarea : activas){ //Podriamos tener alguna clase Filtro elija las tareas mas prometedoras
            if(tarea.isAlive()){// TODO EN lugar de agarrar solo uno, podriamos agarrar un par.
                bloqueado = tarea.getId();
                //TODO preguntar si la tarea esa no esta finalizada
                tarea.setDividir(true);
                String msg = "";
                while (bloqueado!=0 && !tarea.isFinalizado()){ //Busy waiting
                    //do nothing
                    msg = "BLOQUEADO Thread: " + Thread.currentThread().getName() + " TAREA NAME  "+ tarea.getName() ;
                    //resultLog.error(msg);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {

                    }
                }
                if(!tarea.isDividir()){
                    //resultLog.info(Thread.currentThread().getName() + " SE PUDO DIVIDIR " + tarea.getName());
                    pendientes.addAll(creadorTareas.crear(tarea.getEstado()));
                    tarea.setBloqueado(false);
                    //resultLog.info(Thread.currentThread().getName() + " DESBLOQUEO A " + tarea.getName());
                    break;
                }else{
                    //resultLog.error("ACTUAL " + Thread.currentThread().getName() +" NOOOOOO se pudo dividir " + tarea.getName());
                    tarea.setDividir(false); // NO SE SI DEBE IR PERO SUPONGO QUE NO JODE.
                }
            }
        }
        //resultLog.info("SALIENDO SOLICITAR MAS: ACTUAL " +Thread.currentThread().getName() +" activadas " + cantActivadas() + " pendientes " + pendientes.size());
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

        /*resultLog.info("ACTUAL " + Thread.currentThread().getName() + "  ---------- manager.activarTareas() ---------");
        resultLog.info(" HAY " + activas.size() + " activas");
        resultLog.info(" HAY " + String.valueOf(activadas) + " activadas");
        resultLog.info(" HAY " + pendientes.size() + " pendientes");
*/
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

        /*resultLog.info("quedaron");
        resultLog.info("activas " + activas.size());
        resultLog.info("pendientes " + pendientes.size());
        resultLog.info("SALIENDO CON EL " + Thread.currentThread().getName() + "  ---------- manager.activarTareas() ---------");*/
    }

    /**
     * se encarga de que todas las tareas de la lista de activas hayan recibido la señal de start()
     * */
    public void iniciarTareasActivas(){
        int log = 0;
        //resultLog.info("ENTRANDO CON " + Thread.currentThread().getName() +" a IniciarTareasActivas #activas : " + activas.size());
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

        //resultLog.info("SALIENDO CON "+ Thread.currentThread().getName() + " IniciarTareasActivas se iniciaron : " + String.valueOf(log) + " FINALIZADAS: " + borrar.size());
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



    public void ejecutar(ArrayList <Ficha> fichas, Tablero tablero, int nivelBackInicial)  {
        //resultLog.info("manaager.Ejecutar () ");
        pendientes = creadorTareas.crearTareasIniciales(tablero, fichas, nivelBackInicial);
        //resultLog.info("CrearTareasIniciales = " + pendientes.size());
        activarTareas();
        resultLog.info("SE ACTIVAN  " + activas.size() + " / PENDIENTES " + pendientes.size() );
        iniciarTareasActivas();
        int entradas = 0;
        while (tieneTareas()){
            entradas ++;
            //resultLog.info("manager.ejecutar (entrada= " + entradas + ")");
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
        int colores = N+1;
        GeneradorFichas generadorFichas = new GeneradorFichasUnicas(N,colores);
        ArrayList <Ficha> fichas = generadorFichas.getFichasUnicas();
        Tablero tablero = new Tablero(N);

        resultLog.info("----- INICIO  " + ZonedDateTime.now());
        Manager m = new Manager();
        long startTime = System.nanoTime();
        m.ejecutar(fichas,tablero, NIVEL_BACK_INICIAL);
        long endTime = System.nanoTime();

        BigDecimal duration = new BigDecimal((endTime - startTime));
        BigDecimal durationSecs = duration.divide(new BigDecimal(1000000000));

        //TODO redondear a dos

        resultLog.info("********************************");
        resultLog.info("TAMAÑO TABLERO= " + N);
        resultLog.info("COLORES = " + colores) ;
        resultLog.info("# nucleos usados " + m.getWindowSize() + " de " + Runtime.getRuntime().availableProcessors() );
        resultLog.info("TIEMPO " + durationSecs.toString().replace('.',',') + " SEGUNDOS");
        resultLog.info("CANTIDAD SOLUCIONES = " + SOLUCIONES.size()) ;
        resultLog.info("# THREADS INICIADOS " + m.getCantActivados());


    }


    /*public static void main(String[] args){
        GeneradorFichas generadorFichas = new GeneradorFichas(8,8);
        ArrayList <Ficha> fichas = generadorFichas.getFichasUnicas();
        Tablero tablero = new Tablero(8);
        TareaRunnable tareaRunnable = new TareaRunnable(tablero.clone(), Utils.getCopia(fichas),null);
        tareaRunnable.backRichi();

        resultLog.info("LINEAL #Soluciones: " + tareaRunnable.solucion);

        *//*int s = 0;
        for(Tablero t : comparacion){
            resultLog.info("SOL " + s);
            String todas ="";
            ArrayList<Ficha> todasFichas = t.getFichasUsadas();
            for(Ficha ficha : todasFichas){
                todas+= String.valueOf(ficha.getId()) + " - ";
            }
            resultLog.info(todas);
            s++;
        }*//*

        Manager m = new Manager();
        m.ejecutar(fichas,tablero);

        resultLog.info("PARALELIZADO: " + SOLUCIONES.size());
        int i = 1;
        for(Tablero t : SOLUCIONES){
            resultLog.info("SOLUCION # " + i);
            resultLog.info(t.imprimirUsadas());
            resultLog.info(t.imprimirse());
            i ++;
        }


        int cont = 0;
        if(tareaRunnable.solucion == SOLUCIONES.size()){
            resultLog.info("MISMA CANTIDAD ENTRE LINEAL Y PARALELIZABLE");
            *//*for(Tablero t : SOLUCIONES){
                if(!comparacion.contains(t)){
                    resultLog.info("NO SON IGUALES");
                }else{
                    cont++;
                }
            }
            if (cont == SOLUCIONES.size()) {
                resultLog.info("SON IGUALES");
            }*//*
        }else {
            resultLog.info("NO HAY IGUAL CANTIDAD soluciones = " + SOLUCIONES.size() +  " Lineal " + tareaRunnable.solucion) ;
        }

    }*/





}
