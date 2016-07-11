/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.resources;

import cz.muni.fi.scheduler.resources.nodes.DiskNode;
import cz.muni.fi.scheduler.resources.nodes.NicNode;
import cz.muni.fi.scheduler.resources.nodes.PciNode;
import cz.muni.fi.scheduler.resources.nodes.HistoryNode;
import java.util.List;
import java.util.Objects;

/**
 * This class represents an OpenNebula Vm
 * 
 * @author Gabriela Podolnikova
 */
public class VmElement {
    
    private Integer vmId;
     
    private Integer uid;
    
    private Integer gid;    
    
    private String name;
        
    /**
    * STATE values,
        see http://opennebula.org/_media/documentation:rel3.6:states-complete.png
    */
    private Integer state;
    
    /**
    * LCM_STATE this sub-state is relevant only when STATE is ACTIVE (4)
    * for values see vm.xsd
    */
    private Integer lcm_state;
        
    private Integer resched;
        
    private String deploy_id;
    
    private Float cpu;
    
    private Integer memory;
    
    private List<DiskNode> disks;    
    
    private List<HistoryNode> histories;
    
    private List<NicNode> nics;        
    
    private Integer templateId;
    
    /**
     * Possible values:
     * RUNNING_VMS : packing
     * -RUNNING_VMS : stripping
     * FREE_CPU : load aware
     */
    private String schedRank;
    
    /**
     * Possible values:
     * -FREE_MB : packing
     * FREE_MB : striping
     */
    private String schedDsRank;
    
    private String schedRequirements;
    
    private String schedDsRequirements;
    
    private List<PciNode> pcis;
    
    
    public boolean evaluateSchedReqs(HostElement host) {
        if (schedRequirements == null) {
            return true;
        }
        String[] reqs = schedRequirements.split("\\|");
        boolean fits = false;
        if (schedRequirements.equals("")) {
            System.out.println("Vm does not have any requirements");
            return true;
        }
        for (String req: reqs) {
            req = req.trim();
            System.out.println("inside reqs: " + req);
            Integer id = Integer.parseInt(req.substring(req.indexOf("=")+2, req.length()-1));
            System.out.println("Evaluate sched reqs: " + id);
            if (req.contains("ID")) {
                if (host.getId() == id) {
                    fits = true;
                }
            }
            if (req.contains("CLUSTER")) {
                if (host.getClusterId() == id) {
                    fits = true;
                }
            }
        }
        return fits;
    }
        
    public int getRunTime() {        
        int runTime = 0;  
        for (int i=0; i<histories.size(); i++) {
            HistoryNode history = histories.get(i);
            boolean isActive = (state == 3);
            boolean isLast = (i == histories.size() - 1);
            if (history.isClosed() || (isActive && isLast)) {
                runTime += history.getRunTime();
            } else {     
                // There is a history record that should have been closed
                // TODO : calculate runtime
            }
        }
        return runTime;
    }    
    
    /**
     * @return the id
     */
    public Integer getVmId() {
        return vmId;
    }    
    
    /**
     * @param id the id to set
     */
    public void setVmId(Integer id) {
        this.vmId = id;
    }
    
    /**
     * @return the uid
     */
    public Integer getUid() {
        return uid;
    }

    /**
     * @return the gid
     */
    public Integer getGid() {
        return gid;
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
     * @return the state
     */
    public Integer getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * @return the lcm_state
     */
    public Integer getLcm_state() {
        return lcm_state;
    }

    /**
     * @param lcm_state the lcm_state to set
     */
    public void setLcm_state(Integer lcm_state) {
        this.lcm_state = lcm_state;
    }

    /**
     * @return the resched
     */
    public Integer getResched() {
        return resched;
    }

    /**
     * @param resched the resched to set
     */
    public void setResched(Integer resched) {
        this.resched = resched;
    }

    /**
     * @return the deploy_id
     */
    public String getDeploy_id() {
        return deploy_id;
    }

    /**
     * @param deploy_id the deploy_id to set
     */
    public void setDeploy_id(String deploy_id) {
        this.deploy_id = deploy_id;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(Integer uid) {
        this.uid = uid;
    }

    /**
     * @param gid the gid to set
     */
    public void setGid(Integer gid) {
        this.gid = gid;
    }
    
    @Override
    public String toString() {
        return "VM{" +
                "id='" + getVmId() + '\'' +
                ", name='" + name + '\'' +
                ", state=" + state +
                ", lcm_state='" + lcm_state + '\'' +
                ", cpu=" + getCpu() + '\'' +
                ", memory=" + getMemory() + '\'' +
                ", schedRank=" + schedRank + '\'' +
                ", schedDsRank=" + schedDsRank + '\'' +
                ", schedRequirements=" + schedRequirements + '\'' +
                ", schedDsRequirements=" + schedDsRequirements + '\'' +
                ", pcis=" + pcis + '\'' +
                '}';
    }


    /**
     * @return the cpu
     */
    public Float getCpu() {
        return cpu;
    }

    /**
     * @param cpu the cpu to set
     */
    public void setCpu(Float cpu) {
        this.cpu = cpu;
    }

    /**
     * @return the memory
     */
    public Integer getMemory() {
        return memory;
    }

    /**
     * @param memory the memory to set
     */
    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    /**
     * @return the network_id
     */
    public List<NicNode> getNetwork_ids() {
        return nics;
    }

    /**
     * @param nics the network_id to set
     */
    public void setNetwork_ids(List<NicNode> nics) {
        this.nics= nics;
    }

    /**
     * @return the schedRank
     */
    public String getSchedRank() {
        return schedRank;
    }

    /**
     * @param schedRank the schedRank to set
     */
    public void setSchedRank(String schedRank) {
        this.schedRank = schedRank;
    }

    /**
     * @return the schedDsRank
     */
    public String getSchedDsRank() {
        return schedDsRank;
    }

    /**
     * @param schedDsRank the schedDsRank to set
     */
    public void setSchedDsRank(String schedDsRank) {
        this.schedDsRank = schedDsRank;
    }

    /**
     * @return the schedRequirements
     */
    public String getSchedRequirements() {
        return schedRequirements;
    }

    /**
     * @param schedRequirements the schedRequirements to set
     */
    public void setSchedRequirements(String schedRequirements) {
        this.schedRequirements = schedRequirements;
    }

    /**
     * @return the schedDsRequirements
     */
    public String getSchedDsRequirements() {
        return schedDsRequirements;
    }

    /**
     * @param schedDsRequirements the schedDsRequirements to set
     */
    public void setSchedDsRequirements(String schedDsRequirements) {
        this.schedDsRequirements = schedDsRequirements;
    }

    /**
     * @return the templateId
     */
    public Integer getTemplateId() {
        return templateId;
    }

    /**
     * @param templateId the templateId to set
     */
    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public List<DiskNode> getDisks() {
        return disks;
    }

    public void setDisks(List<DiskNode> disks) {
        this.disks = disks;
    }

    public List<HistoryNode> getHistories() {
        return histories;
    }

    public void setHistories(List<HistoryNode> histories) {
        this.histories = histories;
    }

    /**
     * @return the pcis
     */
    public List<PciNode> getPcis() {
        return pcis;
    }
    
    public void setPcis(List<PciNode> pcis) {
        this.pcis = pcis;
    }

    public List<NicNode> getNics() {
        return nics;
    }

    public void setNics(List<NicNode> nics) {
        this.nics = nics;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.getVmId());
        hash = 47 * hash + Objects.hashCode(this.getName());
        hash = 47 * hash + Objects.hashCode(this.getState());
        hash = 47 * hash + Objects.hashCode(this.getCpu());
        hash = 47 * hash + Objects.hashCode(this.getMemory());
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof VmElement)) {
            return false;
        }
        final VmElement other = (VmElement) obj;
        if (!Objects.equals(this.getVmId(), other.getVmId())) {
            return false;
        }
        if (!Objects.equals(this.getName(), other.getName())) {
            return false;
        }
        if (!Objects.equals(this.getState(), other.getState())) {
            return false;
        }
        if (!Objects.equals(this.getCpu(), other.getCpu())) {
            return false;
        }
        if (!Objects.equals(this.getMemory(), other.getMemory())) {
            return false;
        }
        return true;
    }
}
