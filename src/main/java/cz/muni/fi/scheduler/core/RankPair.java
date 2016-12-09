/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.scheduler.core;

import cz.muni.fi.scheduler.elements.DatastoreElement;

/**
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
