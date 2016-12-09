/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.mappers;

import cz.muni.fi.scheduler.elements.HostElement;
import cz.muni.fi.scheduler.elements.nodes.DatastoreNode;
import cz.muni.fi.scheduler.elements.nodes.PciNode;
import cz.muni.fi.xml.resources.nodes.DatastoreNodeXml;
import cz.muni.fi.xml.resources.HostXml;
import cz.muni.fi.xml.resources.nodes.PciNodeXml;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Gabriela Podolnikova
 */
public class HostXmlMapper {
    
    public static List<HostElement> map(List<HostXml> hosts) {
        return hosts.stream().map(HostXmlMapper::map).collect(Collectors.toList());
    }
    
    public static HostElement map(HostXml host) {
        HostElement h = new HostElement();
        h.setId(host.getId());
        h.setName(host.getName());
        h.setState(host.getState());
        h.setClusterId(host.getClusterId());
        h.setDisk_usage(host.getDisk_usage());
        h.setMem_usage(host.getMem_usage()/1024);
        h.setCpu_usage(host.getCpu_usage()/100);
        h.setMax_disk(host.getMax_disk());
        h.setMax_mem(host.getMax_mem()/1024);
        h.setMax_cpu(host.getMax_cpu()/100);
        h.setFree_disk(host.getFree_disk());
        h.setFree_mem(host.getFree_mem()/1024);
        h.setFree_cpu(host.getFree_cpu()/100);
        h.setUsed_disk(host.getUsed_disk());
        h.setUsed_mem(host.getUsed_mem()/1024);
        h.setUsed_cpu(host.getUsed_cpu()/100);
        h.setRunningVms(host.getRunningVms());
        if (host.getReservedCpu() == null) {
            h.setReservedCpu(0.00f);
        } else {
            h.setReservedCpu(host.getReservedCpu());
        }
        if (host.getReservedMemory() == null) {
            h.setReservedMemory(0);
        } else {
            h.setReservedMemory(host.getReservedMemory());
        }
        h.setVms(host.getVms());
        h.setPcis(mapPcis(host.getPcis()));
        h.setDatastores(mapDatastores(host.getDatastores()));
        return h;
    }
    
    public static List<PciNode> mapPcis(List<PciNodeXml> pcis) {
        List<PciNode> result = new ArrayList<>();
        if (pcis != null) {
            result.addAll(pcis.stream().map(HostXmlMapper::map).collect(Collectors.toList()));
        }
        return result;
    }
    
    public static PciNode map(PciNodeXml pci) {
        PciNode result = new PciNode();
        result.setPci_class(pci.getPci_class());
        result.setDevice(pci.getDevice());
        result.setVendor(pci.getVendor());
        return result;
    }
    
    public static List<DatastoreNode> mapDatastores(List<DatastoreNodeXml> datastores) {
        List<DatastoreNode> result = new ArrayList<>();
        if (datastores !=null) {
            result.addAll(datastores.stream().map(HostXmlMapper::map).collect(Collectors.toList()));
        }
        return result;
    }
    
    public static DatastoreNode map(DatastoreNodeXml ds) {
        DatastoreNode result = new DatastoreNode();
        result.setId_ds(ds.getId_ds());
        result.setTotal_mb(ds.getTotal_mb());
        result.setFree_mb(ds.getFree_mb());
        result.setUsed_mb(ds.getUsed_mb());
        return result;
    }
}
