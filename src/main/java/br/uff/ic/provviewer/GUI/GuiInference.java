/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.GUI;

import br.uff.ic.graphmatching.GraphMatching;
import br.uff.ic.provviewer.xmlToProlog.XMLConverter;
import br.uff.ic.utility.IO.BasePath;
import static br.uff.ic.provviewer.GraphFrame.StatusFilterBox;
import br.uff.ic.provviewer.Inference.AutomaticInference;
import br.uff.ic.provviewer.Inference.PrologInference;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.AttributeErrorMargin;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import br.uff.ic.utility.graphgenerator.ClusteringEvaluator;
import br.uff.ic.utility.graphgenerator.Main;
import br.uff.ic.utility.graphgenerator.OracleGraph;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JToggleButton;

/**
 * Class responsible for the PROLOG implementation
 *
 * @author Kohwalter
 */
public class GuiInference {

    /**
     * Method to enable the prolog functions
     *
     * @param prolog is the prolog implementation for the automatic inference
     * @param prologIsInitialized is the boolean to check if the prolog can be
     * activated
     * @param InitPrologButton is the boolean to check if the prolog can be
     * activated
     */
    public static void InitializeProlog(PrologInference prolog, boolean prologIsInitialized, JToggleButton InitPrologButton) {
        if (InitPrologButton.isSelected() && !prologIsInitialized) {
            prologIsInitialized = true;
            prolog.Init();
        }
    }

    /**
     * Method to generate the prolog facts using the XML-Inference
     *
     * @param initialGraph
     * @param file
     * @param demo
     */
    public static void GeneratePrologFacts(boolean initialGraph, File file, String demo) {
        if (initialGraph) {
            file = new File(BasePath.getBasePathForClass(GuiInference.class) + demo);
            initialGraph = false;
        }
        ConvertXML(file);
        System.out.println("Finished Converting to Prolog");
    }

    /**
     * Method to convert the XML to prolog facts
     *
     * @param file the XML file for conversion
     */
    private static void ConvertXML(File file) {
        XMLConverter xmlConv = new XMLConverter();
        xmlConv.ConvertXMLtoProlog(file);
    }

    /**
     * Method to apply the similarity collapse in the graph
     *
     * @param InitPrologButton is the boolean to check if the prolog is active
     * @param prolog is the prolog implementation
     * @param variables
     * @param edgeType is the type of edge used for the collapse
     */
    public static void SimilarityCollapse(boolean InitPrologButton, PrologInference prolog, Variables variables, String edgeType) {
        if (InitPrologButton) {
            System.out.println("Starting Prolog Inference: " + edgeType);
            GuiButtons.Reset(variables);
            String list;
            list = prolog.QueryCollapse((String) StatusFilterBox.getSelectedItem(), edgeType);
            System.out.println("Collapsing...");
            variables.collapser.CollapseIrrelevant(variables, list);
            System.out.println("Finished Collapsing");
        } else {
            GuiButtons.Reset(variables);
            ArrayList<ConcurrentHashMap<String, Object>> list = ColorSchemeCollapse((String) StatusFilterBox.getSelectedItem(), variables.graph, false, true);
            variables.collapser.CollapseIrrelevant(variables, printCollapseGroups(list));
            System.out.println("Finished Collapsing");
        }
    }
    
    public static String printCollapseGroups(ArrayList<ConcurrentHashMap<String, Object>> collapseGroups) {
        String collapseList = "";
        for (ConcurrentHashMap<String, Object> subGraph : collapseGroups) {
            if (subGraph.size() > 0) {
                for (Object v1 : subGraph.values()) {
                    String id1 = ((Vertex) v1).getID();
                    collapseList += "," + id1;
                }
                collapseList += " ";
            }
        }
        return collapseList;
    }

    public static ArrayList<ConcurrentHashMap<String, Object>> ColorSchemeCollapse(String attribute, DirectedGraph<Object, Edge> graph, boolean updateError, boolean verifyWithinCluster) {
        // -----------------------------
        // Standard Deviation
        // -----------------------------
        float std = Utils.std(graph.getVertices(), attribute);
//        System.out.println("STD = " + std);

        // -----------------------------
        // Similarity configuration
        // -----------------------------
        float similarityThreshold = 0.5f;
        String defaultError = "0";
        Map<String, AttributeErrorMargin> restrictionList = new HashMap<>();
        AttributeErrorMargin epsilon;
        epsilon = new AttributeErrorMargin(attribute, String.valueOf(std));
        restrictionList.put(attribute, epsilon);
        GraphMatching combiner = new GraphMatching(restrictionList, similarityThreshold, defaultError, 0);

        // -----------------------------
        // Return Collapse Clusters
        // -----------------------------
//        long startTime = System.currentTimeMillis();
//        System.out.println(": L = " + collapseList);
        AutomaticInference infer = new AutomaticInference(7, 4, 4);
        ArrayList<ConcurrentHashMap<String, Object>> clusters =  infer.cluster(graph, combiner, updateError, verifyWithinCluster);
//        String clusters =  AutomaticInference.dbscan(variables, combiner);
        
//        long stopTime = System.currentTimeMillis();
//        long elapsedTime = stopTime - startTime;
//        System.out.println("elapsedTime: " + elapsedTime);
        return clusters;
    }

}
