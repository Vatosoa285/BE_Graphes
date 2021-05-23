package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.model.*;


public class AStarAlgorithm extends DijkstraAlgorithm {
	

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    protected void Init(Label[] Labels, Graph graph, ShortestPathData data) {
    	//si PCC en temps
    	if (data.getMode() == Mode.TIME) {
    		double vitesse = (double) data.getGraph().getGraphInformation().getMaximumSpeed()/3.6 ; 
    		//ci dessus la divisons par 3.6 sert pour la conversion km/h -> m/s 
    		for (int i = 0 ; i < graph.size(); i++) {
        		//distance à vol d'oiseau = distance entre 2 points
        		double distance = Point.distance(graph.get(i).getPoint(), data.getDestination().getPoint());
            	Labels[i] = new LabelStar(i, false, Double.POSITIVE_INFINITY, null, distance/vitesse) ;
            }     
    	}
    	//si PCC distance
    	else {
	    	for (int i = 0 ; i < graph.size(); i++) {
	    		
	    		double distance = Point.distance(graph.get(i).getPoint(), data.getDestination().getPoint());
	        	Labels[i] = new LabelStar(i, false, Double.POSITIVE_INFINITY, null, distance) ;
	        } 
    	}
   }
}
