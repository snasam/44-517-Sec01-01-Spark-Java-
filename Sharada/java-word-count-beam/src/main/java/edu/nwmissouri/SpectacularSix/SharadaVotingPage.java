package edu.nwmissouri.SpectacularSix;

import java.io.Serializable;

public class SharadaVotingPage implements Serializable{
    String name = "unknown.md";
    Double rank = 1.0;
    Integer votes = 0;
    public SharadaVotingPage(String name, Double rank, Integer votes) {
        this.name = name;
        this.rank = rank;
        this.votes = votes;
    }
    public SharadaVotingPage(String name, Integer votes) {
        this.name = name;
        this.votes = votes;
    }
    public String getName() {
        return name;
    }
    
    public Double getRank() {
        return rank;
    }
    
    public Integer getVotes() {
        return votes;
    }
    @Override
    public String toString() {
        return String.format("%s,%.5f,%s", this.name,this.rank,this.votes);
    }
    
    

    
}