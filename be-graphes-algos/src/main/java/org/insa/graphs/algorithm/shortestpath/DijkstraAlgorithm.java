package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.List;

import org.insa.graphs.algorithm.utils.BinaryHeap; //à voir si utile
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);             
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        Graph graphe = data.getGraph();       		
        BinaryHeap<Label> tas = new BinaryHeap<>(); 
        		
        Label[] label = new Label[graphe.size()]; 
        float infini = Float.POSITIVE_INFINITY;
     
        //association d'un label à chq sommet        
        for (int i = 0; i<=graphe.size()-1; i++) {
        	label[i].sommet=i; 
        	label[i].cost= infini; 
        	label[i].pere =0;      	       		       
        	label[i].marque =false ;        	
        }
        
        //insertion du sommet origine dans la file de priorité 
       int origine = data.getOrigin().getId();     
       label[origine].cost = 0; //différence avec setteur 
       tas.insert(label[origine]);
      
        //déroulement de l'algorithme 
       int pointeur=0; 
       
       
       while (pointeur<=graphe.size()-1) { 
    	   while (label[pointeur].marque==false)   { //on sort de la boucle quand tous les sommets seront marqués 
    		   Comparable x = tas.findMin(); 
    		   label[(int) x].marque = true; 
    		   
    		   //parcours des successeurs de x 
    		
    		   
    		   
    		   
    	   }
    	   
       }
      
        
        
        // TODO:
        return solution;
    }

}
