package soluciones.reduccion_threads;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;
import utilidades.GeneradorFichas;
import utilidades.GeneradorFichasUnicas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Manager {
    private long bloqueado; // Contiene el id del thread por el cual se bloqueo
    public static List<Tablero> SOLUCIONES;
    public static boolean TIENE_TAREAS = true;
    public static List<Estado> pendientes;
    public static int indice = 0;
    private static long contadorThreads;

    private CreadorTareas creadorTareas;
    private ArrayList<Tarea> activas;
    private int windowSize ;

    static final Logger resultLog = Logger.getLogger("resultadoLogger");

    private static int N = 8;

    private static int NIVEL_BACK_INICIAL = 2;



    public Manager() {
        creadorTareas = new CreadorTareas();
        pendientes = Collections.synchronizedList(new ArrayList<Estado>());
        activas = new ArrayList<>();
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

    public static long getNextContador(){
        contadorThreads +=1;
        return contadorThreads;
    }

    public static synchronized void addEstado(Estado proximo){
        pendientes.add(proximo);
    }

    public static synchronized Estado getProximoEstado(){
        Estado result = null;
        if(pendientes.size()>indice){
            result = pendientes.get(indice);
            indice +=1;
        }

        return result;
    }

    /**
     * Considera activas a las tareas que estan en etapa de carga (actual!=null)
     * finalizado = false
     * */
    public int cantActivas(){
        int c = 0;
        for(Tarea tarea: activas){
            /*if (tarea.getActual()!=null && !tarea.isFinalizado()){
                c+=1;
            }*/
            if (!(tarea.getActual()==null && tarea.isFinalizado())){
                c+=1;
            }
        }
        return c;
    }

    public void iniciarTareas(){
        for (Tarea tarea: activas){
            if(!tarea.isAlive()){
                tarea.start();
            }
        }
    }

    //TODO cargar tareas hasta windowsSize siempre?
    public void cargarThreadsIniciales(){
        for (int i=0; (i< windowSize && i<pendientes.size()) ; i++){
            Tarea tarea = new Tarea(getProximoEstado());
            activas.add(tarea);
        }
    }

    public void cargarThreads(){
        while ((windowSize - activas.size() > (pendientes.size() - indice)) ){
            Tarea tarea = new Tarea(getProximoEstado());
            activas.add(tarea);
        }
    }

    public void ejecutar(ArrayList <Ficha> fichas, Tablero tablero, int nivelBackInicial)  {
        boolean primera_ficha_colocada = Boolean.FALSE;
        pendientes = creadorTareas.crearTareasIniciales(tablero, fichas, nivelBackInicial,primera_ficha_colocada);
        cargarThreadsIniciales();

        resultLog.info("SE ACTIVAN  " + activas.size() + " / PENDIENTES " + pendientes.size() +" / INDICE "+ indice);
        iniciarTareas();

        //TODO cada cuanto va a solicitar mas estados ? por tiempo o a partir de alguna condicion
        //TODO indice == pendientes.size podria reemplazarse por pendientes.size -4 <indice < pendientes.size() o algo asi
        //(o directamente while cantActivas()>0 ??)
        while (cantActivas()>0 || indice < pendientes.size()){
            System.out.println(Thread.currentThread().getName() + " INDICE " + indice + " / " + pendientes.size() + " / CANT " + cantActivas());
            /*if(!Tarea.isDividir() && (pendientes.size() - indice < 20 )){
                // relacionar con la cantActivadas tmb
                Tarea.setDividir(true);
            }*/
            if(indice ==pendientes.size()){
                if(!Tarea.isDividir()){
                    Tarea.setDividir(true);
                    try {
                        System.out.println(Thread.currentThread().getName() + " SE VA A DORMIR mas tiempo");
                        Thread.sleep(1000 * N); //milisegundos -> 60000 = 1 minuto
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } // Algun if (pendientes.size()-indice > )

            /** (windowSize - cantActivas() == #OCIOSOS **/
            while (( cantActivas() < windowSize ) && ((windowSize - cantActivas()) > (pendientes.size() -indice))){
                if(!Tarea.isDividir()){
                    Tarea.setDividir(true);
                    try {
                        System.out.println(Thread.currentThread().getName() + " SE VA A DORMIR porque somos pocos laburando, loco");
                        //Thread.sleep(10 * N); //milisegundos -> 60000 = 1 minuto
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if(activas.size() < windowSize && indice < pendientes.size()){
                System.out.println("CARGA THREADS Y LOS INICIAR");
                cargarThreads();
                iniciarTareas();
            }
            try {//TODO podria decir si hay 10 pendientes, dormir 10 ms si hay 40 dormir 100 ms o algo asi
                System.out.println(Thread.currentThread().getName() + " SE VA A DORMIR ");
                Thread.sleep(100 * N); //milisegundos -> 60000 = 1 minuto
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        //PARA CORTAR LOS THREADS
        //if(cantActivas()==0 && indice==pendientes.size()){
        TIENE_TAREAS = false;

    }

    public static void main(String[] args){
        int colores = N;
        GeneradorFichas generadorFichas = new GeneradorFichasUnicas(N,colores);
        ArrayList <Ficha> fichas = generadorFichas.getFichasUnicas();
        //TODO Desordenar fichas
        Collections.shuffle(fichas);
        Tablero tablero = new Tablero(N);
        resultLog.info("----- INICIO  " + ZonedDateTime.now());
        Manager m = new Manager();

        long startTime = System.nanoTime();
        m.ejecutar(fichas,tablero, NIVEL_BACK_INICIAL);
        long endTime = System.nanoTime();

        BigDecimal duration = new BigDecimal((endTime - startTime));
        BigDecimal durationSecs = (duration.divide(new BigDecimal(1000000000))).setScale(3, RoundingMode.HALF_UP);



        resultLog.info("PARALELIZADO: " + SOLUCIONES.size());
        int i = 1;
        for(Tablero t : SOLUCIONES){
            resultLog.info("SOLUCION # " + i);
            resultLog.info(t.imprimirUsadas());
            resultLog.info(t.imprimirse());
            i++;
        }

        //TODO redondear a dos

        resultLog.info("********************************");
        resultLog.info("TAMAÑO TABLERO= " + N);
        resultLog.info("COLORES = " + colores) ;
        resultLog.info("# nucleos usados " + m.getWindowSize() + " de " + Runtime.getRuntime().availableProcessors() );
        resultLog.info("TIEMPO " + durationSecs.toString().replace('.',',') + " SEGUNDOS");
        resultLog.info("CANTIDAD SOLUCIONES = " + SOLUCIONES.size()) ;
        resultLog.info("# THREADS INICIADOS " + m.getCantActivados());

    }

}
