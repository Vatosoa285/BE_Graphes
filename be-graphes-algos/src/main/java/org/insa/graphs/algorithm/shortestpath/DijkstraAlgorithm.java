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
        
      //Variables pour vérifier 
        double ancien_cout = 0;
        int nb_explores = 0 ;
 
        Graph graphe = data.getGraph();       		   
        BinaryHeap<Label> heap = new BinaryHeap<>(); 
        		
        Label[] label = new Label[graphe.size()]; 
        double infini = Double.POSITIVE_INFINITY;
     
        //associating a label to each node        
        for (int i = 0; i<graphe.size(); i++) {
        	label[i] = new Label(i, false, Double.POSITIVE_INFINITY,null);        	
        }
        
        //insertion du sommet origine dans la file de priorité        
       int origine = data.getOrigin().getId();      
       label[origine].setCost(0);
       heap.insert(label[origine]);
       
       // Notification à l'observateur de l'initialisation de la recherche (origine inséré dans le tas) 
       notifyOriginProcessed(data.getOrigin());
      
        //déroulement de l'algorithme    
       while (!label[data.getDestination().getId()].isMarque()) { 
    	    Label currentNodeLabel;
    		  	
    	   	try {
                currentNodeLabel = heap.deleteMin();
            } catch (EmptyPriorityQueueException e) {
                // Means that no new node was marked after the previous one
                // also the previous node was the only one visited but not marked
                // we've reached and marked all nodes that we can visit
                break;
            }
    	   	
    	   	int currentID = currentNodeLabel.getNode_associe(); 
    	   	Node currentNode = graphe.get(currentID);
    	   	
    	   	label[currentID].setMarque(true);
        	this.notifyNodeMarked(currentNode);
        	
        	if (currentNode==data.getDestination()) {
        		this.notifyDestinationReached(data.getDestination());
        	}
        	
        	else {
        		//Vérification que les coûts des labels marqués sont croissants
        		if(ancien_cout > currentNodeLabel.getCost()) { 
            		System.out.println("Les coûts des Labels marqués  sont croissants.");
            	}
            ancien_cout = currentNodeLabel.getCost();

    		//parcours des sommets successeurs du sommet courant 
            for (Arc successor : currentNode.getSuccessors()) {
                if(data.isAllowed(successor)) {
                	Node nextNode = successor.getDestination();
                	int nextNodeID = nextNode.getId();
            		Label nextLabel = label[nextNodeID];
            		
            		if (!nextLabel.isMarque()) {
            			//si ils ne sont pas déjà marqués, on calcule le nouveau cout
            			double new_cost = Double.min(nextLabel.getCost(), currentNodeLabel.getCost()+data.getCost(successor));
            			if (new_cost < nextLabel.getCost()) {
            				//si le nouveau cout est différent de l'ancien on le màj
            				if (label[nextNodeID].getCost()!=Double.POSITIVE_INFINITY) {
            					//y était déjà dans le tas donc on l'enlève pour le màj
            					label[nextNodeID].setCost(new_cost);
            					heap.remove(label[nextNodeID]);
            					heap.insert(nextLabel);
            					//on màj son père
                				label[nextNodeID].setPere(successor);
            				}
            				else {
            					label[nextNodeID].setCost(new_cost);
                				heap.insert(nextLabel);
                				label[nextNodeID].setPere(successor);
                				this.notifyNodeReached(nextNode);
            				}
            				nb_explores++;            					
            			}          		
            		}
                } 
            }
            System.out.println("Le nombre de successeurs explorés est : " + nb_explores + " pour " + currentNode.getNumberOfSuccessors() + " successeur(s).");
        	nb_explores = 0;     
        } 
       }
        	        	
                // TODO:
       //la destination est soit atteignable soit non atteignable 
       
       ShortestPathSolution solution = null;
       Node dest = data.getDestination(); 
       Label label_dest = label [dest.getId()]; 
       Arc arc = label_dest.getPere(); 
       ArrayList<Arc> arcs = new ArrayList<>();
       
       if (arc==null) { 
           solution = new ShortestPathSolution(data,AbstractSolution.Status.INFEASIBLE);
       } else {
           // Destination trouvée 
    	   
    	   //notification à l'observateur 
    	   while (arc != null) {
               arcs.add(arc);
               arc = label[arc.getOrigin().getId()].getPere();
           }

           // Reverse the path...
           Collections.reverse(arcs);

           // Create the final solution.
           solution = new ShortestPathSolution(data, AbstractSolution.Status.OPTIMAL, new Path(graphe, arcs));
       }
       
 
        
        return solution;
    }
}

