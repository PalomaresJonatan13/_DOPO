package domain;  
 
import java.util.ArrayList;

public class Team extends Participant{
   
    private String manager;
    private String uniform;
    
    private ArrayList<Player> players;
    
    /**
     * Constructs a new Team
     * @param name
     * @param type
     */
    public Team(String name, int minutes, char position, String manager, String uniform){
        super(name, minutes, position);
        this.manager=manager;
        this.uniform=uniform;
        players= new ArrayList<Player>();
    }


     /**
     * Add a new Player
     * @param c
     */   
    public void addPlayer(Player c){
        players.add(c);
    }
       
 
   public int marketValue() throws FifaException {
        int minutesPlayed = 0;
        long weightedSum = 0;
        for (Player p : this.players) {
            minutesPlayed += p.minutes();
        }

        for (Player p : this.players) {
            int playerMarketValue = p.marketValue();
            int playerMinutes = p.minutes();
            weightedSum += playerMarketValue*playerMinutes;
        }

        if (minutesPlayed == 0) {
            throw new FifaException(FifaException.IMPOSSIBLE);
        }

        return (int) weightedSum/minutesPlayed;
    }

   /**
     * Returns the expectet Market Value 
     * @return
     * @throws FifaException, if any marker value or minutes is unknown
     */
    //If more than half of the players have no recorded minutes, the total number of players is used to average. 
    //Otherwise, the average minutes played by known players is used for those whose minutes are unknown.
    
    public int expectedMarketValue() throws FifaException{
        int usedToAverage = 0;
        long weightedSum = 0;
        int playersWithNoMinutes = 0;
        for (Player p : this.players) {
            try {
                usedToAverage += p.minutes();
            } catch (FifaException e) {
                playersWithNoMinutes++;
            }
        }

        int totalPlayers = this.players.size();
        // division by 0
        int averageMinutes = usedToAverage/(totalPlayers - playersWithNoMinutes);
        usedToAverage = (playersWithNoMinutes > totalPlayers/2) ?
            totalPlayers - playersWithNoMinutes : 
            0;
        for (Player p : this.players) {
            int playerMarketValue = p.marketValue();
            if (playersWithNoMinutes > totalPlayers/2) {
                weightedSum += playerMarketValue;
            } else {
                try {
                    int playerMinutes = p.minutes();
                    weightedSum += playerMarketValue*playerMinutes;
                } catch (FifaException e) {
                    weightedSum += playerMarketValue*averageMinutes;
                }
            }
        }

        if (usedToAverage == 0) {
            throw new FifaException(FifaException.IMPOSSIBLE);
        }

        return (int) weightedSum/minutesPlayed;
    }
    
    
    /**
     * Returns the Marked Value using default values 
     * @return
     * @throws FifaException, if the resistance cannot be calculate
     */
    //If a player's market value or minutes played are unknown, default values ​​are used.
    public int defaultMarkedValue(int defaultMarketValue, int defaultMinutes) throws FifaException{
        return 0;
    }
    
    
    @Override
    public String data() throws FifaException{
        StringBuffer answer=new StringBuffer();
        answer.append(name+".\t Grupo: "+position+".\t Valor Promedio:" +marketValue());
        for(Player p: players) {
            answer.append("\n\t"+p.data());
        }
        return answer.toString();
    } 
    

}
