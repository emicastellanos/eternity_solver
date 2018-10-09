package soluciones.reduccion_threads;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;
import utilidades.GeneradorFichas;
import utilidades.GeneradorFichasUnicas;

import java.lang.management.ManagementFactory;
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
    public static int cantTareasIniciales;
    public TareaFactoryMethod tareaFactory ;
    private CreadorTareas creadorTareas;
    private ArrayList<TareaAbs> hilos;
    static final Logger resultLog = Logger.getLogger("resultadoLogger");
    static final Logger MEDICIONES_LOGGER = Logger.getLogger("medicionesLogger");

    private boolean divisible = true;

    private int hilosParalelos = 1774;

    private boolean desordenar = true;

    private static int N = 8;

    private static int NIVEL_BACK_INICIAL = 6;

    private static int colores = 8;

    boolean primera_ficha_colocada = false ;

    private final String TIPO_BACK = "DFS";




    public Manager() {
        creadorTareas = new CreadorTareas();
        pendientes = Collections.synchronizedList(new ArrayList<Estado>());
        hilos = new ArrayList<>();
        SOLUCIONES = Collections.synchronizedList(new ArrayList<>());
        bloqueado = 0;
        contadorThreads = 0;
        tareaFactory = new TareaFactory(TIPO_BACK);
    }

    public int getHilosParalelos() {
        return hilosParalelos;
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
        return hilos.size();
    }

    public static int getCantTareasIniciales() {
        return cantTareasIniciales;
    }

    public static void setCantTareasIniciales(int cantTareasIniciales) {
        Manager.cantTareasIniciales = cantTareasIniciales;
    }

    public static long getNextContador(){
        contadorThreads +=1;
        return contadorThreads;
    }

    public static synchronized void addEstado(Estado proximo){
        pendientes.add(proximo);
    }

    public static synchronized void addAllEstado(List<Estado> proximos){
        pendientes.addAll(proximos);
    }

    public static synchronized Estado getProximoEstado(){
        Estado result = null;
        if(pendientes.size()>indice){
            result = pendientes.get(indice);
            indice +=1;
            System.out.println("se entrego una tarea, indice = " + indice);
        }

        return result;
    }



    /**
     * Considera activas a las tareas que estan en etapa de carga (actual!=null)
     * finalizado = false
     * */
    public int cantActivas(){
        int c = 0;
        for(TareaAbs tareaAbs : hilos){
            if (!(tareaAbs.getActual()==null && tareaAbs.isFinalizado())){
                c+=1;
            }

        }
        return c;
    }

    public void iniciarTareas(){
        for (TareaAbs tareaAbs : hilos){
            if(!tareaAbs.isAlive()){
                tareaAbs.start();
            }
        }
    }

   public synchronized void despertar() {
	   notify();
   }

    //TODO cargar tareas hasta windowsSize siempre?
    public void cargarThreadsIniciales(){
        for (int i = 0; i< hilosParalelos; i++){
            TareaAbs tareaAbs = tareaFactory.crearTarea(getProximoEstado(),this);
            tareaAbs.setDivisible(divisible);
            hilos.add(tareaAbs);
        }
    }

    public synchronized void ejecutar(ArrayList <Ficha> fichas, Tablero tablero, int nivelBackInicial)  {

        pendientes = creadorTareas.crearTareasIniciales(tablero, fichas, nivelBackInicial);
        setCantTareasIniciales(pendientes.size());
        cargarThreadsIniciales();

        resultLog.info("SE ACTIVAN  " + hilos.size() + " / PENDIENTES " + (pendientes.size() - indice));
        iniciarTareas();

        while (cantActivas()>0 || pendientes.size() > indice){
        	/*if(cantActivas() < hilosParalelos && pendientes.size() == indice) { // si hay pendientes no divido
        		//TareaAbs.setDividir(true);
	            //resultLog.info("manager setea dividir");
        	}*/

        	try {
               wait();
            }
        	catch (Exception e) {
                e.printStackTrace();
            }
        }

        TIENE_TAREAS = false;
    }

    @Override
    public void run(){
    	//TareaAbs.it=0;
        GeneradorFichas generadorFichas = new GeneradorFichasUnicas(N,colores);
        ArrayList <Ficha> fichas = generadorFichas.getFichasUnicas();
        //TODO Desordenar fichas
        Tablero tablero = new Tablero(N);
        if(primera_ficha_colocada) {
            tablero.insertarFinal(fichas.get(0));
            tablero.aumentarPosicion();
            fichas.get(0).setUsada(true);
        }

        if(desordenar)
        	Collections.shuffle(fichas);
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
        resultLog.info("TAMANO TABLERO                  = " + N);
        resultLog.info("CANTIDAD COLORES                = " + colores) ;
        resultLog.info("PRIMERA COLOCADA?               = " + primera_ficha_colocada) ;
        resultLog.info("FICHAS MEZCLADAS?               = " + desordenar) ;
        resultLog.info("CANTIDAD HILOS USADOS           = " +( m.getHilosParalelos() +1));
        resultLog.info("CANTIDAD NUCLEOS DEL PROCESADOR = "+Runtime.getRuntime().availableProcessors() );
        resultLog.info("CANTIDAD SOLUCIONES             = " + SOLUCIONES.size()) ;
        resultLog.info("NIVEL DE BACK INICIAL           = " + NIVEL_BACK_INICIAL);
        resultLog.info("CANTIDAD TAREAS INICIALES       = "+ getCantTareasIniciales());
        resultLog.info("TIEMPO                          = " + durationSecs.toString().replace('.',',') + " SEGUNDOS");
        resultLog.info("# tareas totales que se ejecutaron " + m.pendientes.size());
        // resultLog.info("# iteraciones totales "+TareaAbs.it);

    }


    public static void main(String[] args){
    	Manager m = new Manager();
    	m.run();
        MEDICIONES_LOGGER.info("\nTIEMPO " + Thread.currentThread().getName() + " "+ (new BigDecimal(ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime()).divide(new BigDecimal(1000000000))).setScale(3, RoundingMode.HALF_UP));
    }
        
}
