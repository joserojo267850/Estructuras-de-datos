package tablahash;

import java.util.LinkedList;

/**
 * Implementación de una Tabla Hash utilizando encadenamiento separado
 * (separate chaining) para resolver colisiones.
 * 
 * La estructura soporta inserción, búsqueda, eliminación y 
 * redimensionamiento dinámico cuando el factor de carga supera 0.75.
 *
 * @param <K> Tipo de la clave (debe implementar hashCode y equals)
 * @param <V> Tipo del valor asociado a la clave
 */
public class TablaHash<K, V> implements Diccionario<K, V> {

    /**
     * Clase interna que representa un par clave–valor dentro de la estructura.
     * Cada nodo se almacena dentro de una lista enlazada en un bucket.
     */
    private class Nodo {
        K key;
        V value;

        /**
         * Constructor de un nodo que almacena clave y valor.
         * 
         * @param key   Clave única del elemento
         * @param value Valor asociado a la clave
         */
        public Nodo(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    /** Arreglo de listas enlazadas que representa la tabla hash */
    private LinkedList<Nodo>[] tabla;

    /** Cantidad actual de elementos almacenados */
    private int size;

    /** Tamaño del arreglo (número de buckets) */
    private int capacidad;

    /** Factor máximo permitido antes de hacer resize */
    private static final double FACTOR_CARGA_MAX = 0.75;

    /**
     * Constructor por defecto.
     * Inicializa la tabla con capacidad 11 e instancia cada bucket.
     */
    public TablaHash() {
        this.capacidad = 11;
        this.tabla = new LinkedList[capacidad];
        this.size = 0;

        for (int i = 0; i < capacidad; i++) {
            tabla[i] = new LinkedList<>();
        }
    }

    /**
     * Calcula el índice dentro del arreglo usando hashCode.
     * Maneja hash negativos usando & con 0x7fffffff.
     *
     * @param key Clave a procesar
     * @return Índice válido dentro del arreglo de buckets
     */
    private int hash(K key) {
        return (key.hashCode() & 0x7fffffff) % capacidad;
    }

    /**
     * Inserta un nuevo par clave–valor en la tabla.
     * Si la clave ya existe, su valor se actualiza.
     *
     * @param key   Clave única del elemento
     * @param value Valor a almacenar
     * @throws NullPointerException si key es null
     */
    @Override
    public void put(K key, V value) {

        if (key == null)
            throw new NullPointerException("La clave no puede ser null");

        int indice = hash(key);
        LinkedList<Nodo> lista = tabla[indice];

        // Buscar si la clave ya existe
        for (Nodo n : lista) {
            if (n.key.equals(key)) {
                n.value = value; // Actualizar
                return;
            }
        }

        // Si no existe, insertar nuevo nodo
        lista.add(new Nodo(key, value));
        size++;

        // Verificar factor de carga
        if ((double) size / capacidad >= FACTOR_CARGA_MAX) {
            resize();
        }
    }

    /**
     * Busca y retorna el valor asociado a una clave.
     *
     * @param key Clave a buscar
     * @return Valor asociado, o null si no existe la clave
     */
    @Override
    public V get(K key) {
        int indice = hash(key);
        LinkedList<Nodo> lista = tabla[indice];

        for (Nodo n : lista) {
            if (n.key.equals(key)) {
                return n.value;
            }
        }
        return null;
    }

    /**
     * Elimina un nodo por su clave.
     *
     * @param key Clave del elemento a eliminar
     * @return Valor eliminado o null si no existía
     */
    @Override
    public V remove(K key) {
        int indice = hash(key);
        LinkedList<Nodo> lista = tabla[indice];

        for (int i = 0; i < lista.size(); i++) {
            Nodo n = lista.get(i);

            if (n.key.equals(key)) {
                lista.remove(i);
                size--;
                return n.value;
            }
        }

        return null;
    }

    /**
     * Verifica si una clave está almacenada en la tabla.
     * 
     * @param key Clave a buscar
     * @return true si la clave existe, false de lo contrario
     */
    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Retorna la cantidad de elementos almacenados.
     *
     * @return Número de elementos en la tabla
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Duplica la capacidad del arreglo y reinserta todos los elementos.
     * Este proceso recalcula los índices hash debido al cambio de tamaño.
     */
    private void resize() {
        LinkedList<Nodo>[] tablaVieja = tabla;

        capacidad *= 2;
        tabla = new LinkedList[capacidad];
        size = 0;

        for (int i = 0; i < capacidad; i++) {
            tabla[i] = new LinkedList<>();
        }

        // Reinsertar todos los elementos
        for (LinkedList<Nodo> bucket : tablaVieja) {
            for (Nodo n : bucket) {
                put(n.key, n.value); 
            }
        }
    }

    public static void main(String[] args) {
        
    }
}
