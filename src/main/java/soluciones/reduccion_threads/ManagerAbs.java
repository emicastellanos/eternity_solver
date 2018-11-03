package soluciones.reduccion_threads;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;
import utilidades.GeneradorFichas;
import utilidades.GeneradorFichasUnicas;
import utilidades.Utils;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public abstract class ManagerAbs extends Thread {
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
    public static Map<String, Integer> interrupciones;
    public static int cantdivisiones;
    public long tiempoInicial;

    protected int hilosParalelos;// = 32;

    private boolean desordenar;// = true;

    private static int N; // = 8;

    private static int NIVEL_BACK_INICIAL; // = 4;

    private static int colores; // = 8;

    boolean primera_ficha_colocada = true ;



    public ManagerAbs(String tipoBack,int n, int c, int h, int b, int des ) {
        creadorTareas = new CreadorTareas();
        pendientes = Collections.synchronizedList(new ArrayList<Estado>());
        hilos = new ArrayList<>();
        SOLUCIONES = Collections.synchronizedList(new ArrayList<Tablero>());
        bloqueado = 0;
        contadorThreads = 0;
        tareaFactory = new TareaFactory(tipoBack);
        interrupciones = Collections.synchronizedMap(new HashMap<String, Integer>());
        cantdivisiones=0;
        N = n;
        colores = c;
        NIVEL_BACK_INICIAL = b;
        hilosParalelos = h;
        desordenar = des==1;
    }
    
    public void setTiempoInicial(long l) {
    		this.tiempoInicial = l;
    }
    
    public long getTiempoInicial() {
    		return tiempoInicial;
    }

    public int getCantHilosParalelos() {
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

    public boolean hayNodosParaExplorar(){
        return pendientes.size()<indice;
    }

    //ESTE METODO NOS DICE TRUE O FALSE DEPENDIENDEO DE SI HAY ALGUN THREAD QUE TIENE UNA SETEADA QUE TODAVIA NO INICIALIZO (ni siquiera la empezo a trabajar)
    public boolean todosInicializados(){
        List<TareaAbs> tareas = getAllThreads();
        for (TareaAbs tarea : tareas){
            if (!tarea.isInicializado()) {
                return false;
            }
        }
        return true;
    }

    public List<TareaAbs> getAllThreads(){
        return hilos;
    }


    public static int getCantTareasIniciales() {
        return cantTareasIniciales;
    }

    public static void setCantTareasIniciales(int cantTareasIniciales) {
        ManagerAbs.cantTareasIniciales = cantTareasIniciales;
    }

    public static long getNextContador(){
        contadorThreads +=1;
        return contadorThreads;
    }

    //Es al pedo que este synchronized porque solo lo va a llamar una tarea a la vez
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
            //System.out.println("se entrego una tarea al " + Thread.currentThread().getName() + ", indice = " + indice);
        }else{
            //System.out.println("se PIDIO una tarea para el " + Thread.currentThread().getName() + ", pero indice = " + indice);
        }


        return result;
    }



    /**
     * Considera activas a las tareas que estan en etapa de carga (actual!=null) o
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
            hilos.add(tareaAbs);
        }
    }

    public String getInterrupciones(){
        StringBuffer result = new StringBuffer("\n");
        Set<String> keys = interrupciones.keySet();
        Iterator<String> iterator = keys.iterator();
        Integer c = 0;
        while(iterator.hasNext()){
            String threadName = iterator.next();
            //result.append(threadName).append(" - ").append(interrupciones.get(threadName)).append("\n");
            c += interrupciones.get(threadName);
        }

        return c.toString();
    }

    public abstract void logicaDivisiones();

    //si algun hilo no finalizo significa que esta trabajando
    public boolean hayHilosTrabajando(){
        List<TareaAbs> threads = getAllThreads();
        for(TareaAbs thread : threads){
            if (!thread.isFinalizado()){
                return true;
            }
        }
        return false;
    }

    public void despertarDormidos(){
        List<TareaAbs> threads = getAllThreads();
        for(TareaAbs thread : threads){
            if (thread.getState().equals(State.WAITING)){
                thread.despertar();
            }
        }

    }

    public void esperarParaTerminar(){
        while (hayHilosTrabajando()){
            try {
                wait();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        despertarDormidos();
    }


    public synchronized void ejecutar(ArrayList <Ficha> fichas, Tablero tablero, int nivelBackInicial)  {
    		setTiempoInicial(System.nanoTime());
        pendientes = creadorTareas.crearTareasIniciales(tablero, fichas, nivelBackInicial);
        setCantTareasIniciales(pendientes.size());
        cargarThreadsIniciales();

        resultLog.info("SE ACTIVAN  " + hilos.size() + " / de " + pendientes.size());
        iniciarTareas();

        logicaDivisiones();

        TIENE_TAREAS = false;
        System.out.println("TIENE TAREAS = FALSE");

        esperarParaTerminar();

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
        //resultLog.info("----- INICIO  " + ZonedDateTime.now());


        long startTime = System.nanoTime();
        ejecutar(fichas,tablero, NIVEL_BACK_INICIAL);
        long endTime = System.nanoTime();

        BigDecimal duration = new BigDecimal((endTime - startTime));
        BigDecimal durationSecs = (duration.divide(new BigDecimal(1000000000))).setScale(3, RoundingMode.HALF_UP);


        MEDICIONES_LOGGER.info(Utils.imprimirTablerosSolucion(SOLUCIONES));

        MEDICIONES_LOGGER.info("\nTIEMPO " + Thread.currentThread().getName() + " "+ (new BigDecimal(ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime()).divide(new BigDecimal(1000000000))).setScale(3, RoundingMode.HALF_UP));

        resultLog.info("\n******************************** RESUMEN ******************************** \n");
        resultLog.info("TAMANO TABLERO                  = " + N);
        resultLog.info("CANTIDAD COLORES                = " + colores) ;
        resultLog.info("PRIMERA COLOCADA?               = " + primera_ficha_colocada) ;
        resultLog.info("FICHAS MEZCLADAS?               = " + desordenar) ;
        resultLog.info("CANTIDAD HILOS USADOS           = " +( getCantHilosParalelos() +1));
        resultLog.info("CANTIDAD NUCLEOS DEL PROCESADOR = "+Runtime.getRuntime().availableProcessors() );
        resultLog.info("CANTIDAD SOLUCIONES             = " + SOLUCIONES.size()) ;
        resultLog.info("NIVEL DE BACK INICIAL           = " + NIVEL_BACK_INICIAL);
        resultLog.info("CANTIDAD TAREAS INICIALES       = "+ getCantTareasIniciales());
        resultLog.info("TIEMPO                          = " + durationSecs.toString().replace('.',',') + " SEGUNDOS");
        resultLog.info("# tareas totales que se ejecutaron " + pendientes.size());
        resultLog.info("INTERRUPCIONES:  " + getInterrupciones());
        resultLog.info("cant divisiones:  " + cantdivisiones);




        /*List<GarbageCollectorMXBean> lista = ManagementFactory.getGarbageCollectorMXBeans();
        for(int j = 0; j < lista.size(); j++){
            System.out.println(j + " - GC count " + lista.get(j).getCollectionCount());
            System.out.println(j + " - Time elapsed " + lista.get(j).getCollectionTime() + "nanosecs");
        }

        ManagementFactory.getMemoryPoolMXBeans();*/
    }

}
