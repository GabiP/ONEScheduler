/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.resources;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;

/**
 *
 * @author Andras Urge
 */
public class VmXml {
    @JacksonXmlProperty(localName = "ID")
    private Integer vmId;
     
    @JacksonXmlProperty(localName = "UID")
    private Integer uid;
    
    @JacksonXmlProperty(localName = "GID")
    private Integer gid;    
    
    @JacksonXmlProperty(localName = "NAME")
    private String name;
        
    @JacksonXmlProperty(localName = "STATE")
    private Integer state;
    
    @JacksonXmlProperty(localName = "LCM_STATE")
    private Integer lcm_state;
        
    @JacksonXmlProperty(localName = "RESCHED")
    private Integer resched;
        
    @JacksonXmlProperty(localName = "DEPLOY_ID")
    private String deploy_id;
    
    // TODO : properties are not at this level
    @JacksonXmlProperty(localName = "")
    private Float cpu;
    
    @JacksonXmlProperty(localName = "")
    private Integer memory;
    
    @JacksonXmlProperty(localName = "")
    private Integer datastore_id;
    
    @JacksonXmlProperty(localName = "")
    private List<DiskNodeXml> disks;    
    
    @JacksonXmlProperty(localName = "")
    private List<HistoryNodeXml> histories;
    
    @JacksonXmlProperty(localName = "")
    private List<NicNodeXml> nics;        
    
    @JacksonXmlProperty(localName = "")
    private Integer templateId;
    
    @JacksonXmlProperty(localName = "")
    private String schedRank;
    
    @JacksonXmlProperty(localName = "")
    private String schedDsRank;
    
    @JacksonXmlProperty(localName = "")
    private String schedRequirements;
    
    @JacksonXmlProperty(localName = "")
    private String schedDsRequirements;
    
    @JacksonXmlProperty(localName = "")
    private List<PciNodeXml> pcis;

    public Integer getVmId() {
        return vmId;
    }

    public void setVmId(Integer vmId) {
        this.vmId = vmId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getLcm_state() {
        return lcm_state;
    }

    public void setLcm_state(Integer lcm_state) {
        this.lcm_state = lcm_state;
    }

    public Integer getResched() {
        return resched;
    }

    public void setResched(Integer resched) {
        this.resched = resched;
    }

    public String getDeploy_id() {
        return deploy_id;
    }

    public void setDeploy_id(String deploy_id) {
        this.deploy_id = deploy_id;
    }

    public Float getCpu() {
        return cpu;
    }

    public void setCpu(Float cpu) {
        this.cpu = cpu;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public Integer getDatastore_id() {
        return datastore_id;
    }

    public void setDatastore_id(Integer datastore_id) {
        this.datastore_id = datastore_id;
    }

    public List<DiskNodeXml> getDisks() {
        return disks;
    }

    public void setDisks(List<DiskNodeXml> disks) {
        this.disks = disks;
    }

    public List<HistoryNodeXml> getHistories() {
        return histories;
    }

    public void setHistories(List<HistoryNodeXml> histories) {
        this.histories = histories;
    }

    public List<NicNodeXml> getNics() {
        return nics;
    }

    public void setNics(List<NicNodeXml> nics) {
        this.nics = nics;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getSchedRank() {
        return schedRank;
    }

    public void setSchedRank(String schedRank) {
        this.schedRank = schedRank;
    }

    public String getSchedDsRank() {
        return schedDsRank;
    }

    public void setSchedDsRank(String schedDsRank) {
        this.schedDsRank = schedDsRank;
    }

    public String getSchedRequirements() {
        return schedRequirements;
    }

    public void setSchedRequirements(String schedRequirements) {
        this.schedRequirements = schedRequirements;
    }

    public String getSchedDsRequirements() {
        return schedDsRequirements;
    }

    public void setSchedDsRequirements(String schedDsRequirements) {
        this.schedDsRequirements = schedDsRequirements;
    }

    public List<PciNodeXml> getPcis() {
        return pcis;
    }

    public void setPcis(List<PciNodeXml> pcis) {
        this.pcis = pcis;
    }
}
