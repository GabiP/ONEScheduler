package cz.muni.fi.one.mappers;

import cz.muni.fi.one.XpathLoader;
import cz.muni.fi.scheduler.elements.DatastoreElement;
import org.opennebula.client.datastore.Datastore;

/**
 * This class maps OpenNebula's Datastore class to DatastoreElement class.
 * Retreives from OpenNebula's Datastore instance its attributes by using OpenNebula's Java API.
 * For further information of Java API please refer to: https://docs.opennebula.org/5.2/integration/system_interfaces/java.html
 * 
 * @author Gabriela Podolnikova
 */
public class DatastoreMapper {
    
    public static DatastoreElement map(Datastore ds) {
        DatastoreElement result = new DatastoreElement();
        ds.info();
               
        result.setId(XpathLoader.getInt(ds, "/DATASTORE/ID"));
        result.setUid(XpathLoader.getInt(ds, "/DATASTORE/UID"));
        result.setGid(XpathLoader.getInt(ds, "/DATASTORE/GID"));
        result.setName(ds.xpath("/DATASTORE/NAME"));
        result.setType(XpathLoader.getInt(ds, "/DATASTORE/TYPE"));
        result.setState(XpathLoader.getInt(ds, "/DATASTORE/STATE"));
        result.setTmMadName(ds.xpath("/DATASTORE/TEMPLATE/TM_MAD"));
        result.setOwner_u(XpathLoader.getInt(ds, "/DATASTORE/PERMISSIONS/OWNER_U"));
        result.setOwner_m(XpathLoader.getInt(ds, "/DATASTORE/PERMISSIONS/OWNER_M"));
        result.setOwner_a(XpathLoader.getInt(ds, "/DATASTORE/PERMISSIONS/OWNER_A"));
        result.setGroup_u(XpathLoader.getInt(ds, "/DATASTORE/PERMISSIONS/GROUP_U"));
        result.setGroup_m(XpathLoader.getInt(ds, "/DATASTORE/PERMISSIONS/GROUP_M"));
        result.setGroup_a(XpathLoader.getInt(ds, "/DATASTORE/PERMISSIONS/GROUP_A"));
        result.setOther_u(XpathLoader.getInt(ds, "/DATASTORE/PERMISSIONS/GROUP_U"));
        result.setOther_m(XpathLoader.getInt(ds, "/DATASTORE/PERMISSIONS/OTHER_M"));
        result.setOther_a(XpathLoader.getInt(ds, "/DATASTORE/PERMISSIONS/OTHER_A"));
        result.setClusters(XpathLoader.getIntList(ds, "/DATASTORE/CLUSTERS/ID"));
        result.setTotal_mb(XpathLoader.getInt(ds, "/DATASTORE/TOTAL_MB"));
        result.setFree_mb(XpathLoader.getInt(ds, "/DATASTORE/FREE_MB"));
        result.setUsed_mb(XpathLoader.getInt(ds, "/DATASTORE/USED_MB"));   
                
        return result;
    }
}
