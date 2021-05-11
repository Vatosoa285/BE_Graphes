package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;   
import java.util.List;

import org.insa.graphs.model.Arc;

public class Label implements Comparable<Label> {
	//attributs 
		
	//booléen si cout min trouvé 
	protected boolean marque; 	
	//valeur courante du plus court chemin depuis l'origine -> sommet
	protected double cost; 	
	//sommet précédent sur le chemin correspondant au plus court chemin courant 
	int pere;
	//arc prédécesseur à stoquer aussi 		
	//associer un label à chq noeud 
	private int node_associe; 
	
	//constructeur 
	
	public Label(int nodeId, boolean marque, double cost, int father) {
		this.node_associe = nodeId ; 
		this.marque = marque ; 
		this.cost = cost ; 
		this.pere = father ; } 
	
	
	//méthodes, getters et setters : 
	public Double getCost() {
		return cost;
	}
	
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	//associer un label à chq noeud 
	public int getNode_associe() {
		return node_associe;
	}

	
	public void setNode_associe(int node_associe) {
		this.node_associe = node_associe;
	}


	public boolean isMarque() {
		return marque;
	}

	public void setMarque(boolean marque) {
		this.marque = marque;
	}

	public int getPere() {
		return pere;
	}

	public void setPere(int pere) {
		this.pere = pere;
	}		
		
	// compare to 
	
	@Override
	public int compareTo(Label arg2) {
		// TODO Auto-generated method stub
		return (int)(this.cost-arg2.cost); 
	}


	
				
} 
	
	
	
	
	
	
	

