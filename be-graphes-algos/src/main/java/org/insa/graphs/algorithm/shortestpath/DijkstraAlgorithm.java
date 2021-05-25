package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.utils.BinaryHeap;
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


    //associating a label to each node        
    //pour que AStar marche il faut : 
    //1.s'assurer que ça marche pour les labels de la classe "Label" ou ceux de la classe "LabelStar"
    //2.bien mettre à jour les coûts, qui doivent mtn se baser sur TotalCost et non plus cost 
    protected void Init(Label[] Labels, Graph graph, ShortestPathData data) {
   	 for (int i = 0 ; i < graph.size(); i++) {
        	Labels[i] = new Label(i, false, Double.POSITIVE_INFINITY, null) ;
        }
   }
    
    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        
      //Variables pour vérifier 
        
        int nb_explores = 0 ;
 
        Graph graphe = data.getGraph();       		   
        BinaryHeap<Label> heap = new BinaryHeap<>(); 
        		
        Label[] label = new Label[graphe.size()]; 
        //initialisation de l'algo 
        this.Init(label, graphe, data);
       
        
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
                currentNodeLabel = heap.findMin();
            } catch (EmptyPriorityQueueException e) {
                // Means that no new node was marked after the previous one
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
           
            heap.remove(currentNodeLabel);
            
    		//parcours des sommets successeurs du sommet courant 
            for (Arc successor : currentNode.getSuccessors()) {
                if(data.isAllowed(successor)) {
                	Node nextNode = successor.getDestination();
                	int nextNodeID = nextNode.getId();
            		Label nextLabel = label[nextNodeID];
            		
            		if (!nextLabel.isMarque()) {
            			//comparaison des couts actuel et nouveau 
            			double new_cost = Double.min(nextLabel.getTotalCost(), label [successor.getOrigin().getId()].getCost()+  data.getCost(successor) + label[nextNodeID].getEstimatedCost());
            			
            			if (new_cost < nextLabel.getTotalCost()) {
            				double toUpdate = label[successor.getOrigin().getId()].getCost() + data.getCost(successor); 
            				//si le nouveau cout est différent de l'ancien on le met à jour
          					//y était déjà dans le tas donc on l'enlève pour le mettre à jour 
            			        label[nextNodeID].setCost(toUpdate);
            					//heap.remove(label[currentID]);
            					heap.insert(nextLabel);
            					//on met à jour son père
                				label[nextNodeID].setPere(successor);
                				
                				if (label[nextNodeID].getCost()!=Double.POSITIVE_INFINITY)
                					this.notifyNodeReached(nextNode);
            				
            				nb_explores++;            					
            			}          		
            		}
                } 
            }
            //System.out.println("Nbr de successeurs explorés : " + nb_explores + " pour " + currentNode.getNumberOfSuccessors() + " successeurs");
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
    	   double PathFoundByDijkstra=  label[data.getDestination().getId()].getTotalCost();
    	   //notification à l'observateur 
    	   while (arc != null) {
               arcs.add(arc);
               arc = label[arc.getOrigin().getId()].getPere();
           }

           // Reverse the path...
           Collections.reverse(arcs);

           // Create the final solution.
           solution = new ShortestPathSolution(data, AbstractSolution.Status.OPTIMAL, new Path(graphe, arcs));
           
           Path path = new Path(graphe, arcs);           
           //comparaison du résultat de Dijksra avec celui du path
          // System.out.println("Chemin valide ? " + solution.getPath().isValid());
           
           //Sont-ils égaux en distance ?
           if ((int)PathFoundByDijkstra ==(int)path.getLength()) {
           	System.out.println("Longueur calculée par Path = celle de Dijkstra" );
           }
       }
       
       
        return solution;
    }
}