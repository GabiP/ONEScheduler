package cz.muni.fi.scheduler.core;

import cz.muni.fi.scheduler.elements.DatastoreElement;
import cz.muni.fi.scheduler.elements.HostElement;
import cz.muni.fi.scheduler.elements.VmElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents matched resources for a given list of VMs.
 * 
 * @author Gabriela Podolnikova
 */
public class Match {

    private HostElement host;
    
    private DatastoreElement datastore;
    
    private List<VmElement> vms;

    public Match(HostElement host, DatastoreElement datastore) {
        this.host = host;
        this.datastore = datastore;
    }
    
    public Match() {
        
    }
    
    public HostElement getHost() {
        return host;
    }

    public void setHost(HostElement host) {
        this.host = host;
    }

    public DatastoreElement getDatastore() {
        return datastore;
    }

    public void setDatastore(DatastoreElement datastore) {
        this.datastore = datastore;
    }

    public List<VmElement> getVms() {
        return vms;
    }

    public void setVms(List<VmElement> vms) {
        this.vms = vms;
    }

    @Override
    public String toString() {
        return "Match{" + "host=" + host + ", datastore=" + datastore + ", vms=" + vms + '}';
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.getHost());
        hash = 47 * hash + Objects.hashCode(this.getDatastore());
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Match)) {
            return false;
        }
        final Match other = (Match) obj;
        if (!Objects.equals(this.getHost(), other.getHost())) {
            return false;
        }
        return Objects.equals(this.getDatastore(), other.getDatastore());
    }
    
    public List<Match> addVm(List<Match> plan, VmElement vm) {
        if (plan.contains(this)) {
            Match m = plan.get(plan.indexOf(this));
            m.getVms().add(vm);
        } else {
            List<VmElement> vmToAdd = new ArrayList<>();
            vmToAdd.add(vm);
            this.setVms(vmToAdd);
            plan.add(this);
        }
        return plan;
    }
}
