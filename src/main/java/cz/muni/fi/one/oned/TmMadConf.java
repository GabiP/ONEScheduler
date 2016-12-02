package cz.muni.fi.one.oned;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * # Transfer Manager Driver Behavior Configuration
#*******************************************************************************
# The  configuration for each driver is defined in TM_MAD_CONF. These
# values are used when creating a new datastore and should not be modified
# since they define the datastore behavior.
#   name      : name of the transfer driver, listed in the -d option of the
#               TM_MAD section
#   clone_target : determines how the non persistent images will be
#                  cloned when a new VM is instantiated.
#       NONE: The image will be linked and no more storage capacity will be used
#       SELF: The image will be cloned in the Images datastore
#       SYSTEM: The image will be cloned in the System datastore
#   shared : determines if the storage holding the system datastore is shared
#            among the different hosts or not. Valid values: "yes" or "no"
 * @author Gabriela Podolnikova
 */
public class TmMadConf {
    
    @JacksonXmlProperty(localName = "NAME")
    private String name;
    
    @JacksonXmlProperty(localName = "CLONE_TARGET")
    private String cloneTarget;
    
    @JacksonXmlProperty(localName = "SHARED")
    private String shared;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCloneTarget() {
        return cloneTarget;
    }

    public void setCloneTarget(String cloneTarget) {
        this.cloneTarget = cloneTarget;
    }

    public String getShared() {
        return shared;
    }

    public void setShared(String shared) {
        this.shared = shared;
    }
    
}
