package soluciones.reduccion_threads;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;
import utilidades.GeneradorFichas;
import utilidades.GeneradorFichasUnicas;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.*;

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
    public static Map<String, Integer> interrupciones;
    public static int cantdivisiones;
    private boolean divisible = true;

    private int hilosParalelos = 10;

    private boolean desordenar = true;

    private static int N = 7;

    private static int NIVEL_BACK_INICIAL = 1;

    private static int colores = 7;

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
        interrupciones = new HashMap<String,Integer>();
        cantdivisiones=0;
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
        	if(cantActivas() < hilosParalelos && pendientes.size() == indice) { // si hay pendientes no divido
        		TareaAbs.setDividir(true);
        		cantdivisiones+=1;
	            //resultLog.info("manager setea dividir");
        	}

        	try {
               wait();
            }
        	catch (Exception e) {
                e.printStackTrace();
            }
        }

        TIENE_TAREAS = false;
    }

    public String getInterrupciones(){
        StringBuffer result = new StringBuffer("\n");
        Set<String> keys = interrupciones.keySet();
        Iterator<String> iterator = keys.iterator();
        while(iterator.hasNext()){
            String threadName = iterator.next();
            result.append(threadName).append(" - ").append(interrupciones.get(threadName)).append("\n");
        }
        return result.toString();
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
        resultLog.info("INTERRUPCIONES:  " + getInterrupciones());
        resultLog.info("cant divisiones:  " + cantdivisiones);


    }


    public static void main(String[] args){
    	Manager m = new Manager();
    	m.start();

        MEDICIONES_LOGGER.info("\nTIEMPO " + Thread.currentThread().getName() + " "+ (new BigDecimal(ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime()).divide(new BigDecimal(1000000000))).setScale(3, RoundingMode.HALF_UP));

        MEDICIONES_LOGGER.info("\nTIEMPO CurrentThreadCpuTime MAIN "+ (new BigDecimal(ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime()).divide(new BigDecimal(1000000000))).setScale(3, RoundingMode.HALF_UP));
        MEDICIONES_LOGGER.info("\n" + ManagementFactory.getOperatingSystemMXBean().getArch());
        MEDICIONES_LOGGER.info("\n" + ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors());
        MEDICIONES_LOGGER.info("\n" + Runtime.getRuntime().availableProcessors());
        MEDICIONES_LOGGER.info("\n" + ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage());
        MEDICIONES_LOGGER.info("\n" + ManagementFactory.getOperatingSystemMXBean().getVersion());
        MEDICIONES_LOGGER.info("\n" + ManagementFactory.getRuntimeMXBean().getSystemProperties().get("java.home"));
        MEDICIONES_LOGGER.info("\n" + ManagementFactory.getRuntimeMXBean().getSystemProperties().get("java.class.path"));
        MEDICIONES_LOGGER.info("\n" + ManagementFactory.getRuntimeMXBean().getSystemProperties().get("os.name"));
        MEDICIONES_LOGGER.info("\n" + ManagementFactory.getRuntimeMXBean().getUptime());
        ManagementFactory.getMemoryPoolMXBeans();

        List<GarbageCollectorMXBean> lista = ManagementFactory.getGarbageCollectorMXBeans();
        for(int i = 0; i < lista.size(); i++){
            System.out.println(i + " - GC count " + lista.get(i).getCollectionCount());
            System.out.println(i + " - Time elapsed " + lista.get(i).getCollectionTime());
        }

        ManagementFactory.getMemoryPoolMXBeans();
    }
        
}
