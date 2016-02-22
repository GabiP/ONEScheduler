/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pools;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.opennebula.client.Client;
import org.opennebula.client.vm.VirtualMachine;

/**
 * This class represents an OpenNebula Vm
 * 
 * @author Gabriela Podolnikova
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Vm {
    
    @JacksonXmlProperty(localName = "ID")
    private int vmId;
     
    @JacksonXmlProperty(localName = "UID")
    private Integer uid;
    
    @JacksonXmlProperty(localName = "GID")
    private Integer gid;
    
    @JacksonXmlProperty(localName = "UNAME")
    private String uname;
    
    @JacksonXmlProperty(localName = "GNAME")
    private String gname;
    
    @JacksonXmlProperty(localName = "NAME")
    private String name;
    
    @JacksonXmlProperty(localName = "LAST_POLL")
    private Integer last_poll;
    
    /**
    * STATE values,
        see http://opennebula.org/_media/documentation:rel3.6:states-complete.png
    */
    @JacksonXmlProperty(localName = "STATE")
    private Integer state;
    
    /**
    * LCM_STATE this sub-state is relevant only when STATE is ACTIVE (4)
    * for values see vm.xsd
    */
    @JacksonXmlProperty(localName = "LCM_STATE")
    private Integer lcm_state;
    
    @JacksonXmlProperty(localName = "PREV_STATE")
    private Integer prev_state;
    
    @JacksonXmlProperty(localName = "PREV_LCM_STATE")
    private Integer prev_lcm_state;
    
    @JacksonXmlProperty(localName = "RESCHED")
    private Integer resched;
    
    @JacksonXmlProperty(localName = "STIME")
    private Integer stime;
    
    @JacksonXmlProperty(localName = "ETIME")
    private Integer etime;
    
    @JacksonXmlProperty(localName = "DEPLOY_ID")
    private String deploy_id;
    
    /**
      *MEMORY consumption in kilobytes
    */
    @JacksonXmlProperty(localName = "MEMORY")
    private Integer memory;
    
    /**
     * Percentage of 1 CPU consumed (two fully consumed cpu is 200)
    */
    @JacksonXmlProperty(localName = "CPU")
    private Integer cpu;
    
    /***
     * Sent bytes to the network
     */
    @JacksonXmlProperty(localName = "NET_TX")
    private Integer net_tx;
    
    
    /***
     * Received bytes from the network
     */
    @JacksonXmlProperty(localName = "NET_RX")
    private Integer net_rx;

    /*@JacksonXmlProperty(localName = "TEMPLATE")
    private Object template;
    
    @JacksonXmlProperty(localName = "USER_TEMPLATE")
    private String user_template;*/

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
     * @return the cpu
     */
    public Integer getCpu() {
        return cpu;
    }

    /**
     * @param cpu the cpu to set
     */
    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    /**
     * @return the net_tx
     */
    public Integer getNet_tx() {
        return net_tx;
    }

    /**
     * @param net_tx the net_tx to set
     */
    public void setNet_tx(Integer net_tx) {
        this.net_tx = net_tx;
    }

    /**
     * @return the net_rx
     */
    public Integer getNet_rx() {
        return net_rx;
    }

    /**
     * @param net_rx the net_rx to set
     */
    public void setNet_rx(Integer net_rx) {
        this.net_rx = net_rx;
    }

    /**
     * @return the template
     */
    /*public Object getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    /*public void setTemplate(Object template) {
        this.template = template;
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
                ", memory='" + memory + '\'' +
                ", cpu='" + cpu + '\'' +
 //               ", template=" + template + '\'' +
                '}';
    }

    /**
     * @return the id
     */
    public int getVmId() {
        return vmId;
    }

}
