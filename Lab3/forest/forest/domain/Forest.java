package domain;
import java.util.*;

/*No olviden adicionar la documentacion*/
public class Forest{
    static private int SIZE=25;
    private Thing[][] places;
    private int tictac;
    
    public Forest() {
        places=new Thing[SIZE][SIZE];
        tictac = 0;
        for (int r=0;r<SIZE;r++){
            for (int c=0;c<SIZE;c++){
                places[r][c]=null;
            }
        }
        someThings();
    }

    public int  getSize(){
        return SIZE;
    }

    public Thing getThing(int r,int c){
        return places[r][c];
    }

    public void setThing(int r, int c, Thing e){
        places[r][c]=e;
    }

    public void someThings(){   
        Tree beard = new Tree(this, 10, 10);
        Tree soul = new Tree(this, 15, 15);

        Squirrel scrat = new Squirrel(this, 2, 3);
        Squirrel sandy = new Squirrel(this, 4, 4);
    }
    
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
   

    public boolean isEmpty(int r, int c){
        return (inForest(r,c) && places[r][c]==null);
    }    
        
    private boolean inForest(int r, int c){
        return ((0<=r) && (r<SIZE) && (0<=c) && (c<SIZE));
    }

    public int getTictac() {
        return this.tictac;
    }
    
   
    public void ticTac(){
        this.tictac++;
        for (int j=0; j<this.getSize(); j++) {
            for (int k=0; k<this.getSize(); k++) {
                Thing thing = this.places[j][k];
                if (thing != null) this.places[j][k].ticTac();
            }
        }
    }

}
