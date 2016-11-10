import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    public static ArrayList<Ficha> fichas;
    public static Boolean[] libres ;
    private static final int N = 6;
    public static Logger logger = Logger.getLogger(Main.class.getName());
    public static SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy_hh_mm_ss");



    public static void inicializarLibres(Boolean[] libres){
        int M=N*N;
        for(int i=0; i<M; i++){
            libres[i]=true;
        }
    }

    public static void inicLogger() {
        FileHandler fh;
        FileHandler fh2;

        try {

            // This block configure the logger with handler and formatter
            fh = new FileHandler("output/miLog_"+N+"_"+formatter.format(Calendar.getInstance().getTime())+".log");

            logger.addHandler(fh);
            FormateadorEmi format = new FormateadorEmi();
            fh.setFormatter(format);
            logger.log(Level.INFO, Calendar.getInstance().getTime().toString());

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void crearFichas() throws FileNotFoundException, IOException {
        String cadena;
        Ficha ficha = null ;
        fichas = new ArrayList<Ficha>();
        String[] colores;
        FileReader f = new FileReader("resources/tableroN6_3colores_ordenado.txt");
        BufferedReader b = new BufferedReader(f);
        int i = 0;
        while( (cadena = b.readLine()) !=null  ) {
        	
        	colores = cadena.split("-");
        	ficha = new Ficha(Integer.valueOf(colores[0]),Integer.valueOf(colores[1]),Integer.valueOf(colores[2]),Integer.valueOf(colores[3]),String.valueOf(i));
        	fichas.add(ficha);
        	i++;
        }
        b.close();
    }


    public static void main(String[] args) throws FileNotFoundException, IOException {

        libres = new Boolean[N*N];
        inicializarLibres(libres);
        inicLogger();
        crearFichas();

        /*Collections.shuffle(fichas);*/
        logger.log(Level.INFO,"-----Fichas-----");

        for (Ficha f: fichas){
            logger.log(Level.INFO, "f"+f.getId()+": "+f.imprimirse());
        }

        Tablero tablero = new Tablero(N);

        SolverByBacktrack solucion = new SolverByBacktrack(N,fichas,tablero,libres, logger);

        logger.log(Level.INFO,"-----BACKTRACKING-----");
        long startTime = System.nanoTime();
        solucion.findSolution();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("tiempo " + duration);

        logger.log(Level.INFO,"tiempo : " + duration);


    }
}

