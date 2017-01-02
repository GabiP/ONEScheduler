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
 * This class maps the UserXml class to the UserElement class and vice versa.
 * @author Andras Urge
 */
public class UserXmlMapper {
    
    public static List<UserElement> map(List<UserXml> users) {
        return users.stream().map(UserXmlMapper::map).collect(Collectors.toList());
    }
    
    public static List<UserXml> mapToXml(List<UserElement> users) {
        return users.stream().map(UserXmlMapper::mapToXml).collect(Collectors.toList());
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
    
    public static UserXml mapToXml(UserElement user) {
        UserXml result = new UserXml();
        result.setId(user.getId());
        result.setGid(user.getGid());
        result.setGroups(user.getGroups());
        result.setDatastoreQuotasXml(mapDSQuotasToXml(user.getDatastoreQuota()));
        result.setVmQuotaXml(mapVMQuotaToXml(user.getVmQuota()));
        return result;
    }
    
    public static List<DatastoreQuota> mapDSQuotas(List<DatastoreQuotaXml> quotas) {
        List<DatastoreQuota> result = new ArrayList<>();
        if (quotas !=null) {
            result.addAll(quotas.stream().map(UserXmlMapper::map).collect(Collectors.toList()));
        }
        return result;
    }
    
    public static List<DatastoreQuotaXml> mapDSQuotasToXml(List<DatastoreQuota> quotas) {
        List<DatastoreQuotaXml> result = new ArrayList<>();
        if (quotas !=null) {
            result.addAll(quotas.stream().map(UserXmlMapper::mapToXml).collect(Collectors.toList()));
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
    
    public static DatastoreQuotaXml mapToXml(DatastoreQuota q) {
        DatastoreQuotaXml result = new DatastoreQuotaXml();
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
     
    public static VmQuotaXml mapVMQuotaToXml(VmQuota q) {
        VmQuotaXml result = new VmQuotaXml();
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
