package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;

public class Label implements Comparable<Label> {
	//attributs 
		
	//booléen si cout min trouvé 
	protected boolean marque; 	
	//valeur courante du plus court chemin depuis l'origine -> sommet
	protected double cost; 	
	//sommet précédent sur le chemin correspondant au plus court chemin courant (sauvegardé en tant qu'arc) 
	Arc pere;	
	//associer un label à chq noeud 
	private int node_associe; 
	
	//constructeur 
	
	public Label(int nodeId, boolean marque, double cost, Arc father) {
		this.node_associe = nodeId ; 
		this.marque = marque ; 
		this.cost = cost ; 
		this.pere = father ; } 
	
	
	// getters et setter : 
	public Double getCost() {
		return cost;
	}
	
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	public Arc getPere() {
		return pere;
	}
	
	public void setPere(Arc F) {
		this.pere = F;
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

	//autres méthodes : 
		
	
	public double getEstimatedCost() { //cette méthode est redéfinie dans LabelStar 
		return 0; 
	}
	
	public double getTotalCost() { //cette méthode est redéfinie dans LabelStar,,
		return this.cost;
	}
		
	// compare to : la version initiale établissait un ordre croissant des coûts depuis l'origine uniquement (cost)
	//pour Astar, on veut étendre celui-ci pour ordonner les labels suivant un ordre croissant en se basant sur le TotalCost 
	
	
	@Override
	public int compareTo(Label arg2) {
		// TODO Auto-generated method stub	
		
		//return (int)(this.cost-arg2.cost); // la version d'avant 
		
		int ecart = (int) (this.getTotalCost()-arg2.getTotalCost()); 
		
		if (ecart == 0 ) { //si 2 labels ont le même coût total, on se basera sur le coût estimé à la destination 
             ecart= (int) (this.getEstimatedCost() - arg2.getEstimatedCost()); 
		}
		
		return ecart; 
		
	} 
   					
} 
	
	
	
	
	
	
	

