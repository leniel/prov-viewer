/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Input;

import br.uff.ic.provviewer.Attribute;
import br.uff.ic.provviewer.Edge.Edge;
import br.uff.ic.provviewer.GraphObject;
import br.uff.ic.provviewer.Vertex.ActivityVertex;
import br.uff.ic.provviewer.Vertex.AgentVertex;
import br.uff.ic.provviewer.Vertex.EntityVertex;
import br.uff.ic.provviewer.Vertex.Vertex;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 *
 * @author Kohwalter
 */
public class PROVNReader extends InputReader {

    int edgeOptionalID = 0;
    public PROVNReader(File f) throws URISyntaxException, IOException{
        super(f);
    }
    
    public void readFile() throws URISyntaxException, IOException{
        BufferedReader br = new BufferedReader(new FileReader(file));
        try {
            String line = br.readLine();
            
            Vertex node = new AgentVertex("Unknown", "Unknown", "");
            addNode(node);
            
            while (line != null) {
                Read(line);
                line = br.readLine();
            }
        } finally {
            br.close();
        }

    }
    public void Read(String line){
        String[] elements;
        String[] statement;
        String[] attributes;
        String[] optionalAttributes = null;
        
        System.out.println(line);
        line = line.replace(")", "");
        line = line.replace(" ", "");
        line = line.replace("\t", "");
        line = line.replace("]", "");
        line = line.replace("'", "");
        elements = line.split("\\(");
        if(elements.length > 1)
        {
            statement = elements[1].split("\\[");
//            if(optionalAttributes.length > 1)
//            {
//                optionalAttributes[1] = optionalAttributes[1].replace("]", "");
//            }
            attributes = statement[0].split(",");
            if(statement.length > 1)
                optionalAttributes = statement[1].split(",");

            if(elements[0].contains("entity")){
                readEntity(attributes, optionalAttributes);
            }
            if(elements[0].contains("activity")){
                readActivity(attributes, optionalAttributes);
            }
            if(elements[0].contains("agent")){
                readAgent(attributes, optionalAttributes);
            }
            if(elements[0].contains("wasGeneratedBy")){
                readGeneration(attributes, optionalAttributes);
            } 
            if(elements[0].contains("used")){
                readUsage(attributes, optionalAttributes);
            }
            if(elements[0].contains("wasInformedBy")){
                readCommunication(attributes, optionalAttributes);
            }
            if(elements[0].contains("wasStartedBy")){
                readStart(attributes, optionalAttributes);
            }
            if(elements[0].contains("wasEndedBy")){
                readEnd(attributes, optionalAttributes);
            }
            if(elements[0].contains("wasInvalidatedBy")){
                readInvalidation(attributes, optionalAttributes);
            }
            if(elements[0].contains("wasDerivedFrom")){ //Revision, Quotation, Primary Source from PROV-N
                readDerivation(attributes, optionalAttributes);
            }
//            if(elements[0].contains("wasDerivedFrom")){ //Revision from PROV-N
//                readRevision(attributes, optionalAttributes);
//            }
//            if(elements[0].contains("wasDerivedFrom")){ //Quotation from PROV-N
//                readQuotation(attributes, optionalAttributes);
//            }
//            if(elements[0].contains("wasDerivedFrom")){ //Primary Source from PROV-N
//                readPrimarySource(attributes, optionalAttributes);
//            }
            if(elements[0].contains("wasAttributedTo")){
                readAttribution(attributes, optionalAttributes);
            }
            if(elements[0].contains("wasAssociatedWith")){
                readAssociation(attributes, optionalAttributes);
            }
            if(elements[0].contains("actedOnBehalfOf")){
                readDelegation(attributes, optionalAttributes);
            }
            if(elements[0].contains("wasInfluencedBy")){
                readInfluence(attributes, optionalAttributes);
            }
        }
    }

    public void readEntity(String[] attributes, String[] optionalAttributes) {
        Vertex node;
        String id = attributes[0];
        node = new EntityVertex(id, id, "");
        readAttributes(node, optionalAttributes);
        addNode(node);
    }
    public void readActivity(String[] attributes, String[] optionalAttributes) {
        Vertex node;
        String id = attributes[0];
        node = new ActivityVertex(id, id, "");
        if (attributes.length > 1) {
            String startTime = attributes[1];
            
            Attribute optAtt = new Attribute("startTime", startTime);
            node.addAttribute(optAtt);
        }
        if (attributes.length > 2) {
            String endTime = attributes[2];
            Attribute optAtt = new Attribute("endTime", endTime);
            node.addAttribute(optAtt);
        }
        readAttributes(node, optionalAttributes);
        addNode(node); 
    }
    
    public void readAgent(String[] attributes, String[] optionalAttributes){
        Vertex node;
        String id = attributes[0];
        node = new AgentVertex(id, id, "");
        readAttributes(node, optionalAttributes);
        addNode(node);
    }
    
    public void readGeneration(String[] attributes, String[] optionalAttributes){
        Edge edge;
        String id = null;
        String entity = null;
        String activity;
        String time;

        if (attributes.length == 3) {
            id = getEdgeID(attributes[0], id);
            entity = getEdge1stAttribute(attributes[0], entity);
            activity = attributes[1];
            time = attributes[2];       
        }
        else  {
            id = getEdgeID(attributes[0], id);
            entity = getEdge1stAttribute(attributes[0], entity);
            activity = null;
            time = "";
        }
        edge = new Edge(id, "wasGeneratedBy", "-", "-", nodes.get(activity), nodes.get(entity));
        Attribute optAtt = new Attribute("time", time);
        edge.addAttribute(optAtt);
        
        readAttributes(edge, optionalAttributes);
        addEdge(edge);
        
    }
    
    public void readUsage(String[] attributes, String[] optionalAttributes){
        Edge edge;
        String id = null;
        String activity = null;
        String entity;
        String time;

        if (attributes.length == 3) {
            id = getEdgeID(attributes[0], id);
            activity = getEdge1stAttribute(attributes[0], activity);
            entity = attributes[1];
            time = attributes[2];       
        }
        else  {
            id = getEdgeID(attributes[0], id);
            activity = getEdge1stAttribute(attributes[0], activity);
            entity = null;
            time = "";
        }
        
        edge = new Edge(id, "used", "-", "-", nodes.get(entity), nodes.get(activity));
        Attribute optAtt = new Attribute("time", time);
        edge.addAttribute(optAtt);
        
        readAttributes(edge, optionalAttributes);
        addEdge(edge);
    }
    
    public void readCommunication(String[] attributes, String[] optionalAttributes){
        Edge edge;
        String id = null;
        String informed = null;
        String informant;

        id = getEdgeID(attributes[0], id);
        informed = getEdge1stAttribute(attributes[0], informed);
        informant = attributes[1];      
        
        edge = new Edge(id, "wasInformedBy", "-", "-", nodes.get(informant), nodes.get(informed));
        
        readAttributes(edge, optionalAttributes);
        addEdge(edge);
    }
    
    public void readStart(String[] attributes, String[] optionalAttributes){
        starterOrEnder(attributes, optionalAttributes, "wasStartedBy");
    }
    
    public void readEnd(String[] attributes, String[] optionalAttributes){
        starterOrEnder(attributes, optionalAttributes, "wasEndedBy");
    }
    
    public void starterOrEnder(String[] attributes, String[] optionalAttributes, String type) {
        Edge edge;
        String id = null;
        String activity = null;
        String trigger = null;
        String starterOrEnder = null;
        String time;

        if (attributes.length == 4) {
            id = getEdgeID(attributes[0], id);
            activity = getEdge1stAttribute(attributes[0], activity);
            trigger = attributes[1];
            starterOrEnder = attributes[2];     
            time = attributes[3];
        }
        else  {
            id = getEdgeID(attributes[0], id);
            activity = getEdge1stAttribute(attributes[0], activity);
            time = "";
        }
        
        if(trigger == null) {
            edge = new Edge(id, type, "-", "-", nodes.get(starterOrEnder), nodes.get(activity));
            Attribute optAtt = new Attribute("time", time);
            edge.addAttribute(optAtt);

            readAttributes(edge, optionalAttributes);
            addEdge(edge);
        }
        else {
            edge = new Edge(id + "_trigger", type, "-", "-", nodes.get(trigger), nodes.get(activity));
            Attribute optAtt = new Attribute("time", time);
            edge.addAttribute(optAtt);

            readAttributes(edge, optionalAttributes);
            addEdge(edge);
            
            edge = new Edge(id + "_generated", type, "-", "-", nodes.get(starterOrEnder), nodes.get(trigger));
            optAtt = new Attribute("time", time);
            edge.addAttribute(optAtt);

            readAttributes(edge, optionalAttributes);
            addEdge(edge);
        }
    }
    
    public void readInvalidation(String[] attributes, String[] optionalAttributes){
        Edge edge;
        String id = null;
        String entity = null;
        String activity;
        String time;

        id = getEdgeID(attributes[0], id);
        entity = getEdge1stAttribute(attributes[0], entity);
        activity = attributes[1];  
        time = attributes[2];  
        
        edge = new Edge(id, "wasInvalidatedBy", "-", "-", nodes.get(activity), nodes.get(entity));
        Attribute optAtt = new Attribute("time", time);
        edge.addAttribute(optAtt);
            
        readAttributes(edge, optionalAttributes);
        addEdge(edge);
    }
          
    public void readDerivation(String[] attributes, String[] optionalAttributes){
        Edge edge;
        String id = null;
        String generatedEntity = null;
        String usedEntity;
        String activity = null;
        String generation = null;
        String usage = null;
        if (attributes.length == 5) {
            id = getEdgeID(attributes[0], id);
            generatedEntity = getEdge1stAttribute(attributes[0], generatedEntity);
            usedEntity = attributes[1];
            activity = attributes[2];
            generation = attributes[3];
            usage = attributes[4];
        }
        else {
            id = getEdgeID(attributes[0], id);
            generatedEntity = getEdge1stAttribute(attributes[0], generatedEntity);
            usedEntity = attributes[1];
        }
        
        edge = new Edge(id, "wasDerivedFrom", "-", "-", nodes.get(usedEntity), nodes.get(generatedEntity));
        readAttributes(edge, optionalAttributes);
        addEdge(edge);
        
        if (generation != null && !generation.matches("-") && activity != null && !activity.matches("-")) {
            edge = new Edge(generation, "wasGeneratedBy", "-", "-", nodes.get(activity), nodes.get(generatedEntity));
            readAttributes(edge, optionalAttributes);
            addEdge(edge);

            edge = new Edge(usage, "used", "-", "-", nodes.get(usedEntity), nodes.get(activity));
            readAttributes(edge, optionalAttributes);
            addEdge(edge);
        }
    }
    
    public void readRevision(String[] attributes, String[] optionalAttributes){
        readGeneration(attributes, optionalAttributes);
    }
    
    public void readQuotation(String[] attributes, String[] optionalAttributes){
        readGeneration(attributes, optionalAttributes);
    }
    
    public void readPrimarySource(String[] attributes, String[] optionalAttributes){
        readGeneration(attributes, optionalAttributes);
    }
    
    public void readAttribution(String[] attributes, String[] optionalAttributes){
        Edge edge;
        String id = null;
        String entity = null;
        String agent;

        id = getEdgeID(attributes[0], id);
        entity = getEdge1stAttribute(attributes[0], entity);
        agent = attributes[1];   
        
        edge = new Edge(id, "wasAttributedTo", "-", "-", nodes.get(agent), nodes.get(entity));
            
        readAttributes(edge, optionalAttributes);
        addEdge(edge);
    }
    
    public void readAssociation(String[] attributes, String[] optionalAttributes){
        Edge edge;
        String id = null;
        String activity = null;
        String agent;
        String plan = null;

        id = getEdgeID(attributes[0], id);
        activity = getEdge1stAttribute(attributes[0], activity);
        agent = attributes[1];
        if (attributes.length == 3)
            plan = attributes[2]; 
        
        edge = new Edge(id, "wasAssociatedWith", "-", "-", nodes.get(agent), nodes.get(activity)); 
        readAttributes(edge, optionalAttributes);
        addEdge(edge);
        
        if(plan != null && !plan.matches("-"))
        {
//            Vertex node = new EntityVertex(plan, plan, "");
//            Attribute optAtt = new Attribute("prov:type", "prov:Plan");
//            addNode(node);
            edge = new Edge(id, "wasAssociatedWith(Plan)", "-", "-", nodes.get(plan), nodes.get(agent)); 
            readAttributes(edge, optionalAttributes);
            addEdge(edge);
        }
    }
    
    public void readDelegation(String[] attributes, String[] optionalAttributes){
        Edge edge;
        String id = null;
        String delegate = null;
        String responsible;
        String activity = null;

        id = getEdgeID(attributes[0], id);
        delegate = getEdge1stAttribute(attributes[0], delegate);
        responsible = attributes[1]; 
        if (attributes.length == 3)
            activity = attributes[2]; 
        
        edge = new Edge(id, "actedOnBehalfOf", "-", "-", nodes.get(delegate), nodes.get(responsible));
        readAttributes(edge, optionalAttributes);
        addEdge(edge);
        
        if(activity != null)
        {
            edge = new Edge(id, "actedOnBehalfOf(Activity)", "-", "-", nodes.get(activity), nodes.get(responsible));
            readAttributes(edge, optionalAttributes);
            addEdge(edge);
        }
    }
    
    public void readInfluence(String[] attributes, String[] optionalAttributes){
        Edge edge;
        String id = null;
        String influencee = null;
        String influencer;

        id = getEdgeID(attributes[0], id);
        influencee = getEdge1stAttribute(attributes[0], influencee);
        influencer = attributes[1];   
        
        edge = new Edge(id, "wasInfluencedBy", "-", "-", nodes.get(influencer), nodes.get(influencee));
            
        readAttributes(edge, optionalAttributes);
        addEdge(edge);
    }
    
    public void readAlternate(String[] attributes, String[] optionalAttributes){
        alternateOrSpecializationOrMembership(attributes, optionalAttributes, "alternateOf");
    }
    
    public void readSpecialization(String[] attributes, String[] optionalAttributes){
        alternateOrSpecializationOrMembership(attributes, optionalAttributes, "specializationOf");
    }
    
    public void readMembership(String[] attributes, String[] optionalAttributes){
        alternateOrSpecializationOrMembership(attributes, optionalAttributes, "hadMember");
    }
    
    public void alternateOrSpecializationOrMembership(String[] attributes, String[] optionalAttributes, String type) {
        Edge edge;
        String id = null;
        String alternate1 = null;
        String alternate2;

        id = "Edge_" + edgeOptionalID;
        edgeOptionalID++;
        alternate2 = attributes[1];   
        
        edge = new Edge(id, type, "-", "-", nodes.get(alternate2), nodes.get(alternate1));
            
        readAttributes(edge, optionalAttributes);
        addEdge(edge);
    }
    public void readAttributes(GraphObject obj, String[] attributes) {
        //ex:  [prov:type='prov:Revision', ex:comment="a righteous derivation"]
        //ex: agent(ex:ag4, [ prov:type='prov:Person', ex:name="David" ])
//        attributes = attributes.replace("'", "");
//        attributes = attributes.replace("\"", "");
//        String[] attList = attributes.split(",");
        if(attributes != null)
        {
            for (String attList1 : attributes) {
                System.out.println("attList1: " + attList1);
                String[] att = attList1.split("=");
                if(att.length > 1)
                {
                    if(att[0].equalsIgnoreCase("prov:label"))
                    {
                        obj.setLabel(att[1]);
                    }
                    else if((obj instanceof Edge) && (att[0].contains("value")))
                    {
                        ((Edge)obj).setValue(att[1]);
                    }
                    else
                    {
                        Attribute optAtt = new Attribute(att[0], att[1]);
                        obj.addAttribute(optAtt);
                    }
                }
            }
        }
    }
    
    public String getEdgeID(String attribute, String id) {
        String[] att = attribute.split(";");
        if(att.length == 2) {
                id = att[0];
        }
        else {
            id = "Edge_" + edgeOptionalID;
            edgeOptionalID++;
        }
        return id;
    }
    public String getEdge1stAttribute(String attribute, String attr) {
        String[] att = attribute.split(";");
        if(att.length == 2) {
                attr = att[1];
        }
        else {
            attr = att[0];
        }
        return attr;
    }
}