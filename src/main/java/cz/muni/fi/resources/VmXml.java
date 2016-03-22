/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.resources;

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
    
    private Float memory;
    
    private Integer datastore_id;
    
    private Integer disk_id;
    
    private Integer network_id;
    
    private String schedRank;
    
    private String schedDsRank;
    
    private String schedRequirements;
    
    private String schedDsRequirements;
    
    private final VirtualMachine vm;

    public VmXml(VirtualMachine vm) {
        this.vm = vm;
        vm.info();
        System.out.println("constructor");
        System.out.println(vm.xpath("/VM/ID"));
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
        setMemory(Float.parseFloat(getVm().xpath("/VM/TEMPLATE/MEMORY")));
        if (state != 1) {
            datastore_id = Integer.parseInt(getVm().xpath("/VM/TEMPLATE/DISK/DATASTORE_ID"));
            disk_id = Integer.parseInt(getVm().xpath("/VM/TEMPLATE/DISK/DISK_ID"));
            network_id = Integer.parseInt(getVm().xpath("/VM/TEMPLATE/NIC/NETWORK_ID"));
        }
        schedRank = getVm().xpath("/VM/USER_TEMPLATE/SCHED_RANK");
        schedDsRank = getVm().xpath("/VM/USER_TEMPLATE/SCHED_DS_RANK");
        schedRequirements = getVm().xpath("/VM/USER_TEMPLATE/SCHED_REQUIREMENTS");
        schedDsRequirements = getVm().xpath("/VM/USER_TEMPLATE/SCHED_DS_REQUIREMENTS");
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
     * @return the user_template
     */
    /*public String getUser_template() {
        return user_template;
    }

    /**
     * @param user_template the user_template to set
     */
    /*public void setUser_template(String user_template) {
        this.user_template = user_template;
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
    public Float getMemory() {
        return memory;
    }

    /**
     * @param memory the memory to set
     */
    public void setMemory(Float memory) {
        this.memory = memory;
    }

    /**
     * @return the vm
     */
    public VirtualMachine getVm() {
        return vm;
    }

    

}
