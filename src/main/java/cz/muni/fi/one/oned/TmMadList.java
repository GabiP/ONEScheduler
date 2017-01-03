package cz.muni.fi.one.oned;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;

/**
 * This class is used for getting the driver's configuration list.
 * 
 * @author Gabriela Podolnikova
 */
@JacksonXmlRootElement(localName = "TM_MAD_CONFIGURATION")
public class TmMadList {

    @JacksonXmlProperty(localName = "TM_MAD_CONF")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<TmMad> tmMadList;

    public List<TmMad> getTmMadList() {
        return tmMadList;
    }

    public void setHosts(List<TmMad> tmMadList) {
        this.tmMadList = tmMadList;
    }
    
}
