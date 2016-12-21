/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.mappers;

import cz.muni.fi.scheduler.elements.DatastoreElement;
import cz.muni.fi.xml.resources.DatastoreXml;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Gabriela Podolnikova
 */
public class DatastoreXmlMapper {

    public static List<DatastoreElement> map(List<DatastoreXml> datastores) {
        return datastores.stream().map(DatastoreXmlMapper::map).collect(Collectors.toList());
    }
    
    public static List<DatastoreXml> mapToXml(List<DatastoreElement> datastores) {
        return datastores.stream().map(DatastoreXmlMapper::mapToXml).collect(Collectors.toList());
    }

    public static DatastoreElement map(DatastoreXml datastore) {
        DatastoreElement ds = new DatastoreElement();
        ds.setId(datastore.getId());
        ds.setGid(datastore.getGid());
        ds.setUid(datastore.getUid());
        ds.setName(datastore.getName());
        ds.setType(datastore.getType());
        ds.setState(datastore.getState());
        ds.setTmMadName(datastore.getTmMadName());
        ds.setFree_mb(datastore.getFree_mb());
        ds.setTotal_mb(datastore.getTotal_mb());
        ds.setClusters(datastore.getClusters());
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
    
    public static DatastoreXml mapToXml(DatastoreElement datastore) {
        DatastoreXml ds = new DatastoreXml();
        ds.setId(datastore.getId());
        ds.setGid(datastore.getGid());
        ds.setUid(datastore.getUid());
        ds.setName(datastore.getName());
        ds.setType(datastore.getType());
        ds.setState(datastore.getState());
        ds.setTmMadName(datastore.getTmMadName());
        ds.setFree_mb(datastore.getFree_mb());
        ds.setTotal_mb(datastore.getTotal_mb());
        ds.setClusters(datastore.getClusters());
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
