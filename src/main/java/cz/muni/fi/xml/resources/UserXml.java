package cz.muni.fi.xml.resources;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import cz.muni.fi.xml.resources.nodes.DatastoreQuotaXml;
import cz.muni.fi.xml.resources.nodes.VmQuotaXml;
import java.util.List;

/**
 * This class represents the User retrived from xml.
 * @author Andras Urge
 */
public class UserXml {
    @JacksonXmlProperty(localName = "ID")
    private Integer id;
    
    @JacksonXmlProperty(localName = "GID")
    private Integer gid;
    
    @JacksonXmlProperty(localName = "GROUPS")
    private List<Integer> groups;
    
    @JacksonXmlProperty(localName = "DATASTORE_QUOTA")
    private List<DatastoreQuotaXml> datastoreQuotasXml;
    
    @JacksonXmlProperty(localName = "GROUPS")
    private VmQuotaXml vmQuotaXml;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public List<Integer> getGroups() {
        return groups;
    }

    public void setGroups(List<Integer> groups) {
        this.groups = groups;
    }

    public List<DatastoreQuotaXml> getDatastoreQuotasXml() {
        return datastoreQuotasXml;
    }

    public void setDatastoreQuotasXml(List<DatastoreQuotaXml> datastoreQuotasXml) {
        this.datastoreQuotasXml = datastoreQuotasXml;
    }

    public VmQuotaXml getVmQuotaXml() {
        return vmQuotaXml;
    }

    public void setVmQuotaXml(VmQuotaXml vmQuotaXml) {
        this.vmQuotaXml = vmQuotaXml;
    }

    @Override
    public String toString() {
        return "UserXml{" + "id=" + id + ", gid=" + gid + ", groups=" + groups + '}';
    }
}
