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

public class Manager extends Thread {
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
    
   public synchronized void despertar() {
	   notify();
   }

    //TODO cargar tareas hasta windowsSize siempre?
    public void cargarThreadsIniciales(){
        for (int i=0; (i< windowSize && i<pendientes.size()) ; i++){
            Tarea tarea = new Tarea(getProximoEstado(),this);
            activas.add(tarea);
        }
    }

    public synchronized void ejecutar(ArrayList <Ficha> fichas, Tablero tablero, int nivelBackInicial)  {
        boolean primera_ficha_colocada = Boolean.TRUE;
        pendientes = creadorTareas.crearTareasIniciales(tablero, fichas, nivelBackInicial,primera_ficha_colocada);
        cargarThreadsIniciales();

        resultLog.info("SE ACTIVAN  " + activas.size() + " / PENDIENTES " + pendientes.size() +" / INDICE "+ indice);
        iniciarTareas();

        while (cantActivas()>0 || pendientes.size() > indice){
           System.out.println(Thread.currentThread().getName() + " INDICE " + indice + " / PENDIENTES TOTALES" + pendientes.size() + " / CANT ACTIVAS " + cantActivas()+ " / TOTAL HILOS " + activas.size());
          
            if(cantActivas() == windowSize){
	            try {
	              wait();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
                
            }
            else {
            	if(indice == pendientes.size()) {
            		Tarea.setDividir(true);
            		
            	}
            	  try {
   	               wait();
   	            } catch (Exception e) {
   	                e.printStackTrace();
   	            }
            }
        
        
        }
        TIENE_TAREAS = false;
    }
    
    @Override
    public void run(){
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


    public static void main(String[] args){
    	Manager m = new Manager();
    	m.run();
    }
        
}
