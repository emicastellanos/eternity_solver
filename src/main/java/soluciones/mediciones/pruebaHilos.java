package soluciones.mediciones;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class pruebaHilos {

    public static class TareaPrueba extends Thread{

        public void ejecutarTarea(){
            Integer cuenta = 1;
            for (int i=0; i<1000000000; i++){
                cuenta = 1 + i * 5;
            }
            System.out.println(Thread.currentThread().getName() + " cuenta :" + cuenta);
        }

        @Override
        public void run() {
            long startTime = System.nanoTime();
            ejecutarTarea();
            long endTime = System.nanoTime();

            BigDecimal duration = new BigDecimal((endTime - startTime));
            BigDecimal durationSecs = (duration.divide(new BigDecimal(1000000000))).setScale(3, RoundingMode.HALF_UP);
            System.out.println((Thread.currentThread().getName() + " TIEMPO CORRIENDO  : " + durationSecs.toString().replace('.', ',') + " SEGUNDOS EN ALGUN core "));
        }
    }

    public static void main (String[] args){
        TareaPrueba t1 = new TareaPrueba();
        TareaPrueba t2 = new TareaPrueba();
        TareaPrueba t3 = new TareaPrueba();
        TareaPrueba t4 = new TareaPrueba();
        long startTime = System.nanoTime();
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        long endTime = System.nanoTime();

        BigDecimal duration = new BigDecimal((endTime - startTime));
        BigDecimal durationSecs = (duration.divide(new BigDecimal(1000000000))).setScale(3, RoundingMode.HALF_UP);
        System.out.println((Thread.currentThread().getName() + " TIEMPO CORRIENDO  : " + durationSecs.toString().replace('.', ',') + " SEGUNDOS EN ALGUN core "));
    }


}