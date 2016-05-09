/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.resources;

import org.opennebula.client.PoolElement;

/**
 *
 * @author gabi
 */
public class NicNode extends NodeElement {
    
    private static final String XPATH_EXPR = "/VM/TEMPLATE/NIC";
     
    private Integer networkId;

    @Override
    void load(PoolElement vm, int i) {
        networkId = Integer.parseInt(vm.xpath(XPATH_EXPR + "["+i+"]" + "/NETWORK_ID"));
    }

    @Override
    String getXpathExpr() {
        return XPATH_EXPR;
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
