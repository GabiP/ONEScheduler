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
    
    private final Client oneClient;
    
    public TemplateXmlPool(Client oneClient) {
        this.oneClient = oneClient;
    }
    
    public ArrayList<TemplateXml> loadTemplates() {
        templates = new ArrayList<>();
        tp = new TemplatePool(oneClient);
        OneResponse tpr = tp.info();
        if (tpr.isError()) {
            //TODO: log it
            System.out.println(tpr.getErrorMessage());
        }
        Iterator<Template> itr = tp.iterator();
        while (itr.hasNext()) {
            Template element = itr.next();
            System.out.println("Template: " + element);
            TemplateXml t = new TemplateXml(element);
            System.out.println("Template: " + t);
            templates.add(t);
        }
        return templates;
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
