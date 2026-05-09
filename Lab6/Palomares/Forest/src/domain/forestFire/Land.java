package domain.forestFire;
import domain.*;

import java.util.*;
import java.awt.Color;

/**
 * Celda de tierra; puede generar fuego o agua aleatoriamente en cada tic-tac.
 */
public class Land extends ForestFireThing {
    /**
     * Tierra en la posición dada.
     * @param forest bosque
     * @param row fila
     * @param column columna
     */
    public Land(Forest forest,int row, int column){
        super(forest, row, column);  
        this.color =  new Color(117, 182, 106);
    }

    /**
     * Ignora la lógica de vecinos de la superclase y aplica probabilidades locales de ignición o agua.
     */
    public void ticTac() {
        Random random = new Random();
        float randomFloat = random.nextFloat();

        if ((1-randomFloat) < 0.1) {
            new Fire(this.forest, this.row, this.column);
        } else if (randomFloat < 0.05) {
            new Water(this.forest, this.row, this.column);
        }
    }
}
