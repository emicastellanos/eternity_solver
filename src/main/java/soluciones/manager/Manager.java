package soluciones.manager;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;
import soluciones.master_slave.TareaRunnable;
import utilidades.GeneradorFichas;

import java.util.ArrayList;
import java.util.List;

public class Manager {
    private static boolean bloqueado = false;
    public static ArrayList<Tablero> SOLUCIONES;
    private CreadorTareas creadorTareas;
    private ArrayList<Tarea> pendientes;
    private ArrayList<Tarea> activas;
    private int windowSize ; //Cambiar nombre -> refiere a la cantidad de threads que puede haber como maximo
    static final Logger resultLog = Logger.getLogger("resultadoLogger");


    public Manager () {
        creadorTareas = new CreadorTareas();
        activas = new ArrayList<>();
        pendientes = new ArrayList<>();
        windowSize = 3; //Runtime.getRuntime().availableProcessors();
        SOLUCIONES = new ArrayList<>();
    }

    public static void setBloqueado(boolean e){
        bloqueado = e;
    }

    public static boolean isBloqueado() {
        return bloqueado;
    }

    /**
     * toma una Tarea de la lista de activas, la subdivide.
     * */
    public void solicitarMas(){
        resultLog.info("manager.solicitarMas() ");
        if(activas.size() > 0 ){
            for(Tarea tarea : activas){
                if(tarea.isAlive() && !tarea.isBloqueado()){
                    bloqueado = true;
                    //TODO preguntar si la tarea esa no esta finalizada
                    //capaz pueda resolverse con un solo boolean
                    tarea.setDividir(true);
                    tarea.setBloqueado(true);
                    String msg = "";
                    while (bloqueado){ //Busy waiting
                        //do nothing
                        msg = "manager.SolicitarMas() BLOQUEADO name:" + Thread.currentThread().getName() + " tarea name "+ tarea.getName() + " tarea nombre "+ tarea.getNombre();
                        resultLog.error(msg);

                    }
                    if(!tarea.isDividir()){
                        resultLog.error("habia pa dividir");
                        pendientes.addAll(creadorTareas.crear(tarea.getEstado()));
                    }else{
                        resultLog.error("NOOOOOO habia pa dividir");
                    }
                    /*if (!tarea.isFinalizado()) {

                        tarea.setBloqueado(false);
                        tarea.setDividir(false);
                    }*/
                    break;

                }

            }

        }
        resultLog.info("activadas " + cantActivadas() + " pendientes " + pendientes.size());
    }

    /**
     * cantidad de tareas de la lista de activas a las que se les dio start y todavia no terminaron
     * sun metodo run()
     * */
    public int cantActivadas(){
/*        System.out.println("manager.cantActivadas() ");
        resultLog.info("manager.cantActivadas() ");*/
        int c = 0;
        for (Tarea t : activas ) {
            if(t.isAlive() ) {
                //isAlive() es true si al thread le dieron start y todavia no termino su metodo run
                // (no precisamente tiene que estar corriendo)

                c++;
            }
        }
        return c;
    }

    /**
     * pasar tareas de lista de pendientes a lista de activa.
     * */
    public void activarTareas(){
        System.out.println("manager.activarTareas() ");
        resultLog.info("------ manager.activarTareas() ------");
        int activadas = cantActivadas();
        resultLog.info("hay " + String.valueOf(activadas) + " activadas");
        resultLog.info("pendientes " + pendientes.size());
        int disponibles =  pendientes.size() - activadas;

        if(disponibles < windowSize){
            activas.addAll(pendientes);
            pendientes.clear();
        }else{
            ArrayList<Tarea> removerPendientes = new ArrayList<>();
            int espacio = windowSize - activadas;
            for(int i=0 ; i < espacio; i++){
                removerPendientes.add(pendientes.get(i));
            }
            activas.addAll(removerPendientes);
            pendientes.removeAll(removerPendientes);
        }
        resultLog.info("al salir de manager.activarTareas() quedaron");
        resultLog.info("activas " + activas.size());
        resultLog.info("pendientes " + pendientes.size());

    }

    /**
     * se encarga de que todas las tareas de la lista de activas hayan recibido la señal de start()
     * */
    public void iniciarTareasActivas(){
        int log = 0;
        resultLog.info("IniciarTareasActivas activas : " + activas.size());
        for(Tarea a : activas){
            if(!a.isAlive() && !a.isFinalizado()){
                a.start();
                log++;
            }
        }
        resultLog.info("IniciarTareasActivas se iniciaron : " + String.valueOf(log));
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


        pendientes = creadorTareas.crearTareasIniciales(tablero,fichas,2);
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
            /*else {
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/
        }

    }

    public static void main(String[] args){
        GeneradorFichas generadorFichas = new GeneradorFichas(5);
        ArrayList <Ficha> fichas = generadorFichas.getFichasUnicas();
        Tablero tablero = new Tablero(5);

        TareaRunnable tareaRunnable = new TareaRunnable(tablero.clone());
        ArrayList<Tablero> comparacion = tareaRunnable.backRichi(fichas,1);

        resultLog.info("LINEAL: " + comparacion.size());

        int s = 0;
        for(Tablero t : comparacion){
            resultLog.info("SOL " + s);
            String todas ="";
            ArrayList<Ficha> todasFichas = t.getFichasUsadas();
            for(Ficha ficha : todasFichas){
                todas+= String.valueOf(ficha.getId()) + " - ";
            }
            resultLog.info(todas);
            s++;
        }

        Manager m = new Manager();
        m.ejecutar(fichas,tablero);

        resultLog.info("PARALELIZADO: " + SOLUCIONES.size());
        s = 0;
        for(Tablero t : SOLUCIONES){
            resultLog.info("SOL " + s);
            String todas ="";
            ArrayList<Ficha> todasFichas = t.getFichasUsadas();
            for(Ficha ficha : todasFichas){
                todas+= String.valueOf(ficha.getId()) + " - ";
            }
            resultLog.info(todas);
            s++;
        }


        int cont = 0;
        if(comparacion.size() == SOLUCIONES.size()){
            resultLog.info("MISMA CANTIDAD ENTRE LINEAL Y PARALELIZABLE");
            for(Tablero t : SOLUCIONES){
                if(!comparacion.contains(t)){
                    resultLog.info("NO SON IGUALES");
                }else{
                    cont++;
                }
            }
            if (cont == SOLUCIONES.size()) {
                resultLog.info("SON IGUALES");
            }
        }else {
            resultLog.info("NO HAY IGUAL CANTIDAD soluciones = " + SOLUCIONES.size() +  " Lineal " + comparacion.size()) ;
        }

    }





}
