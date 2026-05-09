package domain;

import java.io.*;
import java.util.*;

import domain.forestFire.CommonTree;
import domain.forestFire.Fire;
import domain.forestFire.Land;
import domain.forestFire.Water;

/**
 * Bosque cuadrado de celdas que contienen {@link Thing}; admite simulación tic-tac y persistencia.
 */
public class Forest implements Serializable{
    static private int SIZE=25;
    private Thing[][] places;
    private int tictac;
    private HashMap<String, Integer> forestFireCoords = new HashMap<>(Map.of(
        "originRow", 0, //2,
        "originCol", 0, //5,
        "width", 9, //9,
        "height", 11 //11
    ));

    /**
     * Bosque vacío o con {@link #someThings()} según {@code empty}; sin región de incendio.
     * @param empty si es {@code true}, no se rellenan elementos por defecto
     */
    public Forest(boolean empty) { // without forestFire
        places=new Thing[SIZE][SIZE];
        tictac = 0;
        for (int r=0;r<SIZE;r++){
            for (int c=0;c<SIZE;c++){
                places[r][c]=null;
            }
        }
        if (!empty) {
            this.someThings();
        }
    }

    /**
     * Bosque por defecto: elementos de ejemplo e incendio simulado en una región rectangular.
     */
    public Forest() {
        this(true);
        someThings();
        this.createForestFire();
    }

    /**
     * Marcador de método legacy no implementado (abrir).
     * @param file archivo solicitado
     * @throws ForestException siempre
     */
    public void openFile00(File file) throws ForestException {
        throw new ForestException(ForestException.FILE_METHODS_NOT_IMPLEMENTED("Abrir", file.getName()));
    }

    /**
     * Carga un bosque serializado desde un archivo {@code .dat}.
     * @param file archivo de entrada
     * @return bosque deserializado
     * @throws ForestException si la extensión no es válida o la lectura falla
     */
    public Forest openFile(File file) throws ForestException {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".dat")) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                return (Forest) in.readObject();
            } catch (IOException | ClassNotFoundException | ClassCastException e) {
                throw new ForestException(
                    ForestException.persistenceFailure("Abrir", file, e), e);
            }
        }
        throw new ForestException(ForestException.WRONG_EXTENSION("Abrir", file.getName()));
    }

    /**
     * Variante anterior de {@link #openFile}: mensaje genérico sin causa encadenada ante error de lectura.
     * @param file archivo de entrada
     * @return bosque deserializado
     * @throws ForestException si falla la operación
     */
    public Forest open01(File file) throws ForestException {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".dat")) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                return (Forest) in.readObject();
            } catch (IOException | ClassNotFoundException | ClassCastException e) {
                throw new ForestException(ForestException.PERSISTENCE_EXCEPTION);
            }
        }
        throw new ForestException(ForestException.WRONG_EXTENSION("Abrir", file.getName()));
    }

    /**
     * Marcador de método legacy no implementado (guardar).
     * @param file archivo solicitado
     * @throws ForestException siempre
     */
    public void saveAs00(File file) throws ForestException {
        throw new ForestException(ForestException.FILE_METHODS_NOT_IMPLEMENTED("Guardar", file.getName()));
    }

    /**
     * Guarda este bosque en un archivo {@code .dat} por serialización Java.
     * @param file archivo de destino
     * @throws ForestException si la extensión no es válida o la escritura falla
     */
    public void saveAs(File file) throws ForestException {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".dat")) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
                out.writeObject(this);
            } catch (IOException e) {
                throw new ForestException(
                    ForestException.persistenceFailure("Guardar", file, e), e);
            }
            return;
        }
        throw new ForestException(ForestException.WRONG_EXTENSION("Guardar", file.getName()));
    }

    /**
     * Variante anterior de {@link #saveAs}: mensaje genérico sin causa ante error de escritura.
     * @param file archivo de destino
     * @throws ForestException si falla la operación
     */
    public void saveAs01(File file) throws ForestException {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".dat")) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
                out.writeObject(this);
            } catch (IOException e) {
                throw new ForestException(ForestException.PERSISTENCE_EXCEPTION);
            }
            return;
        }
        throw new ForestException(ForestException.WRONG_EXTENSION("Guardar", file.getName()));
    }

    /**
     * Marcador de método legacy no implementado (importar).
     * @param file archivo solicitado
     * @throws ForestException siempre
     */
    public void importFile00(File file) throws ForestException {
        throw new ForestException(ForestException.FILE_METHODS_NOT_IMPLEMENTED("Importar", file.getName()));
    }

    /**
     * Reemplaza el estado del bosque leyendo líneas {@code Tipo fila,columna} desde un {@code .txt}.
     * @param file archivo de texto
     * @throws ForestException si extensión inválida, I/O, línea mal formada o tipo desconocido
     */
    public void importFile(File file) throws ForestException {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".txt")) {
            Thing[][] placesSecurityCopy = this.places.clone();
            this.clear();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] info = line.split(" ");
                    if (info.length == 2) {
                        String obj = info[0];
                        String[] coords = info[1].split(",");
                        int row = Integer.parseInt(coords[0]);
                        int col = Integer.parseInt(coords[1]);
                        if (obj.equals("Tree")) {
                            new Tree(this, row, col);
                        } else if (obj.equals("Squirrel")) {
                            new Squirrel(this, row, col);
                        } else if (obj.equals("Shadow")) {
                            new Shadow(this, row, col);
                        } else if (obj.equals("CursedTree")) {
                            new CursedTree(this, row, col);
                        } else if (obj.equals("BlackHole")) {
                            new BlackHole(this, row, col);
                        } else if (obj.equals("CommonTree")) {
                            new CommonTree(this, row, col);
                        } else if (obj.equals("Fire")) {
                            new Fire(this, row, col);
                        } else if (obj.equals("Land")) {
                            new Land(this, row, col);
                        } else if (obj.equals("Water")) {
                            new Water(this, row, col);
                        } else {
                            throw new ForestException(ForestException.INVALID_OBJECT(obj));
                        }
                    } else {
                        throw new ForestException(
                    ForestException.persistenceFailure("Importar", file));
                    }
                }
                this.tictac = 0;
            } catch (IOException e) {
                this.places = placesSecurityCopy.clone();
                throw new ForestException(
                    ForestException.persistenceFailure("Importar", file, e), e);
            }
            return;
        }
        throw new ForestException(ForestException.WRONG_EXTENSION("Importar", file.getName()));
    }

    /**
     * Variante anterior de {@link #importFile}: mensaje genérico sin causa ante error de I/O.
     * @param file archivo de texto
     * @throws ForestException si falla la operación
     */
    public void import01(File file) throws ForestException {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".txt")) {
            Thing[][] placesSecurityCopy = this.places.clone();
            this.clear();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] info = line.split(" ");
                    if (info.length == 2) {
                        String obj = info[0];
                        String[] coords = info[1].split(",");
                        int row = Integer.parseInt(coords[0]);
                        int col = Integer.parseInt(coords[1]);
                        if (obj.equals("Tree")) {
                            new Tree(this, row, col);
                        } else if (obj.equals("Squirrel")) {
                            new Squirrel(this, row, col);
                        } else if (obj.equals("Shadow")) {
                            new Shadow(this, row, col);
                        } else if (obj.equals("CursedTree")) {
                            new CursedTree(this, row, col);
                        } else if (obj.equals("BlackHole")) {
                            new BlackHole(this, row, col);
                        } else if (obj.equals("CommonTree")) {
                            new CommonTree(this, row, col);
                        } else if (obj.equals("Fire")) {
                            new Fire(this, row, col);
                        } else if (obj.equals("Land")) {
                            new Land(this, row, col);
                        } else if (obj.equals("Water")) {
                            new Water(this, row, col);
                        } else {
                            throw new ForestException(ForestException.INVALID_OBJECT(obj));
                        }
                    }
                }
                this.tictac = 0;
            } catch (IOException e) {
                this.places = placesSecurityCopy.clone();
                throw new ForestException(ForestException.PERSISTENCE_EXCEPTION);
            }
            return;
        }
        throw new ForestException(ForestException.WRONG_EXTENSION("Importar", file.getName()));
    }

    /**
     * Marcador de método legacy no implementado (exportar).
     * @param file archivo solicitado
     * @throws ForestException siempre
     */
    public void exportAs00(File file) throws ForestException {
        throw new ForestException(ForestException.FILE_METHODS_NOT_IMPLEMENTED("Exportar", file.getName()));
    }

    /**
     * Escribe una línea por cada celda ocupada usando {@link Object#toString()} de cada {@link Thing}.
     * @param file archivo {@code .txt} de salida
     * @throws ForestException si la extensión no es válida o la escritura falla
     */
    public void exportAs(File file) throws ForestException {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".txt")) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                for (int r=0; r<SIZE; r++) {
                    for (int c=0; c<SIZE; c++) {
                        if (this.places[r][c] != null) {
                            writer.println(this.places[r][c].toString());
                        }
                    }
                }
            } catch (IOException e) {
                throw new ForestException(
                    ForestException.persistenceFailure("Exportar", file, e), e);
            }
            return;
        }
        throw new ForestException(ForestException.WRONG_EXTENSION("Exportar", file.getName()));
    }

    /**
     * Variante anterior de {@link #exportAs}: mensaje genérico sin causa ante error de I/O.
     * @param file archivo de salida
     * @throws ForestException si falla la operación
     */
    public void exportAs01(File file) throws ForestException {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".txt")) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                for (int r=0; r<SIZE; r++) {
                    for (int c=0; c<SIZE; c++) {
                        if (this.places[r][c] != null) {
                            writer.println(this.places[r][c].toString());
                        }
                    }
                }
            } catch (IOException e) {
                throw new ForestException(ForestException.PERSISTENCE_EXCEPTION);
            }
            return;
        }
        throw new ForestException(ForestException.WRONG_EXTENSION("Exportar", file.getName()));
    }

    /**
     * Vacía todas las celdas del bosque.
     */
    private void clear() {
        for (int r=0; r<SIZE; r++) {
            for (int c=0; c<SIZE; c++) {
                this.places[r][c] = null;
            }
        }
    }

    /**
     * @return lado del cuadrado (número de filas y columnas)
     */
    public int  getSize(){
        return SIZE;
    }

    /**
     * @param r fila
     * @param c columna
     * @return contenido de la celda o {@code null}
     */
    public Thing getThing(int r,int c){
        return places[r][c];
    }

    /**
     * Asigna el contenido de una celda.
     * @param r fila
     * @param c columna
     * @param e elemento o {@code null}
     */
    public void setThing(int r, int c, Thing e){
        places[r][c]=e;
    }

    /**
     * Rellena el bosque con árboles, ardillas, sombras y árboles malditos de demostración.
     */
    public void someThings(){   
        new Tree(this, 10, 10);
        new Tree(this, 15, 15);

        new Squirrel(this, 2, 15);
        new Squirrel(this, 4, 16);

        new Shadow(this, 5, 20);
        new Shadow(this, 20, 12);

        new CursedTree(this, 21, 17);
        new CursedTree(this, 8, 6);

        /* BlackHole jonatan = new BlackHole(this, 16, 11);
        BlackHole santiago = new BlackHole(this, 6, 23); */
    }
    
    /**
     * Cuenta vecinos inmediatos con instancia de la misma clase que la celda {@code (r,c)}.
     * @param r fila
     * @param c columna
     * @return número de vecinos de la misma clase
     */
    public int neighborsEquals(int r, int c){
        int num=0;
        if (inForest(r,c) && places[r][c]!=null){
            for(int dr=-1; dr<2;dr++){
                for (int dc=-1; dc<2;dc++){
                    if ((dr!=0 || dc!=0) && inForest(r+dr,c+dc) && 
                    (places[r+dr][c+dc]!=null) &&  (places[r][c].getClass()==places[r+dr][c+dc].getClass())) num++;
                }
            }
        }
        return num;
    }

    /**
     * Celdas vecinas a distancia entera {@code distance} en las ocho direcciones (anillo 3×3 escalado).
     * @param forest bosque de referencia
     * @param row fila central
     * @param column columna central
     * @param distance factor de paso (1 = vecinos inmediatos)
     * @return lista de pares {@code [fila, columna]}
     */
    public static List<Integer[]> neighborCells(Forest forest, int row, int column, int distance) {
        List<Integer[]> neighborCells = new ArrayList<>();
        int forestSize = forest.getSize();

        for(int dr=-1; dr<2; dr++){
            for (int dc=-1; dc<2; dc++){
                int dr_ = dr * distance;
                int dc_ = dc * distance;
                if (
                    (dr_!=0 || dc_!=0) &&
                    ((0<=row+dr_) && (row+dr_<forestSize)) &&
                    ((0<=column+dc_) && (column+dc_<forestSize))
                    // (this.forest.getThing(this.row+dr, this.column+dc) == null)
                ) {
                    neighborCells.add(new Integer[] {row+dr_, column+dc_});
                };
            }
        }
        return neighborCells;
    }

    /**
     * Equivalente a {@link #neighborCells(Forest, int, int, int)} con {@code distance = 1}.
     * @param forest bosque
     * @param row fila
     * @param column columna
     * @return celdas vecinas inmediatas dentro del bosque
     */
    public static List<Integer[]> neighborCells(Forest forest, int row, int column) {
        return neighborCells(forest, row, column, 1);
    }

    /**
     * @param r fila
     * @param c columna
     * @return {@code true} si la coordenada es válida y la celda está vacía
     */
    public boolean isEmpty(int r, int c){
        return (inForest(r,c) && places[r][c]==null);
    }    
        
    /**
     * @param r fila
     * @param c columna
     * @return {@code true} si está dentro del tablero
     */
    private boolean inForest(int r, int c){
        return ((0<=r) && (r<SIZE) && (0<=c) && (c<SIZE));
    }

    /**
     * @return contador global de tic-tacs del bosque
     */
    public int getTictac() {
        return this.tictac;
    }
    
   
    /**
     * Incrementa el tic-tac global y delega en cada {@link Thing} no nulo.
     */
    public void ticTac(){
        this.tictac++;
        for (int j=0; j<this.getSize(); j++) {
            for (int k=0; k<this.getSize(); k++) {
                Thing thing = this.places[j][k];
                if (thing != null) this.places[j][k].ticTac();
            }
        }
    }


    /**
     * Crea la subregión de simulación de incendio con {@link Land}, {@link CommonTree}, {@link Fire} y {@link Water}.
     */
    private void createForestFire() {
        int row = forestFireCoords.get("originRow");
        int col = forestFireCoords.get("originCol");
        int width = forestFireCoords.get("width");
        int height = forestFireCoords.get("height");
        Random random = new Random();

        for (int j=row; j<row+height; j++) {
            for (int k=col; k<col+width; k++) {
                float randomFloat = random.nextFloat();

                if (randomFloat < 0.6) {
                    new Land(this, j, k);
                } else if (randomFloat < 0.75) {
                    new CommonTree(this, j, k);
                } else if (randomFloat < 0.9) {
                    new Fire(this, j, k);
                } else {
                    new Water(this, j, k);
                }
            }
        }
    }
}
