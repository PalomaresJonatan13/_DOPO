package domain;
import java.awt.Color;

/**
 * Árbol vivo en el bosque: cambia de color con el tiempo, envejece y puede morir por falta de energía.
 */
public class Tree extends LivingThing implements Thing{
    private Forest forest;
    
    protected int row,column;    
    protected Color color;
    private int tictac;
    
    /**
     * Coloca un árbol en la celda indicada y lo registra en el bosque.
     * @param forest bosque contenedor
     * @param row fila
     * @param column columna
     */
    public Tree(Forest forest,int row, int column){
        this.forest=forest;
        this.row=row;
        this.column=column;
        this.color=Color.PINK;
        this.tictac=0;
        this.forest.setThing(row,column,(Thing)this);    
    }
    

    /**
     * @return fila actual
     */
    public final int getRow(){
        return row;
    }

    /**
     * @return columna actual
     */
    public final int getColumn(){
        return column;
    }

    
    /**
     * @return color de representación actual
     */
    public final Color getColor(){
        return color;
    }

    /**
     * @return {@link Thing#ROUND}
     */
    public final int shape() {
        return Thing.ROUND;
    }
    

    /**
     * Avanza ciclo de color, edad y posible gasto de energía.
     */
    public void ticTac(){
        tictac++;
        color=(tictac % 4==0? Color.PINK:
               tictac % 4==1? Color.GREEN:
               tictac % 4==2? Color.ORANGE:
               Color.GRAY);
        if (tictac % 4 == 1){
            years+=1;
        }
        if (tictac % 4 == 3){
            boolean OK=step();
            if (! OK){
                die();
            }
        }
    }
    
    /**
     * Libera la celda del bosque.
     */
    public void die(){
        forest.setThing(row, column,null);
    }
    
    /**
     * @return texto {@code NombreClase fila,columna} para exportación
     */
    @Override
    public String toString() {
        return String.format("%s %d,%d", this.getClass().getSimpleName(), this.row, this.column);
    }
  
        
}
