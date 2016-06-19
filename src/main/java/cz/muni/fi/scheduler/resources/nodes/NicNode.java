/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.resources.nodes;

import org.opennebula.client.PoolElement;

/**
 *
 * @author gabi
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
}
