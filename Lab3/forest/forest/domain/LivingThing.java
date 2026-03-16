package domain;
import java.awt.Color;


public abstract class LivingThing{
    
    protected int years;
    private int energy;

    /**Create a new LivingThing
     * 
     */
    public LivingThing(){
        energy=100;
        years=0;
    }


    /**The LivingThing makes one step
     * 
     */
    final boolean step(){
        boolean ok=false;
        if (energy>=1){
            energy-=1;
            ok=true;
        }
        return ok;
    }    
    

    
     /**Returns the energy
    @return 
     */   
    public final int getEnergy(){
        return energy;
    }    

    /**It's an LivingThing
     */
    public final boolean isLivingThing(){
        return true;
    }  
    
}
