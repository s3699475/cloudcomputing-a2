package com.ricky.cc_assignment2;

//For Google BigQuery
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.JobId;
import com.google.cloud.bigquery.JobInfo;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.QueryResponse;
import com.google.cloud.bigquery.TableResult;
import java.util.UUID;

//For Pokemon API Access
import me.sargunvohra.lib.pokekotlin.client.*;

//Normal Java library import
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import me.sargunvohra.lib.pokekotlin.model.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.*;

//Google SQL
import java.sql.*;

public class App {

	static PokeApi pokeApi = new PokeApiClient();
	static JFrame frame = new JFrame("Pokemon API search");
	static BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
	
	static String lastTyped;
	
	public static void pokemonSearchByID() throws IOException {
		int id = 1;

		while (id != 0) {
			try {
				id = Integer.parseInt(JOptionPane.showInputDialog(frame,
						"Please enter the pokemon id you are looking for (1 - 801):"));
			} catch (Exception e) {
				continue;
			}

			if (id > 801 || id < 1) {
				continue;
			}

			Pokemon result = pokeApi.getPokemon(id);
			PokemonSpecies resultSpecies = pokeApi.getPokemonSpecies(id);
			
			lastTyped = Integer.toString(id);
			
			// Get pokemon Name and Types
			String messageResultPokemonType = "";
			for (int i = 0; i < result.getTypes().size(); i++) {
				messageResultPokemonType += result.getTypes().get(i).getType().getName();
				if (i < result.getTypes().size() - 1) {
					messageResultPokemonType += ", ";
				}
			}
			System.out.print("\n");

			// Get what the pokemon evolves from
			String evolveFromWhat;
			try {
				evolveFromWhat = resultSpecies.getEvolvesFromSpecies().getName();
			} catch (Exception e) {
				evolveFromWhat = "It hatched from an egg";
			}

			String messageResult = "Name:                " + result.getName() + "\nColor:                 "
					+ resultSpecies.getColor().getName() + "\nType:                  " + messageResultPokemonType
					+ "\nEvolves from:     " + evolveFromWhat;

			String pathURL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
					+ Integer.toString(id) + ".png";
			// System.out.println(pathURL);
			URL url = new URL(pathURL);
			BufferedImage image = ImageIO.read(url);

			ImageIcon icon = new ImageIcon(image);

			JOptionPane.showMessageDialog(frame, messageResult, "Search Result", JOptionPane.INFORMATION_MESSAGE, icon);
			break;
		}
	}

	public static String listType() {
		String result = "";
		for (int i = 1; i <= 18; i++) {
			result += Integer.toString(i) + ". " + pokeApi.getType(i).getName() + "\n";
		}
		return result;
	}

	public static void typeDetail() {
		int id = 1;

		while (id != 0) {
			try {
				id = Integer.parseInt(JOptionPane.showInputDialog(frame,
						"Please enter the ID of the type you are looking for: \n\n" + listType() + "\n"));
			} catch (Exception e) {
				continue;
			}

			if (id > 18 || id < 1) {
				continue;
			}

			Type typeResult = pokeApi.getType(id);
			String typeName = "Type name:                 " + typeResult.getName();

			lastTyped = typeResult.getName();
			
			// Half damage to
			String halfDamageTo = "\nHalf damage to:          ";
			if (typeResult.getDamageRelations().getHalfDamageTo().size() == 0) {
				halfDamageTo += "none";
			}
			for (int i = 0; i < typeResult.getDamageRelations().getHalfDamageTo().size(); i++) {
				halfDamageTo += typeResult.getDamageRelations().getHalfDamageTo().get(i).getName();
				if (i < typeResult.getDamageRelations().getHalfDamageTo().size() - 1) {
					halfDamageTo += ", ";
				}
			}
			// System.out.println(halfDamageTo);

			// Half damage from
			String halfDamageFrom = "\nHalf damage from:      ";
			if (typeResult.getDamageRelations().getHalfDamageFrom().size() == 0) {
				halfDamageFrom += "none";
			}
			for (int i = 0; i < typeResult.getDamageRelations().getHalfDamageFrom().size(); i++) {
				halfDamageFrom += typeResult.getDamageRelations().getHalfDamageFrom().get(i).getName();
				if (i < typeResult.getDamageRelations().getHalfDamageFrom().size() - 1) {
					halfDamageFrom += ", ";
				}
			}
			// System.out.println(halfDamageFrom);

			// Double damage to
			String doubleDamageTo = "\nDouble damage to:     ";
			if (typeResult.getDamageRelations().getDoubleDamageTo().size() == 0) {
				doubleDamageTo += "none";
			}
			for (int i = 0; i < typeResult.getDamageRelations().getDoubleDamageTo().size(); i++) {
				doubleDamageTo += typeResult.getDamageRelations().getDoubleDamageTo().get(i).getName();
				if (i < typeResult.getDamageRelations().getDoubleDamageTo().size() - 1) {
					doubleDamageTo += ", ";
				}
			}
			// System.out.println(doubleDamageTo);

			// Double damage from
			String doubleDamageFrom = "\nDouble damage from: ";
			if (typeResult.getDamageRelations().getDoubleDamageFrom().size() == 0) {
				doubleDamageFrom += "none";
			}
			for (int i = 0; i < typeResult.getDamageRelations().getDoubleDamageFrom().size(); i++) {
				doubleDamageFrom += typeResult.getDamageRelations().getDoubleDamageFrom().get(i).getName();
				if (i < typeResult.getDamageRelations().getDoubleDamageFrom().size() - 1) {
					doubleDamageFrom += ", ";
				}
			}
			// System.out.println( doubleDamageFrom);

			// No Damage to
			String noDamageTo = "\nNo damage to:            ";
			if (typeResult.getDamageRelations().getNoDamageTo().size() == 0) {
				noDamageTo += "none";
			}
			for (int i = 0; i < typeResult.getDamageRelations().getNoDamageTo().size(); i++) {
				noDamageTo += typeResult.getDamageRelations().getNoDamageTo().get(i).getName();
				if (i < typeResult.getDamageRelations().getNoDamageTo().size() - 1) {
					noDamageTo += ", ";
				}
			}
			// System.out.println( noDamageTo);

			// No Damage from
			String noDamageFrom = "\nNo damage from:        ";
			if (typeResult.getDamageRelations().getNoDamageFrom().size() == 0) {
				noDamageFrom += "none";
			}
			for (int i = 0; i < typeResult.getDamageRelations().getNoDamageFrom().size(); i++) {
				noDamageFrom += typeResult.getDamageRelations().getNoDamageFrom().get(i).getName();
				if (i < typeResult.getDamageRelations().getNoDamageFrom().size() - 1) {
					noDamageFrom += ", ";
				}
			}
			// System.out.println(noDamageFrom);

			String messageResult = typeName + "\n" + halfDamageTo + "\n" + halfDamageFrom + "\n" + doubleDamageTo + "\n"
					+ doubleDamageFrom + "\n" + noDamageTo + "\n" + noDamageFrom + "\n";

			JOptionPane.showMessageDialog(frame, messageResult, "Search Result", JOptionPane.PLAIN_MESSAGE);
			break;
		}

	}

	/**
	 * Look for the pokemon ID using google bigquery
	 * 
	 * @param name the name of the pokemon
	 * @return pokedex ID or 0 if result is not found
	 * @throws InterruptedException
	 */
	public static int findPokemonID(String name) throws InterruptedException {
		int pokedexID = 0;

		QueryJobConfiguration queryConfig = QueryJobConfiguration
				.newBuilder("SELECT number FROM `s3699475-cc2018.pokemon.pokemon` where UPPER(name) like UPPER('%"
						+ name + "%')")
				// Use standard SQL syntax for queries.
				// See: https://cloud.google.com/bigquery/sql-reference/
				.setUseLegacySql(false).build();

		// Create a job ID so that we can safely retry.
		JobId jobId = JobId.of(UUID.randomUUID().toString());
		Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

		// Wait for the query to complete.
		queryJob = queryJob.waitFor();

		// Check for errors
		if (queryJob == null) {
			throw new RuntimeException("Job no longer exists");
		} else if (queryJob.getStatus().getError() != null) {
			// You can also look at queryJob.getStatus().getExecutionErrors() for all
			// errors, not just the latest one.
			throw new RuntimeException(queryJob.getStatus().getError().toString());
		}

		// Get the results.
		TableResult result = queryJob.getQueryResults();

		// Print all pages of the results.
		for (FieldValueList row : result.iterateAll()) {
			int number = Integer.parseInt(row.get("number").getStringValue());
			// long viewCount = row.get("view_count").getLongValue();
			pokedexID = number;
		}

		return pokedexID;
	}

	public static void pokemonSearchByName() throws IOException {
		String userinput = "";
		int id = 0;
		userinput = JOptionPane.showInputDialog(frame, "Please enter the name of pokemon you are looking for: \n");

		try {
			id = findPokemonID(userinput);
		} catch (InterruptedException e) {
			System.out.println("Interrupted Exception catched from BigQuery API search");
		}

		if (id == 0) {
			JOptionPane.showMessageDialog(frame, "No pokemon found with such name", "Search Result",
					JOptionPane.PLAIN_MESSAGE);
			return;
		}

		lastTyped = userinput;
		Pokemon result = pokeApi.getPokemon(id);
		PokemonSpecies resultSpecies = pokeApi.getPokemonSpecies(id);

		// Get pokemon Name and Types
		String messageResultPokemonType = "";
		for (int i = 0; i < result.getTypes().size(); i++) {
			messageResultPokemonType += result.getTypes().get(i).getType().getName();
			if (i < result.getTypes().size() - 1) {
				messageResultPokemonType += ", ";
			}
		}
		System.out.print("\n");

		// Get what the pokemon evolves from
		String evolveFromWhat;
		try {
			evolveFromWhat = resultSpecies.getEvolvesFromSpecies().getName();
		} catch (Exception e) {
			evolveFromWhat = "It hatched from an egg";
		}

		String messageResult = "Name:                " + result.getName() + "\nColor:                 "
				+ resultSpecies.getColor().getName() + "\nType:                  " + messageResultPokemonType
				+ "\nEvolves from:     " + evolveFromWhat;

		String pathURL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
				+ Integer.toString(id) + ".png";
		// System.out.println(pathURL);
		URL url = new URL(pathURL);
		BufferedImage image = ImageIO.read(url);

		ImageIcon icon = new ImageIcon(image);

		
		JOptionPane.showMessageDialog(frame, messageResult, "Search Result", JOptionPane.INFORMATION_MESSAGE, icon);

	}
	

	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.jdbc.Driver");  
		Connection conn=DriverManager.getConnection("jdbc:mysql://35.201.26.132:3306/ccassignment2history","root","123456");  
		
		Statement stmt = conn.createStatement();
		String sql;
		ResultSet rs;
		
		int input = 1;
		while (input != 0) {
			try {
				String message = "What would you like to do: \n\n"
						+ "1.Search pokemon by ID\n"
						+ "2.Search pokemon by name\n"
						+ "3.Look at details of pokemon types\n"
						+ "4.History Log\n"
						+ "5.Delete all history log"
						+ "\n\n0.Exit";
				input = Integer.parseInt(JOptionPane.showInputDialog(frame, message));
			} catch (Exception e) {
				continue;
			}

			if (input > 5 || input < 0) {
				continue;
			} else if (input == 0) {
				System.exit(0);
				break;
			} else if (input == 1) {
				pokemonSearchByID();
				stmt.executeUpdate("INSERT INTO history (detail) VALUES ('User searched pokemon by ID with input of ID " + lastTyped + "')");
				continue;
			} else if (input == 2) {
				pokemonSearchByName();
				stmt.executeUpdate("INSERT INTO history (detail) VALUES ('User searched pokemon by name " + lastTyped + "')");
				continue;
			} else if (input == 3) {
				typeDetail();
				stmt.executeUpdate("INSERT INTO history (detail) VALUES ('User searched pokemon type " + lastTyped + "')");
				continue;
			} else if(input == 4) {
				rs = stmt.executeQuery("SELECT detail FROM history");
				String output ="";
				while ( rs.next() ) {
		            output += rs.getString("detail") + "\n\n";
		        }
				JOptionPane.showMessageDialog(frame, output, "Search Result", JOptionPane.PLAIN_MESSAGE);
				continue;
			} else if(input == 5) {
				stmt.executeUpdate("DELETE FROM history");
				JOptionPane.showMessageDialog(frame, "History successfully deleted", "Details deleted", JOptionPane.INFORMATION_MESSAGE);
				continue;
			}
			
		}	
	}
}
