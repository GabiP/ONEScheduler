package cz.muni.fi.scheduler.core;

import cz.muni.fi.scheduler.elements.DatastoreElement;

/**
 * This class represents the Datastore and its rank.
 * The rank is an integer which value depends on the chosen storage policy.
 * 
 * @author Gabriela Podolnikova
 */
public class RankPair {
    
    private DatastoreElement ds;
    
    private Integer rank;

    public RankPair(DatastoreElement ds, Integer rank) {
        this.ds = ds;
        this.rank = rank;
    }

    public DatastoreElement getDs() {
        return ds;
    }

    public void setDs(DatastoreElement ds) {
        this.ds = ds;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
    
    
}
