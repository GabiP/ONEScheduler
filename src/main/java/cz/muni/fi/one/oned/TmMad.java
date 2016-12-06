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
#   ln_target : determines how the persistent images will be cloned when
#               a new VM is instantiated.
#       NONE: The image will be linked and no more storage capacity will be used
#       SELF: The image will be cloned in the Images datastore
#       SYSTEM: The image will be cloned in the System datastore
#   clone_target : determines how the non persistent images will be
#                  cloned when a new VM is instantiated.
#       NONE: The image will be linked and no more storage capacity will be used
#       SELF: The image will be cloned in the Images datastore
#       SYSTEM: The image will be cloned in the System datastore
#   shared : determines if the storage holding the system datastore is shared
#            among the different hosts or not. Valid values: "yes" or "no"
#   ds_migrate : The driver allows migrations across datastores. Valid values:
#               "yes" or "no". Note: THIS ONLY APPLIES TO SYSTEM DS.
 * @author Gabriela Podolnikova
 */
public class TmMad {
    
    @JacksonXmlProperty(localName = "NAME")
    private String name;
    
    @JacksonXmlProperty(localName = "LN_TARGET")
    private String lnTarget;
    
    @JacksonXmlProperty(localName = "CLONE_TARGET")
    private String cloneTarget;
    
    @JacksonXmlProperty(localName = "SHARED")
    private String shared;
    
    @JacksonXmlProperty(localName = "DS_MIGRATE")
    private String dsMigrate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLnTarget() {
        return lnTarget;
    }

    public void setLnTarget(String lnTarget) {
        this.lnTarget = lnTarget;
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

    public String getDsMigrate() {
        return dsMigrate;
    }

    public void setDsMigrate(String dsMigrate) {
        this.dsMigrate = dsMigrate;
    }


}
