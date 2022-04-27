package edu.nwmissouri.spectacularSix;

import java.io.Serializable;

public class SandeepVotingPage implements Serializable {


    String name = "Unknown.md";
    Double rank = 1.0;
    Integer voters = 0; 
    

    public SandeepVotingPage(String name, Integer voters) {
        this.name = name;
        this.voters = voters;
    }


    public SandeepVotingPage(String name, Double rank, Integer voters) {
        this.name = name;
        this.rank = rank;
        this.voters = voters;
    }


    public String getName() {
        return name;
    }


   


    public Double getRank() {
        return rank;
    }



    public Integer getVoters() {
        return voters;
    }


    @Override
    public String toString() {
        return String.format("%s, %.5f,%d", this.name, this.rank, this.voters);   
     }

    


    
}