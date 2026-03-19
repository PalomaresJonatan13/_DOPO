package domain;
import java.awt.Color;

/*No olviden adicionar la documentacion*/
public interface Thing{
    public static final int ROUND = 1;
    public static final int SQUARE = 2;
    public static final int SHADOW = 3;
        
    
    public void ticTac();
    
    public default int shape(){
        return SQUARE;
    }
    
    public default Color getColor(){
        return Color.BLACK;
    }
    
    public default boolean isOnlyThing(){
        return true;
    }
    
    public default boolean isLivingThing(){
        return false;
    }    
     
}
