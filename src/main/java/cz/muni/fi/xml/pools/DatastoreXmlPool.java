package cz.muni.fi.xml.pools;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elements.DatastoreElement;
import cz.muni.fi.xml.mappers.DatastoreXmlMapper;
import cz.muni.fi.xml.resources.lists.DatastoreXmlList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Gabriela Podolnikova
 */
public class DatastoreXmlPool implements IDatastorePool {
    
    private List<DatastoreElement> datastores;   

    public DatastoreXmlPool(String dsPoolPath) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        String dsPoolMessage = new String(Files.readAllBytes(Paths.get(dsPoolPath)));
        DatastoreXmlList xmlList = xmlMapper.readValue(dsPoolMessage, DatastoreXmlList.class);
        datastores = DatastoreXmlMapper.map(xmlList.getDatastores());
    }
                    
    @Override
    public List<DatastoreElement> getDatastores() {
        return Collections.unmodifiableList(datastores);
    }

    @Override
    public DatastoreElement getDatastore(int id) {
        for (DatastoreElement ds : datastores) {
            if (ds.getId() == id) {
                return ds;
            }
        }
        return null;
    }

    @Override
    public List<DatastoreElement> getSystemDs() {
        return getDatastores().stream().filter(ds -> ds.getType() == 1).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getDatastoresIds() {
        return getDatastores().stream().map(DatastoreElement::getId).collect(Collectors.toList());
    }

    @Override
    public List<DatastoreElement> getImageDs() {
        return getDatastores().stream().filter(ds -> ds.getType() == 0).collect(Collectors.toList());
    }
    
}
