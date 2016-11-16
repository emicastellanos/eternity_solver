import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static ArrayList<Ficha> fichas;
	public static ArrayList<Ficha> fichastemp;
    public static Boolean[] libres ;
    private static final int N = 7;
    public static Logger logger = Logger.getLogger(Main.class.getName());
    public static SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy_hh_mm_ss");
	public static int colorMayor;


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
    
    public static void crearFichasAutoMaximoColores(){
    	fichas = new ArrayList<Ficha>();
    	Ficha ficha = null ;
    	Ficha[][] tablero = new Ficha[N][N];
    	int id = 1;
    	int color = 1;
    	for(int i=0; i<N; i++){
         for(int j=0; j<N; j++){
    		if(i==0 && j==0){	
    			ficha = new Ficha(0,0,color,color+N-1,String.valueOf(id));
    			tablero[i][j] = ficha;
    		}
    		else{
    			if(i==0 && j!= N-1){
    				ficha = new Ficha(color-1,0,color,color+N-1,String.valueOf(id));
        			tablero[i][j] = ficha;
    			}
    			else{
    				if(i==0 && j==N-1){
    					ficha = new Ficha(color-1,0,0,color+N-1,String.valueOf(id));
            			tablero[i][j] = ficha;
    				}
    				else{
    					if(j==0 && i!=N-1){
    						color=tablero[i-1][N-1].getAbj()+1;
    						ficha = new Ficha(0,tablero[i-1][j].getAbj(),color,color+N-1,String.valueOf(id));
                			tablero[i][j] = ficha;
    					}
    					else{
    						if(j!=N-1 && i!=N-1){
    							ficha = new Ficha(color-1,tablero[i-1][j].getAbj(),color,color+N-1,String.valueOf(id));
                    			tablero[i][j] = ficha;
    						}
    						else{
    							if(j==N-1 && i!=N-1){
    								ficha = new Ficha(color-1,tablero[i-1][j].getAbj(),0,color+N-1,String.valueOf(id));
                        			tablero[i][j] = ficha;
    							}
    							else{
    		    					if(j==0 && i==N-1){
    		    						color=tablero[i-1][N-1].getAbj()+1;
    		    						ficha = new Ficha(0,tablero[i-1][j].getAbj(),color,0,String.valueOf(id));
    		                			tablero[i][j] = ficha;
    		    					}
    		    					else{
    		    						if(j!=N-1 && i==N-1){
    		    							ficha = new Ficha(color-1,tablero[i-1][j].getAbj(),color,0,String.valueOf(id));
    		                    			tablero[i][j] = ficha;
    		    						}
    		    						else{
    		    							if(j==N-1 && i==N-1){
    		    								ficha = new Ficha(color-1,tablero[i-1][j].getAbj(),0,0,String.valueOf(id));
    		                        			tablero[i][j] = ficha;
    		    							}
    		    						}
    		    					}
    							}
    						}
    					}
    				}
    			}
    		}
    		id++;
    		color++;
         }
    	}
    	for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
            	ficha =new Ficha(tablero[i][j].getIzq(), tablero[i][j].getArr(), tablero[i][j].getDer(), tablero[i][j].getAbj(), tablero[i][j].getId());
            	fichas.add(ficha);
            	ficha =new Ficha(tablero[i][j].getAbj(), tablero[i][j].getIzq(), tablero[i][j].getArr(), tablero[i][j].getDer(), tablero[i][j].getId());
            	fichas.add(ficha);
            	ficha =new Ficha(tablero[i][j].getDer(), tablero[i][j].getAbj(), tablero[i][j].getIzq(), tablero[i][j].getArr(), tablero[i][j].getId());
            	fichas.add(ficha);
            	ficha =new Ficha(tablero[i][j].getArr(), tablero[i][j].getDer(), tablero[i][j].getAbj(), tablero[i][j].getIzq(), tablero[i][j].getId());
            	fichas.add(ficha);
            }
    	}
    }

	public static boolean existeFicha(Ficha aux){
		for(Ficha f : fichastemp){
			if( aux.getAbj() == f.getAbj() && aux.getArr() == f.getArr() && aux.getDer() == f.getDer() && aux.getIzq() == f.getIzq() )
				return true;
		}
		return false;
	}

	public static void ponerColorDerAbj(Ficha aux){
		boolean encontro = false;
		int colorDer =1;
		int colorAbj=1;
		boolean AbjUltima=true;
		while(!encontro && colorMayor>=colorDer && colorMayor>=colorAbj ){
			aux.setDer(colorDer);
			aux.setAbj(colorAbj);
			if(!existeFicha(aux)){
				encontro=true;
			}
			else{
				if(AbjUltima){
					colorDer++;
					AbjUltima=false;
				}
				else{
					colorAbj++;
					AbjUltima=true;
				}
			}
		}

		colorDer=1;
		colorAbj=1;
		while(!encontro && colorMayor >= colorDer ){
			aux.setDer(colorDer);
			aux.setAbj(colorAbj);
			if(!existeFicha(aux)){
				encontro=true;
			}
			else{
				colorDer++;

			}
		}

		colorDer=1;
		colorAbj=1;
		while(!encontro && colorMayor >= colorAbj ){
			aux.setDer(colorDer);
			aux.setAbj(colorAbj);
			if(!existeFicha(aux)){
				encontro=true;
			}
			else{
				colorAbj++;

			}
		}
		if(!encontro){
			colorMayor++;
			aux.setDer(colorMayor);
			aux.setAbj(1);
		}

	}

	public static void ponerColorAbj(Ficha aux){
		boolean encontro = false;
		int color =1;
		while(!encontro){
			aux.setAbj(color);
			if(!existeFicha(aux)){
				encontro=true;
				if(color>colorMayor)
					colorMayor++;
			}
			color++;
		}
	}

	public static void ponerColorDer(Ficha aux){
		boolean encontro = false;
		int color =1;
		while(!encontro){
			aux.setDer(color);
			if(!existeFicha(aux)){
				encontro=true;
				if(color>colorMayor)
					colorMayor++;
			}
			color++;
		}
	}

	public static void crearFichasAutoMenorColores() {
		fichastemp = new ArrayList<Ficha>();
		fichas = new ArrayList<Ficha>();
		Ficha ficha = null;
		Ficha[][] tablero = new Ficha[N][N];
		int id = 1;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (i == 0 && j == 0) {
					ficha = new Ficha(0, 0, 0, 0, String.valueOf(id));
					ponerColorDerAbj(ficha);
					tablero[i][j] = ficha;
					fichastemp.add(ficha);
				} else {
					if (i == 0 && j != N - 1) {
						ficha = new Ficha(tablero[i][j - 1].getDer(), 0, 0, 0, String.valueOf(id));
						ponerColorDerAbj(ficha);
						tablero[i][j] = ficha;
						fichastemp.add(ficha);
					} else {
						if (i == 0 && j == N - 1) {
							ficha = new Ficha(tablero[i][j - 1].getDer(), 0, 0, 0, String.valueOf(id));
							ponerColorAbj(ficha);
							tablero[i][j] = ficha;
							fichastemp.add(ficha);
						} else {
							if (j == 0 && i != N - 1) {
								ficha = new Ficha(0, tablero[i - 1][j].getAbj(), 0, 0, String.valueOf(id));
								ponerColorDerAbj(ficha);
								tablero[i][j] = ficha;
								fichastemp.add(ficha);
							} else {
								if (j != N - 1 && i != N - 1) {
									ficha = new Ficha(tablero[i][j - 1].getDer(), tablero[i - 1][j].getAbj(), 0, 0, String.valueOf(id));
									ponerColorDerAbj(ficha);
									tablero[i][j] = ficha;
									fichastemp.add(ficha);
								} else {
									if (j == N - 1 && i != N - 1) {
										ficha = new Ficha(tablero[i][j - 1].getDer(), tablero[i - 1][j].getAbj(), 0, 0, String.valueOf(id));
										ponerColorAbj(ficha);
										tablero[i][j] = ficha;
										fichastemp.add(ficha);
									} else {
										if (j == 0 && i == N - 1) {
											ficha = new Ficha(0, tablero[i - 1][j].getAbj(), 0, 0, String.valueOf(id));
											ponerColorDer(ficha);
											tablero[i][j] = ficha;
											fichastemp.add(ficha);
										} else {
											if (j != N - 1 && i == N - 1) {
												ficha = new Ficha(tablero[i][j - 1].getDer(), tablero[i - 1][j].getAbj(), 0, 0, String.valueOf(id));
												ponerColorDer(ficha);
												tablero[i][j] = ficha;
												fichastemp.add(ficha);
											} else {
												if (j == N - 1 && i == N - 1) {
													ficha = new Ficha(tablero[i][j - 1].getDer(), tablero[i - 1][j].getAbj(), 0, 0, String.valueOf(id));
													tablero[i][j] = ficha;
													fichastemp.add(ficha);
												}
											}
										}
									}
								}
							}
						}
					}
				}
				id++;
			}
		}
		for(Ficha f : fichastemp) {// las fichas originales son distintas pero una ficha rotada si puede ser igual a otra original,
			fichas.add(f);
			ficha = new Ficha(f.getAbj(), f.getIzq(), f.getArr(), f.getDer(), f.getId());
			fichas.add(ficha);
			ficha = new Ficha(f.getDer(), f.getAbj(), f.getIzq(), f.getArr(), f.getId());
			fichas.add(ficha);
			ficha = new Ficha(f.getArr(), f.getDer(), f.getAbj(), f.getIzq(), f.getId());
			fichas.add(ficha);
		}
	}


    public static void main(String[] args) throws FileNotFoundException, IOException {

        libres = new Boolean[N*N];
        inicializarLibres(libres);
        inicLogger();
        crearFichasAutoMenorColores();

        Collections.shuffle(fichas);
        logger.log(Level.INFO,"-----Fichas-----");

        for (Ficha f: fichas){
            logger.log(Level.INFO, "f"+f.getId()+": "+f.imprimirse());
        }

        Tablero tablero = new Tablero(N);

        SolverByBacktrack solucion = new SolverByBacktrack(N,fichas,tablero,libres, logger);

        logger.log(Level.INFO,"-----BACKTRACKING-----");
        long startTime = System.nanoTime();
        solucion.solucionRichi(fichas);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("tiempo " + duration);

        logger.log(Level.INFO,"tiempo : " + duration);


    }
}

