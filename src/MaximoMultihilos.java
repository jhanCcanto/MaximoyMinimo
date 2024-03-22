
import java.util.concurrent.*;
public class MaximoMultihilos {

    static class BuscarMaximoRunnable implements Runnable{// Clase para calcular el maximo en una porcion del array
        private int[] array;
        private int inicio;
        private int fin;
        private int maximoParcial;

        public BuscarMaximoRunnable(int[] array, int inicio, int fin){//Constructor de la clase
            this.array = array;//Inicializamos el atributo array
            this.inicio = inicio;
            this.fin = fin;
            this.maximoParcial = Integer.MIN_VALUE;//Se inicializa maximo parcial de tal manera que cualquier valor sea mayor a este
        }

        public int getMaximoParcial(){//Metodo para encontrar el maximo paricial obtenido en cada hilo
            return maximoParcial;
        }

        @Override
        public void run(){// Metodo para buscar el minimo en la porcion de un array
            for(int i=inicio; i<fin; i++){
                if(array[i] > maximoParcial){//Comparacion de valores
                    maximoParcial = array[i];//Actualizar el maximo parcial
                }
            }
        }
    }

    public static int encontrarMaximoMultihilos(int[] array, int numHilos) throws InterruptedException{
        int tamanioSubArray = array.length/numHilos;// CAlcula el tamanio de porcion del array para cada hilo
        int maximoGlobal = Integer.MIN_VALUE;//Inicializamos el maximo global con el menor valor posible

        Thread[] hilos = new Thread[numHilos];// Arreglo para almacenar hilos
        BuscarMaximoRunnable[] runnables = new BuscarMaximoRunnable[numHilos];// Arreglo para almacenar las tareas Runnables

        for(int i=0; i<numHilos; i++){// Creacion y ejecucion de los hilos para buscar el maximo en cada porcion del array
            int inicio = i*tamanioSubArray;// Calcular el indice de inicio de la porcion del array para cada hilo
            int fin = (i==numHilos -1)? array.length : (i+1)*tamanioSubArray;//Calcula el indice de fin de la porcion del array para un hilo
            runnables[i] = new BuscarMaximoRunnable(array, inicio, fin);//Crea una instancia de la clase BuscarMaximoRunnable que busca el maximo en una porcion de array
            hilos[i] = new Thread(runnables[i]);// Crea un nuevo hilo y le asigna la tarea de buscar el maximo
            hilos[i].start();//Inicia el hilos para wue comience la ejecucion de la tarea
        }

        for(int i=0; i<numHilos; i++){
            hilos[i].join();//Espera a qie todos los hilos terminen su ejecucion
        }

        for(int i=0; i<numHilos; i++){
            int maximoParcial = runnables[i].getMaximoParcial();//Buscamos el maximo parcial
            if(maximoParcial>maximoGlobal){//Comprobamos si el maximo parcial es mayor que el maximo global
                maximoGlobal = maximoParcial;
            }
        }
        return maximoGlobal;// DEvolvemos el maximo global encontrado
    }

    public static void main(String[] args) throws InterruptedException{
        int[] array = {3,5,2,8,1,9,4,6,7};
        int numHilos = 4;

        int maximo = encontrarMaximoMultihilos(array, numHilos);
        System.out.println("El maximo es: " + maximo);
    }
}








