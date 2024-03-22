public class MinimoMultihilos {

    static class BuscarMinimoRunnable implements Runnable{ //Clase que calcula el minimo de la porcion de un array
        private int[] array;
        private int inicio;
        private int fin;
        private int minimoParcial;

        public BuscarMinimoRunnable(int[] array, int inicio, int fin){ //Constructor de la clase
            this.array = array;// Inicializamos el atributo array
            this.inicio = inicio;
            this.fin = fin;
            this.minimoParcial = Integer.MAX_VALUE;// Se inicializa minimo parcial de tal manera que cualquier valor sea menor a este
        }

        @Override
        public void run(){// Metodo para buscar el minimo en la porcion de un array
            for(int i=inicio; i<fin; i++){
                if(array[i] < minimoParcial){//Comparacion de valores
                    minimoParcial = array[i];//Actualizar el minimo parcial
                }
            }
        }

        public int getMinimoParcial(){// Metodo para encontrar el minimo parcial obtenido en cada hilo
            return minimoParcial;
        }
    }

    public static int encontrarMinimoMultihilos(int[] array, int numHilos) throws InterruptedException{
        int tamanioSubArray = array.length/numHilos; //Calcula el tamanio de porcion del array para cada hilo
        int minimoGlobal = Integer.MAX_VALUE; //Inicializamos rl minimo global con el valor maximo posible

        Thread[] hilos = new Thread[numHilos]; //Arreglo para almacenar los hilos
        BuscarMinimoRunnable[] runnables = new BuscarMinimoRunnable[numHilos];//Arreglo para almacenar las tareas Runnable

        for(int i=0; i<numHilos; i++){// Creacion y ejecucion de los hilos para buscar el minimo en cada porcion del array
            int inicio = i*tamanioSubArray;// Calcular el indice de inicio de la porcion del array para cada hilo
            int fin = (i==numHilos -1)? array.length : (i+1)*tamanioSubArray;//Calcula el indice de fin de la porcion del array para un hilo
            runnables[i] = new BuscarMinimoRunnable(array, inicio, fin);// Crea una instancia de la clase BuscarMinimoRunnable que busca el minimo en una porcion de array
            hilos[i] = new Thread(runnables[i]);// Crea un nuevo hilo y le asigna la tarea de buscar el minimo
            hilos[i].start();// Inicia el hilo para que comience la ejecucion de la tarea
        }

        for(int i=0; i<numHilos; i++){
            hilos[i].join();// Espera a que todos los hilos terminen su ejecucion
            if(runnables[i].getMinimoParcial() < minimoGlobal){//Combina los resultados de cada hilo para tener un minimo global
                minimoGlobal = runnables[i].getMinimoParcial();
            }
        }

        return minimoGlobal;// Devuelve el minimo global encontrado
    }

    public static void main(String[] args) throws InterruptedException{
        int[] array = {3,5,2,8,1,9,4,6,7};
        int numHilos = 4;// Utilizaremos 4 hilos

        int minimo = encontrarMinimoMultihilos(array, numHilos);//Encontrar el minimo utilizando programacion multihilos
        System.out.println("El minimo es: " + minimo);
    }
}