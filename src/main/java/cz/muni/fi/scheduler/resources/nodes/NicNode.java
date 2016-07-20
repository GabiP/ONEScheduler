package cz.muni.fi.scheduler.resources.nodes;

import org.opennebula.client.PoolElement;

/**
 * This class represents a NicNode.
 * A virtual machine can have multiple nic nodes. A virtual machine in OpenNebula system has a set of NICs attached to one or more virtual networks.
 * Loads the data from xml by using OpenNebula's API.
 * 
 * @author Gabriela Podolnikova
 */
public class NicNode extends AbstractNode {
         
    private Integer networkId;

    @Override
    public void load(PoolElement vm, String xpathExpr) {
        networkId = Integer.parseInt(vm.xpath(xpathExpr + "/NETWORK_ID"));
    }

    /**
     * @return the networkId
     */
    public Integer getNetworkId() {
        return networkId;
    }

    /**
     * @param networkId the networkId to set
     */
    public void setNetworkId(Integer networkId) {
        this.networkId = networkId;
    }

    @Override
    public String toString() {
        return "NicNode{" + "networkId=" + networkId + '}';
    }
}
