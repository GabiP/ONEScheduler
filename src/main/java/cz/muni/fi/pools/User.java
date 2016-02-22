package cz.muni.fi.pools;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 *
 * @author Gabriela Podolnikova
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    
    @JacksonXmlProperty(localName = "ID")
    private Integer id;
    
    @JacksonXmlProperty(localName = "GID")
    private Integer gid;
    
    @JacksonXmlProperty(localName = "GROUPS")
    private Integer[] groups;
    
    @JacksonXmlProperty(localName = "GNAME")
    private String gname;
    
    @JacksonXmlProperty(localName = "AUTH_DRIVER")
    private String auth_driver;
    
    @JacksonXmlProperty(localName = "ENABLED")
    private Integer enabled;
    
    @JacksonXmlProperty(localName = "LOGIN_TOKEN")
    private String login_token;
    
    @JacksonXmlProperty(localName = "DATASTORE")
    private Datastore[] datastores;
    
    @JacksonXmlProperty(localName = "NETWORK")
    private Network[] networks;
    
    @JacksonXmlProperty(localName = "VM")
    private VmQuota vmQuota;
    
    @JacksonXmlProperty(localName = "IMAGE")
    private Image[] images;

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
     * @return the gid
     */
    public Integer getGid() {
        return gid;
    }

    /**
     * @param gid the gid to set
     */
    public void setGid(Integer gid) {
        this.gid = gid;
    }

    /**
     * @return the groups
     */
    public Integer[] getGroups() {
        return groups;
    }

    /**
     * @param groups the groups to set
     */
    public void setGroups(Integer[] groups) {
        this.groups = groups;
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
     * @return the auth_driver
     */
    public String getAuth_driver() {
        return auth_driver;
    }

    /**
     * @param auth_driver the auth_driver to set
     */
    public void setAuth_driver(String auth_driver) {
        this.auth_driver = auth_driver;
    }

    /**
     * @return the enabled
     */
    public Integer getEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the login_token
     */
    public String getLogin_token() {
        return login_token;
    }

    /**
     * @param login_token the login_token to set
     */
    public void setLogin_token(String login_token) {
        this.login_token = login_token;
    }

    /**
     * @return the datastores
     */
    public Datastore[] getDatastores() {
        return datastores;
    }

    /**
     * @param datastores the datastores to set
     */
    public void setDatastores(Datastore[] datastores) {
        this.datastores = datastores;
    }

    /**
     * @return the networks
     */
    public Network[] getNetworks() {
        return networks;
    }

    /**
     * @param networks the networks to set
     */
    public void setNetworks(Network[] networks) {
        this.networks = networks;
    }

    /**
     * @return the vmQuota
     */
    public VmQuota getVmQuota() {
        return vmQuota;
    }

    /**
     * @param vmQuota the vmQuota to set
     */
    public void setVmQuota(VmQuota vmQuota) {
        this.vmQuota = vmQuota;
    }

    /**
     * @return the images
     */
    public Image[] getImages() {
        return images;
    }

    /**
     * @param images the images to set
     */
    public void setImages(Image[] images) {
        this.images = images;
    }
}
