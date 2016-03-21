/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pools;

import org.opennebula.client.datastore.Datastore;

/**
 *
 * @author Gabriela Podolnikova
 */
public class DatastoreXml {
    
    private Integer id;  
    private Integer uid;  
    private Integer gid;
    
    private String name;
    
    private Integer owner_u;
    private Integer owner_m;
    private Integer owner_a;

    private Integer group_u;
    private Integer group_m;
    private Integer group_a;

    private Integer other_u;
    private Integer other_m;
    private Integer other_a;
    
    private Integer cluster_id;
    
    private Integer total_mb;
    private Integer free_mb;
    private Integer used_mb;
    
    private Datastore ds;
    
    //shared - yes or no, how to get it
    
    public DatastoreXml(Datastore ds) {
        this.ds = ds;
        ds.info();
        this.init();
    }
    
    public void init() {
        id = Integer.parseInt(ds.xpath("/DATASTORE/ID"));
        uid = Integer.parseInt(ds.xpath("/DATASTORE/UID"));
        gid = Integer.parseInt(ds.xpath("/DATASTORE/GID"));
        name = ds.xpath("/DATASTORE/NAME");
        owner_u = Integer.parseInt(ds.xpath("/DATASTORE/PERMISSIONS/OWNER_U"));
        owner_m = Integer.parseInt(ds.xpath("/DATASTORE/PERMISSIONS/OWNER_M"));
        owner_a = Integer.parseInt(ds.xpath("/DATASTORE/PERMISSIONS/OWNER_A"));
        group_u = Integer.parseInt(ds.xpath("/DATASTORE/PERMISSIONS/GROUP_U"));
        group_m = Integer.parseInt(ds.xpath("/DATASTORE/PERMISSIONS/GROUP_M"));
        group_a = Integer.parseInt(ds.xpath("/DATASTORE/PERMISSIONS/GROUP_A"));
        other_u = Integer.parseInt(ds.xpath("/DATASTORE/PERMISSIONS/OTHER_U"));
        other_m = Integer.parseInt(ds.xpath("/DATASTORE/PERMISSIONS/OTHER_M"));
        other_a = Integer.parseInt(ds.xpath("/DATASTORE/PERMISSIONS/OTHER_A"));
        cluster_id = Integer.parseInt(ds.xpath("/DATASTORE/CLUSTER_ID"));
        total_mb = Integer.parseInt(ds.xpath("/DATASTORE/TOTAL_MB"));
        free_mb = Integer.parseInt(ds.xpath("/DATASTORE/FREE_MB"));
        used_mb = Integer.parseInt(ds.xpath("/DATASTORE/USED_MB"));
    }
}
