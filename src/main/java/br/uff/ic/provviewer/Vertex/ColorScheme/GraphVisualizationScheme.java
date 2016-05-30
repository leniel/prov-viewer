/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Vertex.ColorScheme;

import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.graph.Vertex;
import java.awt.Color;
import java.awt.Paint;

/**
 *
 * @author Kohwalter
 */
public class GraphVisualizationScheme extends ColorScheme {

    public GraphVisualizationScheme(String attribute) {
        super(attribute);
    }

    public GraphVisualizationScheme(boolean isZeroWhite, boolean isInverted, String attribute, String empty, String g, String y, boolean l) {
        super(false, false, attribute, empty, g, y, l);
    }

    @Override
    public Paint Execute(Object v, final Variables variables) {
        if (((Vertex)v).getID().contains("(Merged)"))
            return new Color(200, 200, 200);
        else if (((Vertex)v).getID().contains(this.attribute))
            return new Color(0, 255, 0);
        else
//            return ((Vertex) v).getColor();
            return new Color(255, 0, 0);
    }
}
