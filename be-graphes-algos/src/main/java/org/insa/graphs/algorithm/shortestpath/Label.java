package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.List;

import org.insa.graphs.model.Arc;

public class Label implements Comparable<Label> {
	//attributs 
	
	//sommet courant 
	int sommet; 
	
	//booléen si cout min trouvé 
	boolean marque; 
	
	//valeur courante du plus court chemin depuis l'origine -> sommet
	Double cost; 
	
	//sommet précédent sur le chemin correspondant au plus court chemin courant 
	int pere;
	//arc prédécesseur à stoquer aussi 
	private Arc arcmin_precedent; 
	
	//associer un label à chq noeud 
	private int node_associe; 
	
	//méthodes : 
	public Double getCost() {
		return cost;
	}
	
	//associer un label à chq noeud 
	public int getNode_associe() {
		return node_associe;
	}

	
	public void setNode_associe(int node_associe) {
		this.node_associe = node_associe;
	}


	public Arc getArcmin_precedent() {
		return arcmin_precedent;
	}

	public void setArcmin_precedent(Arc arcmin_precedent) {
		this.arcmin_precedent = arcmin_precedent;
	}

	public int getSommet() {
		return sommet;
	}

	public void setSommet(int sommet) {
		this.sommet = sommet;
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
		
	//définir un compare to 
	

	@Override
	public int compareTo(Label arg2) {
		// TODO Auto-generated method stub
		return (int)(this.cost-arg2.cost); 
	}


	
				
} 
	
	
	
	
	
	
	

