/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.mappers;

import cz.muni.fi.scheduler.elements.UserElement;
import cz.muni.fi.scheduler.elements.nodes.DatastoreQuota;
import cz.muni.fi.scheduler.elements.nodes.VmQuota;
import cz.muni.fi.xml.resources.UserXml;
import cz.muni.fi.xml.resources.nodes.DatastoreQuotaXml;
import cz.muni.fi.xml.resources.nodes.VmQuotaXml;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Andras Urge
 */
public class UserXmlMapper {
    
    public static List<UserElement> map(List<UserXml> users) {
        return users.stream().map(UserXmlMapper::map).collect(Collectors.toList());
    }
    
    public static UserElement map(UserXml user) {
        UserElement result = new UserElement();
        result.setId(user.getId());
        result.setGid(user.getGid());
        result.setGroups(user.getGroups());
        result.setDatastoreQuota(mapDSQuotas(user.getDatastoreQuotasXml()));
        result.setVmQuota(mapVMQuota(user.getVmQuotaXml()));
        return result;
    }
    
    public static List<DatastoreQuota> mapDSQuotas(List<DatastoreQuotaXml> quotas) {
        List<DatastoreQuota> result = new ArrayList<>();
        if (quotas !=null) {
            result.addAll(quotas.stream().map(UserXmlMapper::map).collect(Collectors.toList()));
        }
        return result;
    }
    
    public static DatastoreQuota map(DatastoreQuotaXml q) {
        DatastoreQuota result = new DatastoreQuota();
        result.setId(q.getId());
        result.setImages(q.getImages());
        result.setImagesUsed(q.getImagesUsed());
        result.setSize(q.getSize());
        result.setSizeUsed(q.getSizeUsed());
        return result;
    }
    
     public static VmQuota mapVMQuota(VmQuotaXml q) {
        VmQuota result = new VmQuota();
        if (q != null) {
            result.setCpu(q.getCpu());
            result.setCpuUsed(q.getCpuUsed());
            result.setMemory(q.getMemory());
            result.setMemoryUsed(q.getMemoryUsed());
            result.setSystemDiskSize(q.getSystemDiskSize());
            result.setSystemDiskSizeUsed(q.getSystemDiskSizeUsed());
            result.setVms(q.getVms());
            result.setVmsUsed(q.getVmsUsed());
        }
        return result;
    }
}
