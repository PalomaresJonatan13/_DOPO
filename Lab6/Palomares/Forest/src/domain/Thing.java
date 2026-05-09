package domain;
import java.awt.Color;
import java.io.Serializable;

/**
 * Elemento que puede colocarse en un {@link Forest} y evolucionar en cada tic-tac.
 */
public interface Thing extends Serializable{
    /** Forma visual redonda (óvalo). */
    public static final int ROUND = 1;
    /** Forma visual cuadrada. */
    public static final int SQUARE = 2;
    /** Tipo sombra (dibujo especial en la GUI). */
    public static final int SHADOW = 3;
        
    /**
     * Avanza el estado del elemento un paso de simulación.
     */
    public void ticTac();
    
    /**
     * @return código de forma ({@link #ROUND}, {@link #SQUARE} o {@link #SHADOW})
     */
    public default int shape(){
        return SQUARE;
    }
    
    /**
     * @return color usado para pintar este elemento
     */
    public default Color getColor(){
        return Color.BLACK;
    }
    
    /**
     * @return {@code true} si la celda solo contiene este tipo de cosa
     */
    public default boolean isOnlyThing(){
        return true;
    }
    
    /**
     * @return {@code true} si implementa comportamiento de ser vivo
     */
    public default boolean isLivingThing(){
        return false;
    }    
     
}
