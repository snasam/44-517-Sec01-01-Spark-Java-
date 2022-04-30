package edu.nwmissouri.SpectacularSix;

import java.io.Serializable;
import java.util.ArrayList;

public class SharadaRankedPage implements Serializable {
    String name = "unknown.md";
    Double rank = 1.000;
    ArrayList<SharadaVotingPage> voters = new ArrayList<SharadaVotingPage>();
    

    SharadaRankedPage(String nameIn, ArrayList<SharadaVotingPage> votersIn){
        this.name = nameIn;
        this.voters = votersIn;
    }

    SharadaRankedPage(String nameIn,Double rankIn, ArrayList<SharadaVotingPage> votersIn){
        this.name = nameIn;
        this.voters = votersIn;
        this.rank = rankIn;

    }

    public Double getRank() {
        return rank;
    }



    public ArrayList<SharadaVotingPage> getVoters() {
        return voters;
    }

    @Override
    public String toString() {
      
        return String.format("%s,%.5f,%s", this.name,this.rank,this.voters.toString());
    }
    
}