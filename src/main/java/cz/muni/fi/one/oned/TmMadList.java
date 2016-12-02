package cz.muni.fi.one.oned;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
@JacksonXmlRootElement(localName = "TM_MAD_CONFIGURATION")
public class TmMadList {

    @JacksonXmlProperty(localName = "TM_MAD_CONF")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<TmMadConf> tmMadList;

    public List<TmMadConf> getTmMadList() {
        return tmMadList;
    }

    public void setHosts(List<TmMadConf> tmMadList) {
        this.tmMadList = tmMadList;
    }
    
}
