package cz.muni.fi.xml.mappers;

import cz.muni.fi.scheduler.elements.UserElement;
import cz.muni.fi.scheduler.elements.nodes.DatastoreQuota;
import cz.muni.fi.scheduler.elements.nodes.VmQuota;
import cz.muni.fi.xml.resources.UserXml;
import cz.muni.fi.xml.resources.nodes.DatastoreQuotaXml;
import cz.muni.fi.xml.resources.nodes.VmQuotaXml;
import java.util.List;
import org.mapstruct.Mapper;

/**
 *
 * @author Andras Urge
 */
@Mapper
public abstract class UserXmlMapper {
    
    public abstract List<UserElement> map(List<UserXml> users);
    
    public abstract List<UserXml> mapToXml(List<UserElement> users);
    
    public abstract UserElement map(UserXml user);
    
    public abstract UserXml mapToXml(UserElement user);
    
    public abstract List<DatastoreQuota> mapDSQuotas(List<DatastoreQuotaXml> quotas);
    
    public abstract List<DatastoreQuotaXml> mapDSQuotasToXml(List<DatastoreQuota> quotas);
    
    public abstract DatastoreQuota map(DatastoreQuotaXml q);
    
    public abstract DatastoreQuotaXml mapToXml(DatastoreQuota q);
    
    public abstract VmQuota mapVMQuota(VmQuotaXml q);
     
    public abstract VmQuotaXml mapVMQuotaToXml(VmQuota q);
    
}
