package utilidades;

import entidades.Ficha;

import java.util.ArrayList;

public class GeneradorFichasUnicas extends GeneradorFichas {
    private int sumaColor = 1;

    public GeneradorFichasUnicas(int N, int cant_colores){
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
        boolean corte = false;
        while(!corte){
            if(existe(ficha)){
                //si la ficha generada ya existe, se libera los colores de izq y abajo y se eligen al azar hasta que se encuentre una ficha valida.
                if(j!=N-1){
                    colores[ficha.getDer()]-=1;
                    ficha.setDer(getColorUnico());
                }
                if(i!=N-1){
                    colores[ficha.getAbj()]-=1;
                    ficha.setAbj(getColorUnico());
                }
                if(i==N-1 && j==N-1){
                    return false;
                }
            }else{
                generadas.add(ficha);
                return true;
            }
        }
		return true;
    }

}
