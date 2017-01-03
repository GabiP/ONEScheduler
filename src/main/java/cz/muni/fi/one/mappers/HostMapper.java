package cz.muni.fi.one.mappers;

import cz.muni.fi.one.XpathLoader;
import cz.muni.fi.scheduler.elements.nodes.DatastoreNode;
import cz.muni.fi.scheduler.elements.HostElement;
import cz.muni.fi.scheduler.elements.nodes.PciNode;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opennebula.client.host.Host;

/**
 * This class maps OpenNebula's Host class to HostElement class.
 * Retreives from OpenNebula's Host instance its attributes by using OpenNebula's Java API.
 * For further information of Java API please refer to: http://docs.opennebula.org/doc/4.14/oca/java/
 * 
 * @author Gabriela Podolnikova
 */
public class HostMapper {
    
    public static HostElement map(Host host) {
        HostElement result = new HostElement();
        host.info();
        
        result.setId(XpathLoader.getInt(host, "/HOST/ID"));
        result.setName(host.xpath("/HOST/NAME"));
        result.setState(XpathLoader.getInt(host, "/HOST/STATE"));
        result.setClusterId(XpathLoader.getInt(host, "/HOST/CLUSTER_ID"));
        result.setDisk_usage(XpathLoader.getInt(host, "/HOST/HOST_SHARE/DISK_USAGE"));
        result.setMem_usage(XpathLoader.getInt(host, "/HOST/HOST_SHARE/MEM_USAGE")/1024);
        result.setCpu_usage(XpathLoader.getFloat(host, "/HOST/HOST_SHARE/CPU_USAGE")/100);
        result.setMax_disk(XpathLoader.getInt(host, "/HOST/HOST_SHARE/MAX_DISK"));
        result.setMax_mem(XpathLoader.getInt(host, "/HOST/HOST_SHARE/MAX_MEM")/1024);
        result.setMax_cpu(XpathLoader.getFloat(host, "/HOST/HOST_SHARE/MAX_CPU")/100);
        result.setFree_disk(XpathLoader.getInt(host, "/HOST/HOST_SHARE/FREE_DISK"));
        result.setFree_mem(XpathLoader.getInt(host, "/HOST/HOST_SHARE/FREE_MEM")/1024);
        result.setFree_cpu(XpathLoader.getFloat(host, "/HOST/HOST_SHARE/FREE_CPU")/100);
        result.setUsed_disk(XpathLoader.getInt(host, "/HOST/HOST_SHARE/USED_DISK"));
        result.setUsed_mem(XpathLoader.getInt(host, "/HOST/HOST_SHARE/USED_MEM")/1024);
        result.setUsed_cpu(XpathLoader.getInt(host, "/HOST/HOST_SHARE/USED_CPU")/100);
        result.setRunningVms(XpathLoader.getInt(host, "/HOST/HOST_SHARE/RUNNING_VMS"));
        result.setReservedCpu(XpathLoader.getFloatOrZero(host, "/HOST/TEMPLATE/RESERVED_CPU")/100);
        result.setReservedMemory(XpathLoader.getIntOrZero(host, "/HOST/TEMPLATE/RESERVED_MEM")/1024);
        result.setVms(XpathLoader.getIntList(host, "/HOST/VMS/ID"));
        
        try { 
            result.setPcis(XpathLoader.getNodeList(host, PciNode.class, "/HOST/HOST_SHARE/PCI_DEVICES/PCI"));
            result.setDatastores(XpathLoader.getNodeList(host, DatastoreNode.class, "/HOST/HOST_SHARE/DATASTORES/DS"));
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(HostMapper.class.getName()).log(Level.SEVERE, null, ex);
        }             
        
        return result;
    }
}
