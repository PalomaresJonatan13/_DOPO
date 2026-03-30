package tower;

class OpenerCup extends Cup {
    protected OpenerCup(int index, int blockSize, int towerMargin, int towerWidth, int towerHeight) {
        super(index, blockSize, towerMargin, towerWidth, towerHeight);
    }

    /* public TowerItem onPush(Tower tower) {
        
        save visibility state
        make invisible
        popCup
        disable
        push dummy
        dummy = Cup.getCup(index)
        while
            if element is lid and its heightReached = cups's heightReached - the cup's height
                popLid
        return dummy
       
    } */
}
