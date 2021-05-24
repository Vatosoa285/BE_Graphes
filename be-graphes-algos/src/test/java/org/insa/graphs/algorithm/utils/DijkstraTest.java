package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.assertEquals;

import java.io.*;
import org.insa.graphs.algorithm.*;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.shortestpath.*;
import org.insa.graphs.model.*;
import org.insa.graphs.model.io.*;
import org.junit.*;


public class DijkstraTest {
	
	//choix de différents types de cartes 
	private static Graph map1;	// carte carré 
	private static Graph map2;	// carte Insa
	private static Graph map3;  // Carte Belgique 
	
	
	@BeforeClass
    public static void init() {
        final String map1_link = "/home/razafini/Bureau/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/carre.mapgr"; 
        final String map2_link = "/home/razafini/Bureau/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr"; 
        final String map3_link = "/home/razafini/Bureau/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/belgium.mapgr"; 
        
        try {
        	final GraphReader reader1 = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(map1_link))));
        	map1 = reader1.read();
        	reader1.close();
        	final GraphReader reader2 = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(map2_link))));
        	map2 = reader2.read();
        	reader1.close();
        	final GraphReader reader3 = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(map3_link))));
        	map2 = reader3.read();
        	
        	reader2.close();
        	reader3.close(); 
        }
        catch(Exception e){
        	System.out.println("Erreur de lecture de la carte.");
        }
	}
        
       //Test 1 : sur la carte carré : petit scénario 
	   //les 3 algos doivent trouver approximativement les mêmes résultats en distance et en temps si les points de départ et de destination de sont pas confondus
	   //pour des points de départ et de destination confondus, les 3 algos ne trouvent pas de chemins 
	    
	   int tab_dep[] = {23, 12, 21, 18}; 
	   int tab_dest[]= {23, 18, 20, 16}; 
	   
	   @Test
	   public void AllShouldBehaveSame() {
		    for  (int dep : tab_dep) {
		    	for (int dest : tab_dest) {
		    		ShortestPathData data = new ShortestPathData(map1, map1.get(dep), map1.get(dest), ArcInspectorFactory.getAllFilters().get(0));
		    		ShortestPathAlgorithm Dijkstra = new DijkstraAlgorithm(data);
		    		AStarAlgorithm AStar = new AStarAlgorithm(data);
		    		ShortestPathAlgorithm Bellman = new BellmanFordAlgorithm(data);
		    		// départ et destination confondus 
		    		if (dep == dest) {
		    			assertEquals(Dijkstra.run().getStatus(), Status.INFEASIBLE);
		    			assertEquals(AStar.run().getStatus(), Status.INFEASIBLE);	
		    			assertEquals(Bellman.run().getStatus(), Status.INFEASIBLE);
		    		}
		    		// départ et destination non confondus et PCC qui existe 
		    		else {
		    			//on doit approximativement trouver les mêmes résultats 
		    			ShortestPathSolution BellmanSolution = Bellman.run();
		    			//distances trouvées : 
		    			assertEquals(Dijkstra.run().getPath().getLength(), BellmanSolution.getPath().getLength(), 0.1 );	
		    			assertEquals(AStar.run().getPath().getLength(), Dijkstra.run().getPath().getLength(), 0.9);	
		                //temps mis : 
		    			assertEquals( (int)(Dijkstra.run().getPath().getMinimumTravelTime()),(int) ( BellmanSolution.getPath().getMinimumTravelTime()), 0.);	
		    			assertEquals((int) (AStar.run().getPath().getMinimumTravelTime()), (int) (BellmanSolution.getPath().getMinimumTravelTime()), 0.);
		    		}
		    	    
		    	}
		    
		    }		
		   
		}
	
	//Test 2 : sur la carte insa.mapgr :  
	 //*  //cas de chemin inexistant 
	  /* @Test	  
	   public void UnexistingPathAllRoadAllowed() {
		ShortestPathData data = new ShortestPathData(map2, map2.get(1036),map2.get(1036), ArcInspectorFactory.getAllFilters().get(0));
   		ShortestPathAlgorithm Dijkstra = new DijkstraAlgorithm(data);
   		AStarAlgorithm AStar = new AStarAlgorithm(data);
   		ShortestPathAlgorithm Bellman = new BellmanFordAlgorithm(data);
  		
   		assertEquals(Dijkstra.run().getStatus(), Status.INFEASIBLE);
		assertEquals(AStar.run().getStatus(), Status.INFEASIBLE);	
		assertEquals(Bellman.run().getStatus(), Status.INFEASIBLE);
		   
	   } */
	  
	   //cas du trajet insa-rangueil, qui admet un PCC optimal 
	 /* @Test
	        public void InsaRangueilDiverseMode() {
		   // int modes[] = {0,1,2,3}; 
		    // 0 PCC en distance, allroadsallowed
		    // 1 PCC en distance, only roads for cars 
		    // 2 PCC en temps, allroadsallowed
		    // 3 PCC en temps, only roads for cars 
		   // for (int mode : modes ) {
			ShortestPathData data = new ShortestPathData(map2, map2.get(552), map2.get(254), ArcInspectorFactory.getAllFilters().get(0));
	   		ShortestPathAlgorithm Dijkstra = new DijkstraAlgorithm(data);
	   		AStarAlgorithm AStar = new AStarAlgorithm(data);
	   		ShortestPathAlgorithm Bellman = new BellmanFordAlgorithm(data);
	   		ShortestPathSolution BellmanSolution = Bellman.run();
	   		
	   		//vérification de l'optimalité 
	   		/*assertEquals(Dijkstra.run().getStatus(), Status.OPTIMAL);
			assertEquals(AStar.run().getStatus(), Status.OPTIMAL);	
			assertEquals(BellmanSolution.getStatus(), Status.OPTIMAL);*/
			
			//vérification en distance et en temps
			/*(Dijkstra.run().getPath().getLength(), BellmanSolution.getPath().getLength(), 0.01 );	
			assertEquals(AStar.run().getPath().getLength(), BellmanSolution.getPath().getLength(), 0.01);
			
			assertEquals( Dijkstra.run().getPath().getMinimumTravelTime(), BellmanSolution.getPath().getMinimumTravelTime(), 0.01);	
			assertEquals(AStar.run().getPath().getMinimumTravelTime(), BellmanSolution.getPath().getMinimumTravelTime(), 0.01);		   
		   
	   } */
	   
	   //les tests précédents se faisant sur des petis scénarios( cartes de superficie moyenne), si ils marchent, on a
	   //la confirmation que  les algos de Dijkstra et Astar implémentés marchent 
	   //pour affiner la vérification du fait que Dijktra et Astar ont les mêmes comportements, on peut tester sur une carte de plus grande superfice 
	   //on ne pourra pas s'appuyer sur Dijkstra car balayant toute la superficie de la carte, cela risque de prendre trop de temps 
	   //Dijktra et Astar doivent trouver les mêmes solution sur ce trajet de la map3 : belgique.mapgr
	   
	   @Test     
	  public void TestDijkstraAstarOnWideMap() {
		    //int modes[] = {0,1,2,3}; 
		    // 0 PCC en distance, allroadsallowed
		    // 1 PCC en distance, only roads for cars 
		    // 2 PCC en temps, allroadsallowed
		    // 3 PCC en temps, only roads for cars 
		    
		    //for (int mode : modes ) {
			ShortestPathData data = new ShortestPathData(map1, map1.get(17), map1.get(1), ArcInspectorFactory.getAllFilters().get(0));
	   		ShortestPathAlgorithm Dijkstra = new DijkstraAlgorithm(data);
	   		AStarAlgorithm AStar = new AStarAlgorithm(data);
	   		
	   		//vérification de l'optimalité 
	   		/*assertEquals(Dijkstra.run().getStatus(), Status.OPTIMAL);
			assertEquals(AStar.run().getStatus(), Status.OPTIMAL);	*/		
			
			//vérification en distance et en temps
			assertEquals(Dijkstra.run().getPath().getLength(), AStar.run().getPath().getLength(), 0.01 );				
			assertEquals( Dijkstra.run().getPath().getMinimumTravelTime(), AStar.run().getPath().getMinimumTravelTime(), 0.01); 	   
		  // }
	   } 
	//
}