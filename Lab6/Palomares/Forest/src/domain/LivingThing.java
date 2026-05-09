package domain;


import java.io.Serializable;

/**
 * Ser con energía y edad que puede consumir energía al actuar y morir.
 */
public abstract class LivingThing implements Serializable{
    
    protected int years;
    private int energy;

    /**
     * Crea un ser vivo con energía inicial 100 y edad 0.
     */
    public LivingThing(){
        energy=100;
        years=0;
    }

    /**
     * Intenta consumir un punto de energía.
     * @return {@code true} si había energía y se gastó una unidad
     */
    final boolean step(){
        boolean ok=false;
        if (energy>=1){
            energy-=1;
            ok=true;
        }
        return ok;
    }    
    

    /**
     * @return energía actual
     */   
    public final int getEnergy(){
        return energy;
    }    

    /**
     * Los {@link LivingThing} se consideran vivos para la interfaz {@link Thing}.
     * @return siempre {@code true}
     */
    public final boolean isLivingThing(){
        return true;
    }

    /**
     * Elimina al ser del bosque o marca su muerte; implementación por defecto vacía.
     */
    public void die() {};    
}
