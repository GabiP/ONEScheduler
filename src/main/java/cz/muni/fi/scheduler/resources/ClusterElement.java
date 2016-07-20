package cz.muni.fi.scheduler.resources;

import java.util.List;
import java.util.Objects;

/**
 * This class represents a Cluster.
 * A Cluster is a group of Hosts. Clusters can have associated Datastores and Virtual Networks.
 * @author Gabriela Podolnikova
 */
public class ClusterElement {

    private Integer id;
    
    private String name;    
    
    private List<Integer> hosts;
    
    private List<Integer> datastores;    
        
    @Override
    public String toString() {
        return "Cluster{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
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
     * @return the hosts
     */
    public List<Integer> getHosts() {
        return hosts;
    }

    public void setHosts(List<Integer> hosts) {
        this.hosts = hosts;
    }
    
    /**
     * @return the datastores
     */
    public List<Integer> getDatastores() {
        return datastores;
    }    
    
    public void setDatastores(List<Integer> datastores) {
        this.datastores = datastores;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.getId());
        hash = 47 * hash + Objects.hashCode(this.getName());
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof DatastoreElement)) {
            return false;
        }
        final DatastoreElement other = (DatastoreElement) obj;
        if (!Objects.equals(this.getId(), other.getId())) {
            return false;
        }
        if (!Objects.equals(this.getName(), other.getName())) {
            return false;
        }
        return true;
    }
}
