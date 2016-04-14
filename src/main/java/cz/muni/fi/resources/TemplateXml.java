/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.resources;

import org.opennebula.client.template.Template;

/**
 *
 * @author Gabriela Podolnikova
 */
public class TemplateXml {
    
    private Integer id;
    
    private Float cpu;
    
    private Integer diskSize;
    
    private String diskType;
    
    private Integer memory;
    
    private Integer vcpu;
    
    private Template t;
    
    public TemplateXml(Template t) {
        this.t = t;
        t.info();
        this.init();
    }
    
    public void init() {
        id = Integer.parseInt(t.xpath("/VMTEMPLATE/ID"));
        cpu = Float.parseFloat(t.xpath("/VMTEMPLATE/TEMPLATE/CPU"));
        try {
            diskSize = Integer.parseInt(t.xpath("/VMTEMPLATE/TEMPLATE/DISK/SIZE"));
        } catch (NumberFormatException e) {
            diskSize = 0;
        }
        diskType = t.xpath("/VMTEMPLATE/TEMPLATE/DISK/TYPE");
        memory = Integer.parseInt(t.xpath("/VMTEMPLATE/TEMPLATE/MEMORY"));
        try {
            vcpu = Integer.parseInt(t.xpath("/VMTEMPLATE/TEMPLATE/VCPU"));
        } catch (NumberFormatException e) {
            vcpu = 0;
        }
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
     * @return the diskSize
     */
    public Integer getDiskSize() {
        return diskSize;
    }

    /**
     * @param diskSize the diskSize to set
     */
    public void setDiskSize(Integer diskSize) {
        this.diskSize = diskSize;
    }

    /**
     * @return the diskType
     */
    public String getDiskType() {
        return diskType;
    }

    /**
     * @param diskType the diskType to set
     */
    public void setDiskType(String diskType) {
        this.diskType = diskType;
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
     * @return the vcpu
     */
    public Integer getVcpu() {
        return vcpu;
    }

    /**
     * @param vcpu the vcpu to set
     */
    public void setVcpu(Integer vcpu) {
        this.vcpu = vcpu;
    }

    /**
     * @return the t
     */
    public Template getT() {
        return t;
    }
}
