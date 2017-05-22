/*
 * The MIT License
 *
 * Copyright 2017 Kohwalter.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.uff.ic.provviewer.Layout;

import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Template for a temporal graph layout. Lines represent each agent and his
 * activities. Columns represent passage of time
 * @deprecated 
 * @author Kohwalter
 * @param <V> JUNG's V (Vertex) type
 * @param <E> JUNG's E (Edge) type
 */
public class TimelineGraphs_Layout<V, E> extends ProvViewerLayout<V, E> {

    public TimelineGraphs_Layout(Graph<V, E> g, Variables variables) {
        super(g, variables);
    }

    @Override
    public void reset() {
        doInit();
    }

    @Override
    public void initialize() {
        doInit();
    }

    /**
     * Initialize layout
     */
    private void doInit() {
        String x_att = variables.layout_attribute_X;
        x_att = Utils.removeMinusSign(x_att);
        
        Collection<String> values = Utils.DetectAllPossibleValuesFromAttribute(variables.graph.getVertices(), "GraphFile");
        ArrayList<Integer> counts = new ArrayList<>();
        for(int i = 0; i < values.size(); i++) {
            counts.add(0);
        }
        setVertexOrder(Utils.getVertexAttributeComparator(x_att));
        int i = 0;
        int agentY = 0;
        double yPos = 0;
        double xPos = 0;
        int yGraphOffset = 0;
        int entityXPos = (int) (vertex_ordered_list.size() * 0.5 - (entity_ordered_list.size() * 0.5));
        int scale = (int) (2 * variables.config.vertexSize * values.size() * 0.25);
        entityXPos = entityXPos * scale;
        for (V v : vertex_ordered_list) {
            Point2D coord = transform(v);
            int j = 0;
            for(String g : values) {
                if(((Vertex)v).getAttributeValue("GraphFile").contains(g)) {
                    yGraphOffset = (int) (variables.config.vertexSize * 0.25 * j);
                    break;
                }
                j++;
            }
            
            // Sync the X position if we have a common vertex node for multiple graphs
            String[] graphFiles = ((Vertex)v).getAttributeValues("GraphFile");
            if (graphFiles.length > 1) {
                // Find the max position so we can sync the rest
                int max = 0;
                for (String gFile : graphFiles) {
                    int w = 0;
                    for(String g : values) {
                        if(((Vertex)v).getAttributeValue("GraphFile").contains(gFile)) {
                            max = Math.max(counts.get(w), max);
                            break;
                        }
                        w++;
                    }
                    
                }
                // Sync the positions with the previous found max
                for (String gFile : graphFiles) {
                    int w = 0;
                    for(String g : values) {
                        int z = 0;
                        if(((Vertex)v).getAttributeValue("GraphFile").contains(gFile)) {
                            counts.set(z, max);
                            break;
                        }
                        z++;
                    }
                }
            }
            
            if (v instanceof AgentVertex) {
                yPos = agentY;
                agentY = agentY + scale;
                i = counts.get(j) + scale;
                counts.set(j, counts.get(j) + 1);
                xPos = i;
            } else if (v instanceof ActivityVertex) {
                if (graph.getOutEdges(v) != null) {
                    for (E neighbor : graph.getOutEdges(v)) {
                        //if the edge link to an Agent-node
                        if (graph.getDest(neighbor) instanceof AgentVertex) {
                            Point2D agentPos = transform(graph.getDest(neighbor));
                            yPos = agentPos.getY();
                        }
                        i = counts.get(j) + scale;
                        counts.set(j, counts.get(j) + 1);
                        xPos = i;
                    }
                }
            } else {
                yPos = 0;
                i = counts.get(j) + scale;
                counts.set(j, counts.get(j) + 1);
                xPos = i;
            }
            yPos = yPos + yGraphOffset;
            coord.setLocation(xPos, yPos);
        }
        for(V v : entity_ordered_list) {
            Point2D coord = transform(v);
            // Position then in the middle
            xPos = entityXPos;
            entityXPos = entityXPos + scale;
            yPos = -10 * scale;
            coord.setLocation(xPos, yPos);
        }
    }

    /**
     * This one is an incremental visualization.
     *
     * @return true
     */
    public boolean isIncremental() {
        return true;
    }

    /**
     * Returns true once the current iteration has passed the maximum count,
     * <tt>MAX_ITERATIONS</tt>.
     *
     * @return true
     */
    @Override
    public boolean done() {
//        if (currentIteration > mMaxIterations || temperature < 1.0/max_dimension)
//        {
//            return true;
//        }
//        return false;
        return true;
    }

    @Override
    public void step() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
}
