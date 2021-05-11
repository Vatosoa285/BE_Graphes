package org.insa.graphs.algorithm.shortestpath;

//classes import
import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.utils.BinaryHeap;
//exceptions import 
import org.insa.graphs.algorithm.utils.ElementNotFoundException;
import org.insa.graphs.algorithm.utils.EmptyPriorityQueueException;

import org.insa.graphs.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections; 

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
        BinaryHeap<Label> heap = new BinaryHeap<>(); 
        		
        Label[] label = new Label[graphe.size()]; 
        double infini = Double.POSITIVE_INFINITY;
     
        //association d'un label à chq sommet        
        for (int i = 0; i<graphe.size(); i++) {
        	label[i].sommet=i; 
        	label[i].cost= infini; 
        	label[i].pere =0;      	       		       
        	label[i].marque =false ;        	
        }
        
        //insertion du sommet origine dans la file de priorité 
        
       int origine = data.getOrigin().getId();      
       label[origine].setCost(0);;
       heap.insert(label[origine]);
       
       // Notification à l'observateur de l'initialisation de la recherche (origine inséré dans le tas) 
       notifyOriginProcessed(data.getOrigin());
      
        //déroulement de l'algorithme    
       while (!label[data.getDestination().getId()].isMarque()) { 
    	    Label currentNodeLabel;
    		  	
    	   	try {
                currentNodeLabel = heap.findMin();
            } catch (EmptyPriorityQueueException e) {
                // Means that no new node was marked after the previous one
                // And the previous node was the only one visited but not marked
                // Means we reached and mark all nodes that we can visit
                break;
            }
    	   	
    	   	//marquage des nodes 
    	    try {
                heap.remove(currentNodeLabel);
            } catch (ElementNotFoundException ignored) { }
            label[currentNodeLabel.getNode_associe()].setMarque(true);

    		//parcours des sommets successeurs  
            for (Arc successor : graphe.get(currentNodeLabel.getNode_associe()).getSuccessors()) {
                if(!data.isAllowed(successor)) continue;
                int nextNodeId = successor.getDestination().getId();
                if (!label[nextNodeId].isMarque()) {
                    double w = data.getCost(successor);
                    double oldDistance = label[nextNodeId].getCost();
                    double newDistance = label[currentNodeLabel.getNode_associe()].getCost() + w;

                    if (Double.isInfinite(oldDistance) && Double.isFinite(newDistance)) {
                        notifyNodeReached(successor.getDestination());
                    }
            
                    // Vérifie si les nouvelles distances sont plus courts, si oui, mettre à jour 
                    if (newDistance < oldDistance) {
                        label[nextNodeId].setCost(newDistance);
                        label[nextNodeId].setPere(currentNodeLabel.getNode_associe());

                        try {
                            // mise à jour du node dans le tas
                            heap.remove(label[nextNodeId]);
                            heap.insert(label[nextNodeId]);
                        } catch (ElementNotFoundException e) {
                            // si le noeud n'est pas dans le tas, on l'insère 
                            heap.insert(label[nextNodeId]);
                        }
                    }
    	   }
    	   
            }
      
       }
        
        // TODO:
       
       if (!label[data.getDestination().getId()].isMarque()) {
           solution = new ShortestPathSolution(data, AbstractSolution.Status.INFEASIBLE);
       } else {
           // Destination trouvée 
    	   
    	   //notification à l'observateur 
    	      	   
           notifyDestinationReached(data.getDestination());

           ArrayList<Node> pathNodes = new ArrayList<>();
           pathNodes.add(data.getDestination());
           Node node = data.getDestination();
           while (!node.equals(data.getOrigin())) {
               Node fatherNode = graphe.getNodes().get(label[node.getId()].getPere());
               pathNodes.add(fatherNode);
               node = fatherNode;
           }
           Collections.reverse(pathNodes);

           // Create the final solution.
           Path solutionPath;
           if (data.getMode().equals(AbstractInputData.Mode.LENGTH)) {
               solutionPath = Path.createShortestPathFromNodes(graphe, pathNodes);
           } else {
               solutionPath = Path.createFastestPathFromNodes(graphe, pathNodes);
           }

           solution = new ShortestPathSolution(data, AbstractSolution.Status.OPTIMAL, solutionPath);
       }
        
        return solution;
    }

}
