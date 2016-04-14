/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.resources;

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
    
    public DatastoreXml() {
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

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the uid
     */
    public Integer getUid() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(Integer uid) {
        this.uid = uid;
    }

    /**
     * @return the gid
     */
    public Integer getGid() {
        return gid;
    }

    /**
     * @param gid the gid to set
     */
    public void setGid(Integer gid) {
        this.gid = gid;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the owner_u
     */
    public Integer getOwner_u() {
        return owner_u;
    }

    /**
     * @param owner_u the owner_u to set
     */
    public void setOwner_u(Integer owner_u) {
        this.owner_u = owner_u;
    }

    /**
     * @return the owner_m
     */
    public Integer getOwner_m() {
        return owner_m;
    }

    /**
     * @param owner_m the owner_m to set
     */
    public void setOwner_m(Integer owner_m) {
        this.owner_m = owner_m;
    }

    /**
     * @return the owner_a
     */
    public Integer getOwner_a() {
        return owner_a;
    }

    /**
     * @param owner_a the owner_a to set
     */
    public void setOwner_a(Integer owner_a) {
        this.owner_a = owner_a;
    }

    /**
     * @return the group_u
     */
    public Integer getGroup_u() {
        return group_u;
    }

    /**
     * @param group_u the group_u to set
     */
    public void setGroup_u(Integer group_u) {
        this.group_u = group_u;
    }

    /**
     * @return the group_m
     */
    public Integer getGroup_m() {
        return group_m;
    }

    /**
     * @param group_m the group_m to set
     */
    public void setGroup_m(Integer group_m) {
        this.group_m = group_m;
    }

    /**
     * @return the group_a
     */
    public Integer getGroup_a() {
        return group_a;
    }

    /**
     * @param group_a the group_a to set
     */
    public void setGroup_a(Integer group_a) {
        this.group_a = group_a;
    }

    /**
     * @return the other_u
     */
    public Integer getOther_u() {
        return other_u;
    }

    /**
     * @param other_u the other_u to set
     */
    public void setOther_u(Integer other_u) {
        this.other_u = other_u;
    }

    /**
     * @return the other_m
     */
    public Integer getOther_m() {
        return other_m;
    }

    /**
     * @param other_m the other_m to set
     */
    public void setOther_m(Integer other_m) {
        this.other_m = other_m;
    }

    /**
     * @return the other_a
     */
    public Integer getOther_a() {
        return other_a;
    }

    /**
     * @param other_a the other_a to set
     */
    public void setOther_a(Integer other_a) {
        this.other_a = other_a;
    }

    /**
     * @return the cluster_id
     */
    public Integer getCluster_id() {
        return cluster_id;
    }

    /**
     * @param cluster_id the cluster_id to set
     */
    public void setCluster_id(Integer cluster_id) {
        this.cluster_id = cluster_id;
    }

    /**
     * @return the total_mb
     */
    public Integer getTotal_mb() {
        return total_mb;
    }

    /**
     * @param total_mb the total_mb to set
     */
    public void setTotal_mb(Integer total_mb) {
        this.total_mb = total_mb;
    }

    /**
     * @return the free_mb
     */
    public Integer getFree_mb() {
        return free_mb;
    }

    /**
     * @param free_mb the free_mb to set
     */
    public void setFree_mb(Integer free_mb) {
        this.free_mb = free_mb;
    }

    /**
     * @return the used_mb
     */
    public Integer getUsed_mb() {
        return used_mb;
    }

    /**
     * @param used_mb the used_mb to set
     */
    public void setUsed_mb(Integer used_mb) {
        this.used_mb = used_mb;
    }

    /**
     * @return the ds
     */
    public Datastore getDs() {
        return ds;
    }

    /**
     * @param ds the ds to set
     */
    public void setDs(Datastore ds) {
        this.ds = ds;
    }
}
