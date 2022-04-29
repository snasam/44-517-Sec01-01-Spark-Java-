package edu.nwmissouri.SpectacularSix;

import java.io.Serializable;
import java.util.ArrayList;

public class VarshithRankedPage implements Serializable {
    String name = "unknown.md";
    Double rank = 1.000;
    ArrayList<VarshithVotingPage> voters = new ArrayList<VarshithVotingPage>();
    

    VarshithRankedPage(String nameIn, ArrayList<VarshithVotingPage> votersIn){
        this.name = nameIn;
        this.voters = votersIn;
    }

    VarshithRankedPage(String nameIn,Double rankIn, ArrayList<VarshithVotingPage> votersIn){
        this.name = nameIn;
        this.voters = votersIn;
        this.rank = rankIn;

    }

    public Double getRank() {
        return rank;
    }



    public ArrayList<VarshithVotingPage> getVoters() {
        return voters;
    }

    @Override
    public String toString() {
      
        return String.format("%s,%.5f,%s", this.name,this.rank,this.voters.toString());
    }


    
}
