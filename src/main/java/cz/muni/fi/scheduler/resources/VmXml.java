/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.resources;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opennebula.client.vm.VirtualMachine;

/**
 * This class represents an OpenNebula Vm
 * 
 * @author Gabriela Podolnikova
 */
public class VmXml {
    
    private int vmId;
     
    private Integer uid;
    
    private Integer gid;
    
    private String uname;
    
    private String gname;
    
    private String name;
    
    private Integer last_poll;
    
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
    
    private Integer prev_state;
    
    private Integer prev_lcm_state;
    
    private Integer resched;
    
    private Integer stime;
    
    private Integer etime;
    
    private String deploy_id;
    
    private Float cpu;
    
    private Integer memory;
    
    private Integer datastore_id;
    
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
    
    private List<PciNodeVm> pcis;
    
    private final VirtualMachine vm;

    public VmXml(VirtualMachine vm) {
        this.vm = vm;
        vm.info();
        this.init();
    }

    private void init() {
        vmId = Integer.parseInt(getVm().xpath("/VM/ID"));
        uid  = Integer.parseInt(getVm().xpath("/VM/UID"));
        gid  = Integer.parseInt(getVm().xpath("/VM/GID"));
        name = getVm().xpath("/VM/NAME");
        state  = Integer.parseInt(getVm().xpath("/VM/STATE"));
        lcm_state  = Integer.parseInt(getVm().xpath("/VM/LCM_STATE"));
        resched  = Integer.parseInt(getVm().xpath("/VM/RESCHED"));
        deploy_id = getVm().xpath("/VM/DEPLOY_ID");
        setCpu(Float.parseFloat(getVm().xpath("/VM/TEMPLATE/CPU")));
        setMemory(Integer.parseInt(getVm().xpath("/VM/TEMPLATE/MEMORY")));
        try {
            datastore_id = Integer.parseInt(getVm().xpath("/VM/TEMPLATE/DISK/DATASTORE_ID"));
        } catch (Exception e) {
            datastore_id = null;
        }
        try {
            disks = NodeElementLoader.getNodeElements(vm, DiskNode.class);
            histories = NodeElementLoader.getNodeElements(vm, HistoryNode.class);
        } catch (InstantiationException | IllegalAccessException ex) {
            // TODO: react on failure if needed
            Logger.getLogger(VmXml.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            nics = NodeElementLoader.getNodeElements(vm, NicNode.class);
        } catch (InstantiationException | IllegalAccessException ex) {
            // TODO: react on failure if needed
            Logger.getLogger(VmXml.class.getName()).log(Level.SEVERE, null, ex);
        }
        schedRank = getVm().xpath("/VM/USER_TEMPLATE/SCHED_RANK");
        schedDsRank = getVm().xpath("/VM/USER_TEMPLATE/SCHED_DS_RANK");
        schedRequirements = getVm().xpath("/VM/USER_TEMPLATE/SCHED_REQUIREMENTS");
        schedDsRequirements = getVm().xpath("/VM/USER_TEMPLATE/SCHED_DS_REQUIREMENTS");
        templateId = Integer.parseInt(getVm().xpath("/VM/TEMPLATE/TEMPLATE_ID"));
        try {
            pcis = NodeElementLoader.getNodeElements(vm, PciNodeVm.class);
        } catch (InstantiationException | IllegalAccessException ex) {
            // TODO: react on failure if needed
            Logger.getLogger(VmXml.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    public boolean evaluateSchedReqs(HostXml host) {
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
     * @return the uname
     */
    public String getUname() {
        return uname;
    }

    /**
     * @param uname the uname to set
     */
    public void setUname(String uname) {
        this.uname = uname;
    }

    /**
     * @return the gname
     */
    public String getGname() {
        return gname;
    }

    /**
     * @param gname the gname to set
     */
    public void setGname(String gname) {
        this.gname = gname;
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
     * @return the last_poll
     */
    public Integer getLast_poll() {
        return last_poll;
    }

    /**
     * @param last_poll the last_poll to set
     */
    public void setLast_poll(Integer last_poll) {
        this.last_poll = last_poll;
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
     * @return the prev_state
     */
    public Integer getPrev_state() {
        return prev_state;
    }

    /**
     * @param prev_state the prev_state to set
     */
    public void setPrev_state(Integer prev_state) {
        this.prev_state = prev_state;
    }

    /**
     * @return the prev_lcm_state
     */
    public Integer getPrev_lcm_state() {
        return prev_lcm_state;
    }

    /**
     * @param prev_lcm_state the prev_lcm_state to set
     */
    public void setPrev_lcm_state(Integer prev_lcm_state) {
        this.prev_lcm_state = prev_lcm_state;
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
     * @return the stime
     */
    public Integer getStime() {
        return stime;
    }

    /**
     * @param stime the stime to set
     */
    public void setStime(Integer stime) {
        this.stime = stime;
    }

    /**
     * @return the etime
     */
    public Integer getEtime() {
        return etime;
    }

    /**
     * @param etime the etime to set
     */
    public void setEtime(Integer etime) {
        this.etime = etime;
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
     * @param id the id to set
     */
    public void setVmId(int id) {
        this.vmId = id;
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
     * @return the id
     */
    public int getVmId() {
        return vmId;
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
     * @return the vm
     */
    public VirtualMachine getVm() {
        return vm;
    }

    /**
     * @return the datastore_id
     */
    public Integer getDatastore_id() {
        return datastore_id;
    }

    /**
     * @param datastore_id the datastore_id to set
     */
    public void setDatastore_id(Integer datastore_id) {
        this.datastore_id = datastore_id;
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
    public List<PciNodeVm> getPcis() {
        return pcis;
    }

    

}
