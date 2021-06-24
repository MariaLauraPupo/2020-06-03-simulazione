package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public void listAllPlayers(Map<Integer,Player> map){
		String sql = "SELECT * FROM Players";
//		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!map.containsKey(res.getInt("PlayerID"))) {
				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				map.put(res.getInt("PlayerID"), player);
				}
//				result.add(player);
			}
			conn.close();
//			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
//			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Player> getCalciatoreByGoal(double mediaGoal, Map<Integer,Player> map) {
		String sql = "SELECT p.PlayerID AS pId, p.Name AS nome "
				+ "FROM players p, actions a  "
				+ "WHERE p.PlayerID = a.PlayerID "
				+ "GROUP BY p.PlayerID, p.Name  "
				+ "HAVING AVG (a.Goals) > ?";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, mediaGoal);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(map.get(res.getInt("pId")));

			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	
	}
}
