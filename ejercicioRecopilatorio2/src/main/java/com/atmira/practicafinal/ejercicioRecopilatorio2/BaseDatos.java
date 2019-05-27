package com.atmira.practicafinal.ejercicioRecopilatorio2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;

import org.apache.log4j.Logger;

public class BaseDatos {

	static final Logger log = Logger.getLogger(BaseDatos.class);

	public void ejecutar(String tipoSQL, String[] Datos) {
		// JDBC driver name and database URL
		String JDBC_DRIVER = "org.h2.Driver";
		String DB_URL = "jdbc:h2:~/test";
		LocalDateTime dateNow = LocalDateTime.now();

		// Database credentials
		String USER = "sa";
		String PASS = "";

		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				Statement stmt = conn.createStatement();) {
			// STEP 1: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			// STEP 2: Open a connection
			System.out.println("Connecting to database...");

			if (tipoSQL == "C") {
				stmt.executeUpdate(
						"create table cine (id INTEGER PRIMARY KEY AUTO_INCREMENT, fechahoraejecucion TIMESTAMP, titulo VARCHAR(50), asientos_libres LONG(10), asientos_ocupados LONG(10), recaudacion DOUBLE(10))");
			} else {
				stmt.executeUpdate("INSERT INTO cine "
						+ "(fechahoraejecucion,titulo,asientos_libres, asientos_ocupados,recaudacion)" + "values('"
						+ dateNow + "', '" + Datos[0] + "'," + Integer.parseInt(Datos[1]) + ", "
						+ Integer.parseInt(Datos[2]) + ", " + Double.parseDouble(Datos[3]) + ")");
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			log.error(e);

		}
	}

}
