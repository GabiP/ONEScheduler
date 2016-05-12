/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pools;

import cz.muni.fi.resources.TemplateXml;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.template.Template;
import org.opennebula.client.template.TemplatePool;

/**
 *
 * @author Gabriela Podolnikova
 */
public class TemplateXmlPool {
    
    private TemplatePool tp;
    
    private ArrayList<TemplateXml> templates;
    
    public TemplateXmlPool(Client oneClient) {
        tp = new TemplatePool(oneClient);
    }
    
    public void loadTemplates() {
        templates = new ArrayList<>();
        OneResponse tpr = tp.info();
        if (tpr.isError()) {
            //TODO: log it
            System.out.println(tpr.getErrorMessage());
        }
        Iterator<Template> itr = tp.iterator();
        while (itr.hasNext()) {
            Template element = itr.next();
            TemplateXml t = new TemplateXml(element);
            templates.add(t);
        }
    }
    
     public TemplateXml findById(Integer id) {
        for (TemplateXml t: templates) {
            if (Objects.equals(t.getId(), id)) {
                return t;
            }
        }
        return null;
    }

    /**
     * @return the templates
     */
    public ArrayList<TemplateXml> getTemplates() {
        return templates;
    }
}
