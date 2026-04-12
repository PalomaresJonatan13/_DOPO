package domain;  

import java.util.ArrayList;


public class Team extends Participant{
    private String manager;
    private String uniform;
    
    private ArrayList<Player> players = new ArrayList<Player>();
    
    /**
     * Constructs a new Team
     * @param name
     * @param type
     */
    public Team(String name, int minutes, char position, String manager, String uniform) {
        super(name, minutes, position);
        this.manager = manager;
        this.uniform = uniform;
    }

    // ----------------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------

     /**
     * Add a new Player
     * @param player
     */   
    public void addPlayer(Player player) {
        if (player == null) throw new FifaArgumentException("The player intended to be added to the team does not exist.");
        players.add(player);
    }
    
    /**
     * Returns the Market Value
     * @return
     * @throws FifaException, if any market value or minutes is unknown, or if all minutes are zero
     */
    // calculated as a weighted average of the market value of the players, with their minutes as weights
    public int marketValue() throws FifaException {
        int minutesPlayed = 0;
        for (Player p : this.players) {
            minutesPlayed += p.minutes();
        }

        if (minutesPlayed == 0) throw new FifaException(FifaException.IMPOSSIBLE);

        double weightedAverage = 0;
        for (Player p : this.players) {
            int playerMarketValue = p.marketValue();
            int playerMinutes = p.minutes();
            weightedAverage += playerMarketValue*(playerMinutes/((double) minutesPlayed)) ;
        }

        return (int) (weightedAverage);
    }

   /**
     * Returns the expected Market Value 
     * @return
     * @throws FifaException, if any market value is unknown, or if all minutes are zero
     */
    //If more than half of the players have no recorded minutes, the total number of players is used to average. 
    //Otherwise, the average minutes played by known players is used for those whose minutes are unknown.
    public int expectedMarketValue() throws FifaException {
        int totalMinutes = 0;
        double weightedAverage = 0;
        int playersWithNoMinutes = 0;
        for (Player p : this.players) {
            try {
                totalMinutes += p.minutes();
            } catch (FifaException e) {
                playersWithNoMinutes++;
            }
        }

        if (totalMinutes == 0) throw new FifaException(FifaException.IMPOSSIBLE);
        int totalPlayers = this.players.size();
        int playersWithMinutes = totalPlayers - playersWithNoMinutes;
        boolean minutesCondition = playersWithNoMinutes > totalPlayers/2;
        double averageMinutes = (playersWithMinutes != 0) ? totalMinutes/((double) playersWithMinutes) : 0;
        double denominator =  (minutesCondition) ? playersWithMinutes : totalPlayers*averageMinutes;

        for (Player p : this.players) {
            int playerMarketValue = p.marketValue();
            try {
                int playerMinutes = p.minutes();
                weightedAverage += playerMarketValue * ((minutesCondition ? 1 : playerMinutes)/denominator);
            } catch (FifaException e) {
                if (!minutesCondition) weightedAverage += playerMarketValue*(averageMinutes/denominator);
            }
        }

        return (int) weightedAverage;
    }
    
    
    /**
     * Returns the Market Value using default values 
     * @return
     * @throws FifaException, if all minutes are zero
     */
    //If a player's market value or minutes played are unknown, default values ​​are used.
    public int defaultMarketValue(int defaultMarketValue, int defaultMinutes) throws FifaException {
        int minutesPlayed = 0;
        for (Player p : this.players) {
            try {
                minutesPlayed += p.minutes();
            } catch (FifaException e) {
                minutesPlayed += defaultMinutes;
            }
        }

        if (minutesPlayed == 0) throw new FifaException(FifaException.IMPOSSIBLE);

        double weightedAverage = 0;
        for (Player p : this.players) {
            int playerMarketValue;
            int playerMinutes;
            try {
                playerMarketValue = p.marketValue();
            } catch (FifaException e) {
                playerMarketValue = defaultMarketValue;
            }
            try {
                playerMinutes = p.minutes();
            } catch (FifaException e) {
                playerMinutes = defaultMinutes;
            }
            weightedAverage += playerMarketValue*(playerMinutes/((double) minutesPlayed)) ;
        }

        return (int) (weightedAverage);
    }
    
    
    @Override
    public String data() throws FifaException {
        StringBuffer answer=new StringBuffer();
        answer.append(name+".\t Grupo: "+position+".\t Valor Promedio:" +marketValue());
        for(Player p: players) {
            answer.append("\n\t"+p.data());
        }
        return answer.toString();
    } 
    

}
