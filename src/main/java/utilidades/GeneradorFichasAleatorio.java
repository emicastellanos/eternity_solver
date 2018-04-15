package utilidades;

import entidades.Ficha;

import java.util.ArrayList;

public class GeneradorFichasAleatorio extends GeneradorFichas {

    public GeneradorFichasAleatorio(int N, int cant_colores){
        super(N,cant_colores);
    }

    /**@Return un valor aleatorio dentro de los colores posibles para usar **/
    private int getColorAleatorio(){
        Double i= Math.random()* cantidadColores;
        while (i.intValue()==0){
            i= Math.random()* cantidadColores;
        }
        colores[i.intValue()]+=1;
        return i.intValue();
    }

    public boolean hacerValidacionesFicha (Ficha ficha, int i, int j, ArrayList<Ficha> generadas){
        boolean corte = false;
        while(!corte){
            if(existe(ficha)){
                //si la ficha generada ya existe, se libera los colores de izq y abajo y se eligen al azar hasta que se encuentre una ficha valida.
                if(j!=N-1){
                    colores[ficha.getDer()]-=1;
                    ficha.setDer(getColorAleatorio());
                }
                if(i!=N-1){
                    colores[ficha.getAbj()]-=1;
                    ficha.setAbj(getColorAleatorio());
                }
                if(i==N-1 && j==N-1){
                    return false;
                }
            }else{
                generadas.add(ficha);
                return true;
            }
        }
		return corte;
    }
}
