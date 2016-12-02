package cz.muni.fi.one.oned;

import java.util.ArrayList;
import java.util.List;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.OneSystem;
import org.w3c.dom.Node;

/**
 *
 * @author Gabriela Podolnikova
 */
public class OnedConf {
    
    OneResponse onedConf;
    
    Node nodeOned;
    
    public OnedConf(Client client) {
        OneSystem oneSystem = new OneSystem(client);
        nodeOned = oneSystem.getConfigurationXML();
        System.out.println("ONEd" + nodeOned.getFirstChild());
    }
    
    //parse the oned conf - how?
    public List<TmMadConf> getTmMad() {
        List<TmMadConf> tmMads = new ArrayList<>();
        if (nodeOned.getNodeType() == Node.ELEMENT_NODE) {
            //calls this method for all the children which is Element
            System.out.println(nodeOned.getNodeName());
        }
        System.out.println(nodeOned.getTextContent());
        
        System.out.println(nodeOned.getNodeValue());

        System.out.println(nodeOned.getUserData("ds_migrate"));
        
        return tmMads;
    }
    
}
