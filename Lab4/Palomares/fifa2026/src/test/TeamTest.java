package test;
import domain.*;


import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TeamTest{
    /* @Test
    public void shouldCalculateTheMarketValueOfATeam(){                              
        Team t = new Team("COLOMBIA",1620, 'K', "Lorenzo", "Amarill-Rojo-Azul");
        t.addPlayer(new Player("L.DIAZ", 690,'A',760_000_000,"Bayer"));
        t.addPlayer(new Player("JAMES", 516,'M',2_200_000,"Minnesota"));
        t.addPlayer(new Player("BORRE", 445,'A',4_400_000,"Sport Club"));
        t.addPlayer(new Player("LUCUMI", 1250,'D',125_000_000,"Bologna"));
        t.addPlayer(new Player("VARGAS", 1160,'P',540_000,"Atlas"));
        try {
           assertEquals(168522432,t.marketValue());
        } catch (FifaException e){
            fail("Threw a exception");
        }    
    }    

    
    @Test
    public void shouldThrowExceptionIfTeamHasNoPlayer(){
        Team t = new Team("COLOMBIA",1620, 'K', "Lorenzo", "Amarill-Rojo-Azul");

        
        try { 
           t.marketValue();
           fail("Did not throw exception");
        } catch (FifaException e) {
            assertEquals(FifaException.IMPOSSIBLE,e.getMessage());
        }    
    }    
    
    
    @Test
    public void shouldThrowExceptionIfPlayersMinutesSumZero(){
        Team t = new Team("COLOMBIA",1620, 'K', "Lorenzo", "Amarill-Rojo-Azul");
        t.addPlayer(new Player("L.DIAZ",0,'A',760_000_000,"Bayer"));
        t.addPlayer(new Player("JAMES", 0,'M',2_200_000,"Minnesota"));
        t.addPlayer(new Player("BORRE", 0,'A',4_400_000,"Sport Club"));
        t.addPlayer(new Player("LUCUMI", 0,'D',125_000_000,"Bologna"));
        t.addPlayer(new Player("VARGAS", 0,'P',540_000,"Atlas"));
        try { 
           t.marketValue();
           fail("Did not throw exception");
        } catch (FifaException e) {
            assertEquals(FifaException.IMPOSSIBLE,e.getMessage());
        }    
    } 
    
   @Test
    public void shouldThrowExceptionIfAMarketValueIsNotKnown(){
        Team t = new Team("COLOMBIA",1620, 'K', "Lorenzo", "Amarill-Rojo-Azul");
        t.addPlayer(new Player("L.DIAZ", 690,'A',760_000_000,"Bayer"));
        t.addPlayer(new Player("JAMES", 516,'M',null,"Minnesota"));
        t.addPlayer(new Player("BORRE", 445,'A',4_400_000,"Sport Club"));
        t.addPlayer(new Player("LUCUMI", 1250,'D',125_000_000,"Bologna"));
        t.addPlayer(new Player("VARGAS", 1160,'P',540_000,"Atlas"));
        try { 
           t.marketValue();
           fail("Did not throw exception");
        } catch (FifaException e) {
            assertEquals(FifaException.VALUE_UNKNOWN,e.getMessage());
        }    
    }     
    
   @Test
    public void shouldThrowExceptionIfAMinutesIsNotKnown(){
        Team t = new Team("COLOMBIA",1620, 'K', "Lorenzo", "Amarill-Rojo-Azul");
        t.addPlayer(new Player("L.DIAZ", 690,'A',760_000_000,"Bayer"));
        t.addPlayer(new Player("JAMES", 516,'M',2_200_000,"Minnesota"));
        t.addPlayer(new Player("BORRE", 445,'A',4_400_000,"Sport Club"));
        t.addPlayer(new Player("LUCUMI", 1250,'D',125_000_000,"Bologna"));
        t.addPlayer(new Player("VARGAS", null,'P',540_000,"Atlas"));
        try { 
           t.marketValue();
           fail("Did not throw exception");
        } catch (FifaException e) {
            assertEquals(FifaException.MINUTES_UNKNOWN,e.getMessage());
        }    
    } */

    // ----------------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------
    
    /* @Test
    public void shouldCalculateTheExpectedMarketValueOfATeam(){                              
        Team t = new Team("COLOMBIA",1620, 'K', "Lorenzo", "Amarill-Rojo-Azul");
        t.addPlayer(new Player("L.DIAZ", 690,'A',760_000_000,"Bayer"));
        t.addPlayer(new Player("JAMES", 516,'M',2_200_000,"Minnesota"));
        t.addPlayer(new Player("BORRE", 445,'A',4_400_000,"Sport Club"));
        t.addPlayer(new Player("LUCUMI", 1250,'D',125_000_000,"Bologna"));
        t.addPlayer(new Player("VARGAS", 1160,'P',540_000,"Atlas"));
        try {
           assertEquals(168522432,t.expectedMarketValue());
        } catch (FifaException e){
            fail("Threw a exception");
        }    
    }    

    
    @Test
    public void shouldThrowExceptionIfTeamHasNoPlayerWhenCalculatingTheExpectedMarketValue(){
        Team t = new Team("COLOMBIA",1620, 'K', "Lorenzo", "Amarill-Rojo-Azul");

        
        try { 
           t.expectedMarketValue();
           fail("Did not throw exception");
        } catch (FifaException e) {
            assertEquals(FifaException.IMPOSSIBLE,e.getMessage());
        }    
    }    
    
    
    @Test
    public void shouldThrowExceptionIfPlayersMinutesSumZeroWhenCalculatingTheExpectedMarketValue(){
        Team t = new Team("COLOMBIA",1620, 'K', "Lorenzo", "Amarill-Rojo-Azul");
        t.addPlayer(new Player("L.DIAZ",0,'A',760_000_000,"Bayer"));
        t.addPlayer(new Player("JAMES", 0,'M',2_200_000,"Minnesota"));
        t.addPlayer(new Player("BORRE", 0,'A',4_400_000,"Sport Club"));
        t.addPlayer(new Player("LUCUMI", 0,'D',125_000_000,"Bologna"));
        t.addPlayer(new Player("VARGAS", 0,'P',540_000,"Atlas"));
        try { 
           t.expectedMarketValue();
           fail("Did not throw exception");
        } catch (FifaException e) {
            assertEquals(FifaException.IMPOSSIBLE,e.getMessage());
        }    
    } 
    
   @Test
    public void shouldCalculateExpectedMarketValueIfLessThan50PercentOfPlayersHaveNoMinutes(){
        Team t = new Team("COLOMBIA",1620, 'K', "Lorenzo", "Amarill-Rojo-Azul");
        t.addPlayer(new Player("L.DIAZ", 690,'A',760_000_000,"Bayer"));
        t.addPlayer(new Player("JAMES", 516,'M',2_200_000,"Minnesota"));
        t.addPlayer(new Player("BORRE", null,'A',4_400_000,"Sport Club"));
        t.addPlayer(new Player("LUCUMI", 1250,'D',125_000_000,"Bologna"));
        t.addPlayer(new Player("VARGAS",  null,'P',540_000,"Atlas"));
        try {
           assertEquals(167_547_902,t.expectedMarketValue());
        } catch (FifaException e){
            fail("Threw a exception");
        }  
    }

    @Test
    public void shouldCalculateExpectedMarketValueIfMoreThan50PercentOfPlayersHaveNoMinutes(){
        Team t = new Team("COLOMBIA",1620, 'K', "Lorenzo", "Amarill-Rojo-Azul");
        t.addPlayer(new Player("L.DIAZ", null,'A',760_000_000,"Bayer"));
        t.addPlayer(new Player("JAMES", 516,'M',2_200_000,"Minnesota"));
        t.addPlayer(new Player("BORRE", null,'A',4_400_000,"Sport Club"));
        t.addPlayer(new Player("LUCUMI", 1250,'D',125_000_000,"Bologna"));
        t.addPlayer(new Player("VARGAS",  null,'P',540_000,"Atlas"));
        try {
           assertEquals(63_600_000,t.expectedMarketValue());
        } catch (FifaException e){
            fail("Threw a exception");
        }  
    }
    
   @Test
    public void shouldThrowExceptionIfAMinutesIsNotKnownWhenCalculatingTheExpectedMarketValue(){
        Team t = new Team("COLOMBIA",1620, 'K', "Lorenzo", "Amarill-Rojo-Azul");
        t.addPlayer(new Player("L.DIAZ", 690,'A',760_000_000,"Bayer"));
        t.addPlayer(new Player("JAMES", 516,'M',2_200_000,"Minnesota"));
        t.addPlayer(new Player("BORRE", 445,'A',4_400_000,"Sport Club"));
        t.addPlayer(new Player("LUCUMI", 1250,'D',125_000_000,"Bologna"));
        t.addPlayer(new Player("VARGAS", null,'P',540_000,"Atlas"));
        try { 
           t.marketValue();
           fail("Did not throw exception");
        } catch (FifaException e) {
            assertEquals(FifaException.MINUTES_UNKNOWN,e.getMessage());
        }    
    } */

    // ----------------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------
    
    @Test
    public void shouldCalculateTheDefaultMarketValueOfATeam(){                              
        Team t = new Team("COLOMBIA",1620, 'K', "Lorenzo", "Amarill-Rojo-Azul");
        t.addPlayer(new Player("L.DIAZ", 690,'A',760_000_000,"Bayer"));
        t.addPlayer(new Player("JAMES", 516,'M',2_200_000,"Minnesota"));
        t.addPlayer(new Player("BORRE", 445,'A',4_400_000,"Sport Club"));
        t.addPlayer(new Player("LUCUMI", 1250,'D',125_000_000,"Bologna"));
        t.addPlayer(new Player("VARGAS", 1160,'P',540_000,"Atlas"));
        try {
           assertEquals(168522432,t.defaultMarketValue(10_000_000, 810));
        } catch (FifaException e){
            fail("Threw a exception");
        }    
    }    

    
    @Test
    public void shouldThrowExceptionIfTeamHasNoPlayerWhenCalculatingTheDefaultMarketValue(){
        Team t = new Team("COLOMBIA",1620, 'K', "Lorenzo", "Amarill-Rojo-Azul");

        
        try { 
           t.defaultMarketValue(10_000_000, 810);
           fail("Did not throw exception");
        } catch (FifaException e) {
            assertEquals(FifaException.IMPOSSIBLE,e.getMessage());
        }    
    }    
    
    
    @Test
    public void shouldThrowExceptionIfPlayersMinutesSumZeroWhenCalculatingTheDefaultMarketValue(){
        Team t = new Team("COLOMBIA",1620, 'K', "Lorenzo", "Amarill-Rojo-Azul");
        t.addPlayer(new Player("L.DIAZ",0,'A',760_000_000,"Bayer"));
        t.addPlayer(new Player("JAMES", 0,'M',2_200_000,"Minnesota"));
        t.addPlayer(new Player("BORRE", 0,'A',4_400_000,"Sport Club"));
        t.addPlayer(new Player("LUCUMI", 0,'D',125_000_000,"Bologna"));
        t.addPlayer(new Player("VARGAS", 0,'P',540_000,"Atlas"));
        try { 
           t.defaultMarketValue(10_000_000, 810);
           fail("Did not throw exception");
        } catch (FifaException e) {
            assertEquals(FifaException.IMPOSSIBLE,e.getMessage());
        }    
    } 

    @Test
    public void shouldCalculateDefaultMarketValueIfMoreSomePlayersHaveNoMinutes(){
        Team t = new Team("COLOMBIA",1620, 'K', "Lorenzo", "Amarill-Rojo-Azul");
        t.addPlayer(new Player("L.DIAZ", null,'A',760_000_000,"Bayer"));
        t.addPlayer(new Player("JAMES", 516,'M',2_200_000,"Minnesota"));
        t.addPlayer(new Player("BORRE", null,'A',4_400_000,"Sport Club"));
        t.addPlayer(new Player("LUCUMI", 1250,'D',125_000_000,"Bologna"));
        t.addPlayer(new Player("VARGAS",  null,'P',540_000,"Atlas"));
        try {
           assertEquals(185_173_164,t.defaultMarketValue(10_000_000, 810));
        } catch (FifaException e){
            fail("Threw a exception");
        }  
    }
    
   @Test
    public void shouldCalculateDefaultMarketValueIfSomePlayersHaveNoMarketValue(){
        Team t = new Team("COLOMBIA",1620, 'K', "Lorenzo", "Amarill-Rojo-Azul");
        t.addPlayer(new Player("L.DIAZ", 690,'A',760_000_000,"Bayer"));
        t.addPlayer(new Player("JAMES", 516,'M',null,"Minnesota"));
        t.addPlayer(new Player("BORRE", 445,'A',4_400_000,"Sport Club"));
        t.addPlayer(new Player("LUCUMI", 1250,'D',null,"Bologna"));
        t.addPlayer(new Player("VARGAS",  1160,'P',null,"Atlas"));
        try {
           assertEquals(136_818_025,t.defaultMarketValue(10_000_000, 810));
        } catch (FifaException e){
            fail("Threw a exception");
        }  
    }
}