/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.graphmatching;

import br.uff.ic.provviewer.GUI.GuiRun;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.GraphAttribute;
import br.uff.ic.utility.IO.BasePath;
import br.uff.ic.utility.IO.UnityReader;
import br.uff.ic.utility.IO.XMLWriter;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import static cern.clhep.Units.g;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kohwalter
 */
public class MatcherTest {
    
    public MatcherTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of Matching method, of class Matcher.
     * @throws java.io.FileNotFoundException
     */
    @Test
    public void testMatching() throws FileNotFoundException, URISyntaxException, IOException {
        System.out.println("Matching");
        DirectedGraph<Vertex, Edge> graph_01 = graphFile("Angry_Robots_Merge_01.xml");
        DirectedGraph<Vertex, Edge> graph_02 = graphFile("Angry_Robots_Merge_01.xml");
        Map<String, GraphAttribute> restrictionList = restriction();
        double similarityThreshold = 0.9;
        Matcher instance = new Matcher();
//        DirectedGraph<Vertex, Edge> expResult = null;
        DirectedGraph<Vertex, Edge> result = instance.Matching(graph_01, graph_02, restrictionList, similarityThreshold);
        
        String resultEdges = "";
        for (Edge e : result.getEdges()) {
            resultEdges += "(" + e.getID() + ") " + e.getTarget().getID() + "->" + e.getSource().getID() + " || ";
        };
        
        String resultVertices = "";
        for (Vertex v : result.getVertices()) {
            resultVertices += v.getID() + " || ";
        };
        
        System.out.println("Edges: " + resultEdges);
        System.out.println("Vertices: " + resultVertices);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
        
        XMLWriter xmlWriter = new XMLWriter(result.getVertices(), result.getEdges());
        xmlWriter.saveToXML("XML_Writer_Test");
    }
    
    private DirectedGraph<Vertex, Edge> graphFile(String path) throws URISyntaxException, IOException {
        File f = new File(BasePath.getBasePathForClass(GuiRun.class) + File.separator + "Graph" + File.separator + path);
        UnityReader file = new UnityReader(f);
        file.readFile();
        DirectedGraph<Vertex, Edge> g = new DirectedSparseMultigraph<Vertex, Edge>();
        
        for (Edge edge : file.getEdges()) {
                g.addEdge(edge, edge.getSource(), edge.getTarget());
            }
        return g;
    }
    
    private DirectedGraph<Vertex, Edge> graph01() {
//        Map<String, GraphAttribute> restrictionList = new HashMap<String, GraphAttribute>();
        Collection<Edge> edges = new ArrayList<Edge>();;
        Vertex v1;
        Vertex v2;
        Vertex v3;
        Vertex v4;
        Edge edge;
        
        v1 = new ActivityVertex("A", "test", "0");
        GraphAttribute av1 = new GraphAttribute("a1", "2");
        v1.addAttribute(av1);
        
        v2 = new ActivityVertex("B", "test", "0");
        GraphAttribute av2 = new GraphAttribute("a1", "4");
        v2.addAttribute(av2);
        
        
        v3 = new ActivityVertex("C", "test", "0");
        GraphAttribute av3 = new GraphAttribute("a1", "6");
        v3.addAttribute(av3);
        
        v4 = new ActivityVertex("D", "test", "0");
        GraphAttribute av4 = new GraphAttribute("a1", "8");
        v4.addAttribute(av4);
        
        edge = new Edge("edge01_g1", "Test", "Testing", "0", v1, v2);
        edges.add(edge);
        
        edge = new Edge("edge02_g1", "Test", "Testing", "0", v2, v3);
        edges.add(edge);
        
        edge = new Edge("edge03_g1", "Test", "Testing", "0", v3, v4);
        edges.add(edge);
        
        DirectedGraph<Vertex, Edge> graph = new DirectedSparseMultigraph<Vertex, Edge>();
        for (Edge e : edges) {
            graph.addEdge(e, e.getSource(), e.getTarget());
        } 
        
        return graph;
    }
    
    private DirectedGraph<Vertex, Edge> graph02() {
        Collection<Edge> edges = new ArrayList<Edge>();
        Vertex v1;
        Vertex v2;
        Vertex v3;
        Vertex v4;
        Edge edge;
        
        v1 = new ActivityVertex("E", "test", "0");
        GraphAttribute av1 = new GraphAttribute("a1", "2");
        v1.addAttribute(av1);
        
        v2 = new ActivityVertex("F", "test", "0");
        GraphAttribute av2 = new GraphAttribute("a1", "4");
        v2.addAttribute(av2);
        
        
        v3 = new ActivityVertex("G", "test", "0");
        GraphAttribute av3 = new GraphAttribute("a1", "7");
        v3.addAttribute(av3);
        
        v4 = new ActivityVertex("H", "test", "0");
        GraphAttribute av4 = new GraphAttribute("a1", "9");
        v4.addAttribute(av4);
        
        edge = new Edge("edge01_g2", "Test", "Testing", "0", v1, v2);
        edges.add(edge);
        
        edge = new Edge("edge02_g2", "Test", "Testing", "0", v2, v3);
        edges.add(edge);
        
        edge = new Edge("edge03_g2", "Test", "Testing", "0", v3, v4);
        edges.add(edge);
        
        DirectedGraph<Vertex, Edge> graph = new DirectedSparseMultigraph<Vertex, Edge>();
        for (Edge e : edges) {
            graph.addEdge(e, e.getSource(), e.getTarget());
        } 
        
        return graph;
    }
    
    private Map<String, GraphAttribute> restriction(){
        Map<String, GraphAttribute> restrictionList = new HashMap<String, GraphAttribute>();
        GraphAttribute epsilon = new GraphAttribute("a1", "0");
        restrictionList.put("a1", epsilon);
        return restrictionList;
    }
}