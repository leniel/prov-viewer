/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer;

/**
 * Abstract Subclass for Vertex named after PROV nomenclature and type
 * @author Kohwalter
 */
public abstract class EntityVertex extends Vertex {
    
    /**
     * Constructor
     * @param id This param is used by JUNG for collapsed vertices and tooltips.
     */
    public EntityVertex(String id) {
        super(id); 
    }    
}
