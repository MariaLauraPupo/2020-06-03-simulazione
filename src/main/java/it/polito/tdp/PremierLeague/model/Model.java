package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;


public class Model {
	private SimpleDirectedWeightedGraph<Player, DefaultWeightedEdge> grafo;
	private PremierLeagueDAO dao;
	private Map<Integer,Player> idMap;
//	private List<Player> partenza;
	private Set<Player> soluzioneMigliore;
	private int titolaritaMaggiore;
	
	public Model() {
		dao = new PremierLeagueDAO();
		idMap = new HashMap<Integer, Player>();
		dao.listAllPlayers(idMap);
	//	this.partenza = dao.listAllPlayers();
	}
	public void creaGrafo(double x) {
		grafo = new SimpleDirectedWeightedGraph<Player, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//nodi
		Graphs.addAllVertices(grafo, dao.getCalciatoreByGoal(x,idMap));
		//archi
		for(Arco a: dao.getArchi(idMap)) {
			if(grafo.containsVertex(a.getP1()) && grafo.containsVertex(a.getP2()) ) {
	//			if(a.getPeso() < 0) {
					//arco che va da p2 a p1
				if(a.getPeso() > 0) {
				//	Graphs.addEdgeWithVertices(grafo, a.getP2(), a.getP1(), ((double) -1)*a.getPeso());
	//			}else if(a.getPeso() > 0) {

					Graphs.addEdgeWithVertices(grafo, a.getP1(), a.getP2(), a.getPeso());
				}
			}
		}
		System.out.println("vertici " + grafo.vertexSet().size());
     	System.out.println("archi " + grafo.edgeSet().size());
	}
	
	public int numeroVertici() {
		return this.grafo.vertexSet().size();
	}
	public int numeroArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public Player getTopPlayer() {
              int archiUscentiMax = 0;
              Player bestPlayer = null;
              for(Player p : this.grafo.vertexSet()) {
            	  if(grafo.outDegreeOf(p) > archiUscentiMax) {
            		  archiUscentiMax = grafo.outDegreeOf(p);
            		  bestPlayer = p;
            	  }
              }
              return bestPlayer;
	}
	
	public List<Player> getBattuti(Player topPlayer){
		List<Player> battuti = new LinkedList<Player>();
		for(DefaultWeightedEdge e : grafo.outgoingEdgesOf(topPlayer)) {
			battuti.add(grafo.getEdgeTarget(e));
		}
	//	Collections.sort(battuti);
		return battuti;
	}
	
	
	public Set<Player> calcolaSottoInsiemeGiocatori (int k){
		List<Player> parziale = new LinkedList<Player>();
		soluzioneMigliore = new HashSet<Player>();
		titolaritaMaggiore = 0;
		cerca(parziale,new ArrayList<Player>(),k);
		return soluzioneMigliore;
		
	}
	private void cerca(List<Player> parziale, /*int L*/ List<Player> giocatori, int k) {
		//casi terminale
		if(parziale.size() > k ) {
			return;
		}
		if(parziale.size() == k) {
			int titolarita = calcolaTitolarita(parziale);
			if(titolarita > titolaritaMaggiore) {
				soluzioneMigliore = new HashSet<>(parziale);
			}
			return;
		}
		
/*		if(L == partenza.size()) {
			return;
		}*/
		//finiti casi terminali
		
		//FATTO DA ME
		
	/*	parziale.add(partenza.get(L)); 
		partenza.removeAll(Graphs.successorListOf(grafo,partenza.get(L)));
		cerca(parziale,L+1, k);
		
		//backtrack
		parziale.remove(partenza.get(L));
		partenza.addAll(Graphs.successorListOf(grafo,partenza.get(L)));
		//faccio andare avanti la ricorsione supponendo di non aver aggiunto l'elemento
		cerca(parziale, L+1, k);*/
		
		//SOLUZIONE
		
		for(Player p : giocatori) {
			if(!parziale.contains(p)) {
				parziale.add(p);
				//i battuti non possono più essere considerati
				List<Player> remaingPlayers = new ArrayList<Player>(giocatori);
				cerca(parziale, remaingPlayers, k);
				parziale.remove(p);
			}
		}
		
		
	}
	
	
	private int calcolaTitolarita(List<Player> parziale) {
		int tit = 0;
		for(Player p : this.grafo.vertexSet()) {
			double val = 0.0;
			for(DefaultWeightedEdge e : this.grafo.incomingEdgesOf(p))
				val += this.grafo.getEdgeWeight(e); //incremento
			for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(p))
				val -= this.grafo.getEdgeWeight(e);
			tit += val;
		}
		return tit;
	}

}
