package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.*;

public class LabelStar extends Label {
	
	private double estimatedCost;  //coût estimé depuis la destination 
	
	public LabelStar(int nodeId, boolean marque, double cost, Arc father, double estCost) {
		super(nodeId, marque, cost, father);
		// TODO Auto-generated constructor stub
		this.setEstimatedCost(estCost); 
	}

	//getter et setter pour estimatedCost 
	public double getEstimatedCost() {
		return estimatedCost;
	}

	public void setEstimatedCost(double estimatedCost) {
		this.estimatedCost = estimatedCost;
	}
		
    //méthode qui prend en compte le coût depuis l'origine + le coût estimé à la destination 
	public double getTotallCost() {
		return this.estimatedCost + super.getCost(); 
	}
	
}