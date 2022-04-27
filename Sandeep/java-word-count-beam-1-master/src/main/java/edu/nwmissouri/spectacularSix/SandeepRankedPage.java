package edu.nwmissouri.spectacularSix;

import java.io.Serializable;
import java.util.ArrayList;

public class SandeepRankedPage implements Serializable{
    String name = "Unknown.md";
    Double rank = 1.0;
    ArrayList<SandeepVotingPage> voters = new ArrayList<SandeepVotingPage>();
    public SandeepRankedPage(String name, ArrayList<SandeepVotingPage> voters) {
        this.name = name;
        this.voters = voters;
    }
    
    public SandeepRankedPage(String name, Double rank, ArrayList<SandeepVotingPage> voters) {
        this.name = name;
        this.rank = rank;
        this.voters = voters;
    }

    public Double getRank() {
        return rank;
    }
    public void setRank(Double rank) {
        this.rank = rank;
    }
    public ArrayList<SandeepVotingPage> getVoters() {
        return voters;
    }
    public void setVoters(ArrayList<SandeepVotingPage> voters) {
        this.voters = voters;
    }

    @Override
    public String toString() {
        return String.format("%s, %.5f,%d", this.name, this.rank, this.voters.toString());    }
    

   
    
}