package cz.muni.fi.pools;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * This class represents an OpenNebula Host
 *
 * @author Gabriela Podolnikova
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Host {

    @JacksonXmlProperty(localName = "ID")
    private int id;

    @JacksonXmlProperty(localName = "NAME")
    private String name;

    @JacksonXmlProperty(localName = "STATE")
    private int state;

    @JacksonXmlProperty(localName = "IM_MAD")
    private String imMad;

    @JacksonXmlProperty(localName = "VM_MAD")
    private String vmMad;

    @JacksonXmlProperty(localName = "VN_MAD")
    private String vnMad;

    @JacksonXmlProperty(localName = "LAST_MON_TIME")
    private int lastMonTime;

    @JacksonXmlProperty(localName = "CLUSTER_ID")
    private int clusterId;

    @JacksonXmlProperty(localName = "CLUSTER")
    private String cluster;
    
    @JacksonXmlProperty(localName = "HOST_SHARE")
    private HostShare hostShare;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getImMad() {
        return imMad;
    }

    public void setImMad(String imMad) {
        this.imMad = imMad;
    }

    public String getVmMad() {
        return vmMad;
    }

    public void setVmMad(String vmMad) {
        this.vmMad = vmMad;
    }

    public String getVnMad() {
        return vnMad;
    }

    public void setVnMad(String vnMad) {
        this.vnMad = vnMad;
    }

    public int getLastMonTime() {
        return lastMonTime;
    }

    public void setLastMonTime(int lastMonTime) {
        this.lastMonTime = lastMonTime;
    }

    public int getClusterId() {
        return clusterId;
    }

    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    @Override
    public String toString() {
        return "Host{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", state=" + state +
                ", imMad='" + imMad + '\'' +
                ", vmMad='" + vmMad + '\'' +
                ", vnMad='" + vnMad + '\'' +
                ", lastMonTime=" + lastMonTime +
                ", clusterId=" + clusterId +
                ", cluster='" + cluster + '\'' +
                '}';
    }

    
    /**
     * Tests whether a VM can be hosted by the host.
     * 
     * @param vm virtual machine to be tested
     * @return true if VM can be hosted, false otherwise
     */
    public boolean testCapacity(Vm vm) {
        if (((hostShare.getMax_cpu() - hostShare.getCpu_usage()) >= vm.getCpu()) && ((hostShare.getMax_mem() - hostShare.getUsed_mem())>= vm.getMemory()) ){
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Increases counter of running virtual machines on current host.
     */
    public void addCapacity() {

    }
    
    public void delCapacity() {

    }

    /**
     * @return the hostShare
     */
    public HostShare getHostShare() {
        return hostShare;
    }

    /**
     * @param hostShare the hostShare to set
     */
    public void setHostShare(HostShare hostShare) {
        this.hostShare = hostShare;
    }
}
