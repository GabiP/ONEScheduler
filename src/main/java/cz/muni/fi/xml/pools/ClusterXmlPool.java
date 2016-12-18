package cz.muni.fi.xml.pools;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elements.ClusterElement;
import cz.muni.fi.xml.mappers.ClusterXmlMapper;
import cz.muni.fi.xml.resources.lists.ClusterXmlList;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Gabriela Podolnikova
 */
public class ClusterXmlPool implements IClusterPool {
    
    private List<ClusterElement> clusters;
    
    ClusterXmlMapper clusterXmlMapper = Mappers.getMapper(ClusterXmlMapper.class);

    public ClusterXmlPool(String clusterPoolPath) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        String clusterPoolMessage = new String(Files.readAllBytes(Paths.get(clusterPoolPath)));
        ClusterXmlList xmlList = xmlMapper.readValue(clusterPoolMessage, ClusterXmlList.class);
        clusters =  clusterXmlMapper.map(xmlList.getClusters());
    }
    
    @Override
    public List<ClusterElement> getClusters() {
        return Collections.unmodifiableList(clusters);
    }

    @Override
    public ClusterElement getCluster(int id) {
        for (ClusterElement cl : clusters) {
            if (cl.getId() == id) {
                return cl;
            }
        }
        return null;
    } 
    
    public void writeToFile(String filePath) throws IOException {
        File file = new File(filePath);         
        XmlMapper xmlMapper = new XmlMapper(); 
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.writeValue(file, this);
    }
}
