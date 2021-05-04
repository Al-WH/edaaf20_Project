package krusty;

import spark.Request;
import spark.Response;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import static krusty.Jsonizer.toJson;

public class Database {
	/**
	 * Modify it to fit your environment and then use this string when connecting to
	 * your database!
	 */
	private static final String jdbcString = "jdbc:mysql://puccini.cs.lth.se:3306/db04";

	// For use with MySQL or PostgreSQL
	private static final String jdbcUsername = "db04";
	private static final String jdbcPassword = "bif639he";

	private Connection conn;

	public void connect() {
		// Connect to database here
		try {
			conn = DriverManager.getConnection(jdbcString, jdbcUsername, jdbcPassword);
			System.out.println("Hej");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error with conn");
		}
	}

	// TODO: Implement and change output in all methods below!

	public String getCustomers(Request req, Response res) {

		String sql = " select * from Customers ";
		String json = "";
		try (PreparedStatement pt = conn.prepareStatement(sql)) {
			json = Jsonizer.toJson(pt.executeQuery(), "customers");
			System.out.println(json);

		} catch (SQLException e) {
			e.getStackTrace();
			return Jsonizer.anythingToJson(e.getMessage(), "status");
		}

		return json;
	}

	public String getRawMaterials(Request req, Response res) {

		String rawmaterials = "{}";

		try {
			PreparedStatement pt = conn.prepareStatement("select materialName as name, amount, unit from RawMaterials");
			rawmaterials = toJson(pt.executeQuery(), "raw-materials");
		} catch (SQLException e) {
			e.getStackTrace();
			return Jsonizer.anythingToJson(e.getMessage(), "status");
		}
		System.out.println(rawmaterials);
		return rawmaterials;
	}

	public String getCookies(Request req, Response res) {
		String cookies = "{}";

		try {
			PreparedStatement pt = conn.prepareStatement("select productName as name from Products");
			cookies = toJson(pt.executeQuery(), "cookies");
		} catch (SQLException e) {
			e.getStackTrace();
			return Jsonizer.anythingToJson(e.getMessage(), "status");
		}
		System.out.println(cookies);
		return cookies;
	}

	public String getRecipes(Request req, Response res) {
		String recepie = "{}";
		try {
			PreparedStatement qury = conn.prepareStatement("select * from Recipes group By productName ");
			recepie = toJson(qury.executeQuery(), "recipes");	
			System.out.println(recepie);
		} catch (SQLException e) {
			e.getStackTrace();
			return Jsonizer.anythingToJson(e.getMessage(), "status");
		}
	
		return recepie;

	}

	public String getPallets(Request req, Response res) {
		ArrayList<String> values = new ArrayList<String>();
        String sql = "SELECT palletNbr AS id, productName AS cookie,"
                + "ProductionDate AS production_date, customerName, IF(Blocked, 'yes', 'no')as blocked"
                + " FROM Pallets LEFT JOIN Orders ON Orders.orderNbr = Pallets.orderNbr" + " WHERE TRUE ";

        if (req.queryParams("cookie") != null) {
            sql += " AND productName = ?";
            values.add(req.queryParams("cookie"));
        }
        if (req.queryParams("from") != null) {
            sql += "AND ProductionDate >= ?";
            values.add(req.queryParams("from"));
        }
        if (req.queryParams("to") != null) {
            sql += " AND ProductionDate <= ?";
            values.add(req.queryParams("to"));
        }

        if (req.queryParams("blocked") != null) {
            sql += "Blocked = ?";
            values.add(req.queryParams("blocked"));
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < values.size(); i++) {
                ps.setString(i + 1, values.get(i));
            }
            ResultSet rs = ps.executeQuery();
            String json = Jsonizer.toJson(rs, "pallets");
            System.out.println(json);
            return json;
        } catch (SQLException e) {
            e.printStackTrace();
            return Jsonizer.anythingToJson("error", "status");
        }
	}

	public String reset(Request req, Response res) {
		// SET FOREIGN KEYS = 0
		try (PreparedStatement ps = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 0")) {
			ps.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		//remove data

		String[] tables = { "Customers","Pallets", "Orders", "Products", "RawMaterials", "Recipes" };
		for (String table : tables) {
			try (PreparedStatement stmt = conn.prepareStatement("Truncate table " + table)) {
				stmt.executeUpdate();
			} catch (SQLException e) {
				e.getStackTrace();
				return Jsonizer.anythingToJson(e.getMessage(), "status");
			}

		}

		// insert default data
		insertIntoCustomers();
		insertIntoRawMaterials();
		insertIntoProducts();
		insertIntoRecipes() ;
		
		// SET FOREIGN KEYS = 1
		try (PreparedStatement ps = conn.prepareStatement("SET FOREIGN_KEY_CHECKS = 1")) {
			ps.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}


		return  Jsonizer.anythingToJson("ok", "status");
	}
	
	// helper methods

	private void insertIntoCustomers() {
		try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Customers ( name, address) VALUES"
				+ "('Bjudkakor AB', 'Ystad')," + "('Finkakor AB', 'Helsingborg')," + "('Gästkakor AB', 'Hässleholm'),"
				+ "('Kaffebröd AB', 'Landskrona')," + "('Kalaskakor AB', 'Trelleborg'),"
				+ "('Partykakor AB', 'Kristianstad')," + "('Skånekakor AB', 'Perstorp'),"
				+ "('Småbröd AB', 'Malmö')")) {
			ps.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}

	}

	private void insertIntoRawMaterials() {
		try (PreparedStatement ps = conn
				.prepareStatement("INSERT into RawMaterials (materialName , amount , unit ,  lastdelevired_Amount ) VALUES "
						+ "('Bread crumbs', 500000, 'g',500000)," + "('Butter', 500000, 'g',500000)," + "('Chocolate', 500000, 'g',500000),"
						+ "('Chopped almonds', 500000, 'g',500000)," + "('Cinnamon', 500000, 'g',500000),"
						+ "('Egg whites', 500000, 'ml',500000)," + "('Eggs', 500000, 'g',500000),"
						+ "('Fine-ground nuts', 500000, 'g',500000)," + "('Flour', 500000, 'g',500000),"
						+ "('Ground, roasted nuts', 500000, 'g',500000)," + "('Icing sugar', 500000, 'g',500000),"
						+ "('Marzipan', 500000, 'g',500000)," + "('Potato starch', 500000, 'g',500000),"
						+ "('Roasted, chopped nuts', 500000, 'g',500000)," + "('Sodium bicarbonate', 500000, 'g',500000),"
						+ "('Sugar', 500000, 'g',500000)," + "('Vanilla sugar', 500000, 'g',500000)," + "('Vanilla', 500000, 'g',500000),"
						+ "('Wheat flour', 500000, 'g',500000);")) {
			ps.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}

	}

	private void insertIntoProducts() {
		try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Products (productName) VALUES" + "('Almond delight'),"
				+ "('Amneris')," + "('Berliner')," + "('Nut cookie')," + "('Nut ring')," + "('Tango');")) {
			ps.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}

	}

	private void insertIntoRecipes() {
		try (PreparedStatement ps = conn
				.prepareStatement("INSERT INTO Recipes(productName, materialName,amount, unit) VALUES "

						+"('Almond delight', 'Butter', 400, 'g')," + "('Almond delight', 'Chopped almonds', 279, 'g'),"
						+ "('Almond delight', 'Cinnamon', 10, 'g')," + "('Almond delight', 'Flour', 400, 'g'),"
						+ "('Almond delight', 'Sugar', 270, 'g')," + "('Amneris', 'Butter', 250, 'g'),"
						+ "('Amneris', 'Eggs', 250, 'g')," + "('Amneris', 'Marzipan', 750, 'g'),"
						+ "('Amneris', 'Potato starch', 25, 'g')," + "('Amneris', 'Wheat flour', 25, 'g'),"
						+ "('Berliner', 'Butter', 250, 'g')," + "('Berliner', 'Chocolate', 50, 'g'),"
						+ "('Berliner', 'Eggs', 50, 'g')," + "('Berliner', 'Flour', 350, 'g'),"
						+ "('Berliner', 'Icing sugar', 100, 'g')," + "('Berliner', 'Vanilla sugar', 5, 'g'),"
						+ "('Nut cookie', 'Bread crumbs', 125, 'g')," + "('Nut cookie', 'Chocolate', 50, 'g'),"
						+ "('Nut cookie', 'Egg whites', 350, 'ml')," + "('Nut cookie', 'Fine-ground nuts', 750, 'g'),"
						+ "('Nut cookie', 'Ground, roasted nuts', 625, 'g')," + "('Nut cookie', 'Sugar', 375, 'g'),"
						+ "('Nut ring', 'Butter', 450, 'g')," + "('Nut ring', 'Flour', 450, 'g'),"
						+ "('Nut ring', 'Icing sugar', 190, 'g')," + "('Nut ring', 'Roasted, chopped nuts', 225, 'g'),"
						+ "('Tango', 'Butter', 200, 'g')," + "('Tango', 'Flour', 300, 'g'),"
						+ "('Tango', 'Sodium bicarbonate', 4, 'g')," + "('Tango', 'Sugar', 250, 'g'),"
						+ "('Tango', 'Vanilla', 2, 'g');")) {
			ps.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}

	}

	public String createPallet(Request req, Response res) {   
		String cookiesName = req.queryParams("cookie");
		String creationTime = LocalDate.now().toString();
		int palletId = -1;

		if (cookieExists(cookiesName)) { // Create pallet

			try(PreparedStatement stmt = conn.prepareStatement("INSERT INTO Pallets (productionDate, productName) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS)){
				
				
				stmt.setString(1, creationTime);
				stmt.setString(2, cookiesName);
				
				stmt.executeUpdate();
				ResultSet result = stmt.getGeneratedKeys();
				if (result.next()) {
					palletId = result.getInt(1);
				}
				stmt.close();

				removeIngredients(cookiesName);

			} catch (SQLException e) {

				e.printStackTrace();
				return Jsonizer.anythingToJson("error", "status");
			}

		}

		else {
			return Jsonizer.anythingToJson("unknown cookie", "status");
		}

		return Jsonizer.anythingToJson(palletId, "id");
	}

	private boolean cookieExists(String cookiesName) {

        String MySql = "SELECT productName FROM Products WHERE productName = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(MySql);
            stmt.setString(1, cookiesName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                stmt.close();
                return true;
            }
            stmt.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return false;

    }
	
	private void removeIngredients(String cookieName) {

		HashMap<String, Integer> ingredients = new HashMap<String, Integer>();

		try {
			conn.setAutoCommit(false); // Transaction

			// Find how much we have of each ingredient used in this cookie
			String sql = "SELECT RawMaterials.materialName, RawMaterials.amount\n" + "FROM RawMaterials\n"
					+ "INNER JOIN Recipes on RawMaterials.materialName = Recipes.materialName\n"
					+ "WHERE productName = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, cookieName);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				String ingredient = rs.getString("materialName"); // add ingredient and total in storage into map
				int amount = rs.getInt("amount");
				ingredients.put(ingredient, amount); // Map with total amount of each ingredient
			}
			ps.close();

			sql = "SELECT materialName, amount FROM Recipes WHERE productName = ?"; // find ingredients and
																							// amounts
			ps = conn.prepareStatement(sql);
			ps.setString(1, cookieName);
			rs = ps.executeQuery();

			while (rs.next()) {

				String ingredient = rs.getString("materialName");
				int amount = rs.getInt("amount");

				int newAmount = ingredients.get(ingredient) - amount * 54; // Subtract total with the amount used to
																			// produce cookies
				ingredients.put(ingredient, newAmount); // Put in new total amount
			}
			ps.close();

			for (Map.Entry<String, Integer> entry : ingredients.entrySet()) { // Update each ingredient

				sql = "UPDATE RawMaterials SET amount = ? WHERE materialName = ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, entry.getValue());
				ps.setString(2, entry.getKey());
				ps.executeUpdate();
				ps.close();

			}

			conn.commit();

		} catch (SQLException e) {

			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

		try {
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	
	
}
