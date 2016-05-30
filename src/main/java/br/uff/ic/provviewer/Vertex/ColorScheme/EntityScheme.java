/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Vertex.ColorScheme;

import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.Vertex;
import java.awt.Paint;

/**
 *
 * @author Kohwalter
 */
public class EntityScheme extends ColorScheme {
    
    public EntityScheme(boolean isZeroWhite, boolean isInverted, String attribute, String empty, String g, String y, boolean l) {
        super(isZeroWhite, isInverted, attribute, empty, g, y, l);
    }

    @Override
    public Paint Execute(Object v, final Variables variables) {
        
        ComputeValue(variables.graph, false);
        if (v instanceof EntityVertex) {
            return this.GetMinMaxColor(v);
        }
        return ((Vertex) v).getColor();
    }
}
