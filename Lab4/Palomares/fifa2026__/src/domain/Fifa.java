package domain;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Fifa
 * @author DOPO
 * @version ECI 2026
 */


public class Fifa{
    private ArrayList<Participant> participants;
    private TreeMap<String,Player> players;

    /**
     * Create a Fifa
     */
    public Fifa(){
        participants = new ArrayList<Participant>();
        players = new TreeMap<String, Player>();
        // addSome();
    }

    // ----------------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------

    private void addSome(){
        String[][] players= {
            {"A.DIAZ", "690", "A", "760000000", "Bayer"},
            {"JAMES", "516", "M", "2200000", "Minnesota"},
            {"BORRE", "445", "A", "4400000", "Sport Club"},
            {"LUCUMI", "1250", "D", "125000000", "Bologna"},
            {"VARGAS", "1160", "P", "540000", "Atlas"}
        };
        for (String[] p : players){
            try {addPlayer(p[0], p[1], p[2], p[3], p[4]);}
            catch (FifaException e) {}
        }
        
        /* String[][] teams = {{"COLOMBIA", "1620", "K", "Lorenzo", "Amarill-Rojo-Azul", "L.DIAZ\nJAMES\nBORRE\nLUCUMI\nVARGAS"}};
        for (String[] t : teams){
            addTeam(t[0], t[1], t[2], t[3], t[4], t[5]);
        } */
    }

    /**
     * Consult the number of participants
     * @return 
     */
    public int numberParticipants(){
        return participants.size();
    }

    /**
     * Return the data of all participants
     * @return  
     */    
    public String toString(){
        return data(participants);
    }

    // ----------------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------

    /**
     * Add a new player
    */
    public void addPlayer(String name, String minutes, String position, String value, String club) throws FifaException {
        if (this.consult(name) != null) throw new FifaException(FifaException.NAME_ALREADY_USED);
        if (!FifaUtils.isInteger(minutes) || !FifaUtils.isInteger(value)) throw new FifaArgumentException("Invalid value, the minutes and the value should represent integers.");

        String name_ = name.toUpperCase();
        int minutes_ = Integer.parseInt(minutes);
        char position_ = position.charAt(0);
        int value_ = Integer.parseInt(value);

        Player player = new Player(name, minutes_, position_, value_, club);
        participants.add(player);
        players.put(name_, player);
    }
    
    /**
     * Add a new team
    */
    public void addTeam(String name, String minutes, String position, String manager, String uniform, String playerNames) throws FifaException {
        if (this.consult(name) != null) throw new FifaException(FifaException.NAME_ALREADY_USED);
        if (!FifaUtils.isInteger(minutes)) throw new FifaArgumentException("Invalid value, the minutes and the value should represent integers");
        
        int minutes_ = Integer.parseInt(minutes);
        char position_ = position.charAt(0);

        Team team = new Team(name, minutes_, position_, manager, uniform);
        String [] playerNamesArray = playerNames.split("\n");
        for (String playerName : playerNamesArray) {
            try {
                team.addPlayer(players.get(playerName.toUpperCase()));
            } catch (FifaArgumentException e) {
                throw new FifaException(FifaException.INVALID_PLAYER_FOR_TEAM);
            }
        }
        participants.add(team);
    }

    // ----------------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------

    /**
     * Consult a participant
     */
    public Participant consult(String name){
        Participant participant = null;
        for(int i=0; i<participants.size() && participant==null; i++){
            Participant participant_ = participants.get(i);
            if (participant_.name().compareToIgnoreCase(name) == 0) {
                participant = participant_;
            }
        }
        return participant;
    }

    /**
     * Consults the participants that start with a prefix
     * @param prefix prefix
     * @return array list with the participants
     */
    public ArrayList<Participant> select(String prefix){
        ArrayList<Participant> answers = new ArrayList<>();
        prefix = prefix.toUpperCase();

        for(int i=0; i<=participants.size(); i++){
            Participant participant_ = participants.get(i);
            if(participant_.name().toUpperCase().startsWith(prefix)){
                answers.add(participant_);
            }   
        }
        return answers;
    }

    /**
     * Consult selected participants
     * @param selected array list of participants
     * @return string with the data of the selected participants
     */
    public String data(ArrayList<Participant> selected){
        StringBuffer answer=new StringBuffer();
        answer.append(participants.size()+ " elementos\n");
        for(Participant p : selected) {
            try{
                answer.append('>' + p.data());
                answer.append("\n");
            }catch(FifaException e) {
                answer.append("**** "+e.getMessage());
            }
        }
        return answer.toString();
    }
    
    /**
     * Return the data of participants with a prefix
     * @param prefix
     * @return  
     */ 
    public String search(String prefix){
        return data(select(prefix));
    }
}
