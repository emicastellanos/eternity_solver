package utilidades;

import entidades.Ficha;

import java.util.ArrayList;

public class GeneradorFichasUnicasOptimizado extends GeneradorFichas {
    private int sumaColor = 1;

    public GeneradorFichasUnicasOptimizado(int N, int cant_colores){
        super(N,cant_colores);
    }

    private int getColorUnico(){
        int sum = sumaColor;
        colores[sumaColor]+=1;
        sumaColor++;
        if(sumaColor > cantidadColores)
            sumaColor = 1;
        return sum;
    }


    public boolean hacerValidacionesFicha (Ficha ficha, int i, int j, ArrayList<Ficha> generadas){
        		int ant,ant2 = 0;
        		if(!existe(ficha)) {
        			generadas.add(ficha);
        			return true;
        		}
            while(existe(ficha)){
                //si la ficha generada ya existe, se libera los colores de izq y abajo y se eligen al azar hasta que se encuentre una ficha valida.
                if(j!=N-1){
                    colores[ficha.getDer()]-=1;
                    ant = ficha.getDer();
	                ficha.setDer(getColorUnico());
	                if(!existe(ficha)) {
		                generadas.add(ficha);
		                return true;
	                }
	                else{
	                		if(i!=N-1) {
	                			ant2 = ficha.getDer();
		                		colores[ficha.getDer()]-=1;
		                		ficha.setDer(ant);
		                		colores[ant]+=1;
		                		
		                	    colores[ficha.getAbj()]-=1;
		 	                ficha.setAbj(getColorUnico());
		 	                if(!existe(ficha)) {
				                generadas.add(ficha);
				                return true;
			                }
		 	                else {
			 	                	colores[ficha.getDer()]-=1;
			                		ficha.setDer(ant2);
			                		colores[ant2]+=1;
			                		if(!existe(ficha)){
					                generadas.add(ficha);
					                return true;
					             }
		 	                }
	                		}
	                }
                }
                else {
                		if(i!= N-1){
                			colores[ficha.getAbj()]-=1;
                			ficha.setAbj(getColorUnico());
                			if(!existe(ficha)) {
                				generadas.add(ficha);
                				return true;
                			}
                		}
                		else {
                			if(i== N-1 && j== N-1) {
                				return false;
                			}
                		}
               }
            }
			return true;   
    }


}
