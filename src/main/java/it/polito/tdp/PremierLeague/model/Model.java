package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	private SimpleDirectedWeightedGraph<Player, DefaultWeightedEdge> grafo;
	private PremierLeagueDAO dao;
	private Map<Integer,Player> idMap;
	
	public Model() {
		dao = new PremierLeagueDAO();
		idMap = new HashMap<Integer, Player>();
		dao.listAllPlayers(idMap);
	}
	public void creaGrafo(double x) {
		grafo = new SimpleDirectedWeightedGraph<Player, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//nodi
		Graphs.addAllVertices(grafo, dao.getCalciatoreByGoal(x,idMap));
		//archi
	}

}
