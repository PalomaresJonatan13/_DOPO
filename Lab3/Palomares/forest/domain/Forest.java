package domain;

import java.util.*;

import domain.forestFire.CommonTree;
import domain.forestFire.Fire;
import domain.forestFire.Land;
import domain.forestFire.Water;

/*No olviden adicionar la documentacion*/
public class Forest{
    static private int SIZE=25;
    private Thing[][] places;
    private int tictac;
    private HashMap<String, Integer> forestFireCoords = new HashMap<>(Map.of(
        "originRow", 2, //2,
        "originCol", 5, //5,
        "width", 9, //9,
        "height", 11 //11
    ));
    
    public Forest() {
        this(2, 5, 9, 11);
    }

    public Forest(int forestFireX, int forestFireY, int forestFireWidth, int forestFireHeight) {
        places=new Thing[SIZE][SIZE];
        tictac = 0;
        for (int r=0;r<SIZE;r++){
            for (int c=0;c<SIZE;c++){
                places[r][c]=null;
            }
        }
        someThings();
        this.forestFireCoords.put("originRow", forestFireX);
        this.forestFireCoords.put("originCol", forestFireY);
        this.forestFireCoords.put("width", forestFireWidth);
        this.forestFireCoords.put("height", forestFireHeight);
        this.createForestFire();
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

        Shadow thief = new Shadow(this, 5, 20);
        Shadow lass = new Shadow(this, 20, 12);

        CursedTree palomares = new CursedTree(this, 21, 17);
        CursedTree castaneda = new CursedTree(this, 8, 6);

        /* BlackHole jonatan = new BlackHole(this, 16, 11);
        BlackHole santiago = new BlackHole(this, 6, 23); */
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

    public static List<Integer[]> neighborCells(Forest forest, int row, int column) {
        return neighborCells(forest, row, column, 1);
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
