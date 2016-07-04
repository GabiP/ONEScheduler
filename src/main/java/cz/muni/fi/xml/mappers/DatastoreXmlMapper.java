/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.mappers;

import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.xml.resources.DatastoreXml;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public class DatastoreXmlMapper {

    public static List<DatastoreElement> map(List<DatastoreXml> datastores) {
        List<DatastoreElement> result = new ArrayList<>();
        for (DatastoreXml xml : datastores) {
            result.add(map(xml));
        }
        return result;
    }

    public static DatastoreElement map(DatastoreXml datastore) {
        DatastoreElement ds = new DatastoreElement();
        ds.setId(datastore.getId());
        ds.setGid(datastore.getGid());
        ds.setUid(datastore.getUid());
        ds.setName(datastore.getName());
        ds.setType(datastore.getType());
        ds.setFree_mb(datastore.getFree_mb());
        ds.setTotal_mb(datastore.getTotal_mb());
        ds.setUsed_mb(datastore.getUsed_mb());
        ds.setGroup_a(datastore.getGroup_a());
        ds.setGroup_m(datastore.getGroup_m());
        ds.setGroup_u(datastore.getGroup_u());
        ds.setOther_a(datastore.getOther_a());
        ds.setOther_m(datastore.getOther_m());
        ds.setOther_u(datastore.getOther_u());
        ds.setOwner_a(datastore.getOwner_a());
        ds.setOwner_m(datastore.getOwner_m());
        ds.setOwner_u(datastore.getOwner_u());
        return ds;
    }
}
