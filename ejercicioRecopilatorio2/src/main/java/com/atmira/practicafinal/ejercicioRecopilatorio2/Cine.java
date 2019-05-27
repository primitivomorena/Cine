package com.atmira.practicafinal.ejercicioRecopilatorio2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class Cine {

	static final Logger log = Logger.getLogger(Cine.class);

	private Peliculas pelicula = new Peliculas();
	private BaseDatos BBDD = new BaseDatos();
	private double precio_entrada;

	private Character letras[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' };
	private String asiento[][] = new String[8][9];
	private String datosBBDD[] = new String[4];
	private String asientoOcupado[][] = new String[8][9];

	private List<Espectador> espectadores = new ArrayList<Espectador>();

	public Cine() {
		super();
		this.precio_entrada = 5;
		rellenarSala();
		rellenaEspectadores();
		comprobarVentaEntrada();
		mostrarAsientos();
		System.out.println("\nSE HA RECAUDADO: " + calcularRecaudacion() + "€");
		creaBBDD();
		guardaPantalla();
	}

	private void creaBBDD() {
		datosBBDD[0] = pelicula.getTitulo();
		datosBBDD[1] = Integer.toString((8 * 9) - contarAsientosLlenos());
		datosBBDD[2] = Integer.toString(contarAsientosLlenos());
		datosBBDD[3] = Double.toString(calcularRecaudacion());
		BBDD.ejecutar("I", datosBBDD);
	}

	private void guardaPantalla() {

		try (FileWriter salida = new FileWriter(Paths.get("src\\main\\resources\\pantalla_cine.txt").toFile());
				BufferedWriter escritorTexto = new BufferedWriter(salida)) {

			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 9; j++) {
					escritorTexto.write(asiento[i][j] + "\t\t");
				}
				escritorTexto.newLine();
				for (int j = 0; j < 9; j++) {
					escritorTexto.write(asientoOcupado[i][j] + "	");
				}
				escritorTexto.newLine();
				escritorTexto.newLine();
			}
			escritorTexto.write("\t\t\t\t\t\tPANTALLA");

			System.out.println("\nFICHERO GUARDADO");

		} catch (FileNotFoundException e) {
			log.error("ERROR AL ESCRIBIR EL FICHERO");
			e.printStackTrace();
		} catch (IOException e) {
			log.error(e);
			e.printStackTrace();
		}
	}

	private void rellenaEspectadores() {
		String linea = "";
		String[] lineas = new String[4];
		int countLine = 0;

		try (InputStream entrada = new FileInputStream(Paths.get("src\\main\\resources\\ENTRADAS.txt").toFile());
				InputStreamReader transformador = new InputStreamReader(entrada);
				BufferedReader lectorTexto = new BufferedReader(transformador)) {

			while ((linea = lectorTexto.readLine()) != null) {
				if (countLine != 0) {
					lineas = linea.split(",");
					espectadores.add(new Espectador(lineas[0], Integer.parseInt(lineas[1]), Integer.parseInt(lineas[2]),
							Integer.parseInt(lineas[3])));
				}
				countLine++;
			}

			// for (Espectador e : espectadores) {
			// System.out.println("- " + e.getNombre());
			// }

		} catch (FileNotFoundException e) {
			log.error("ERROR AL LEER EL DOCUMENTO");
			e.printStackTrace();
		} catch (IOException e) {
			log.error("ERROR: " + e);
			e.printStackTrace();
		}
	}

	public Peliculas getPelicula() {
		return pelicula;
	}

	public double getPrecio_entrada() {
		return precio_entrada;
	}

	private void rellenarSala() {
		int num = 8;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 9; j++) {
				asiento[i][j] = num + " " + letras[j];
			}
			num--;
		}
	}

	private void mostrarAsientos() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 9; j++) {
				System.out.print(asiento[i][j] + "\t");
			}
			System.out.println("");
			for (int j = 0; j < 9; j++) {
				System.out.print(asientoOcupado[i][j] + "\t");
			}
			System.out.println("\n");
		}
		System.out.println("\t\t\tPANTALLA");
	}

	private void comprobarVentaEntrada() {
		if (contarAsientosLlenos() == (8 * 9)) {
			System.out.println("LA SALA ESTÁ LLENA.");
			log.warn("LA SALA ESTÁ LLENA.");
		} else {
			for (Espectador e : espectadores) {
				if ((contarAsientosLlenos() + e.getEntradas()) > (8 * 9)) {
					System.out.println(e.getNombre() + " NO HAY ASIENTOS SUFICIENTES.");
					log.warn(e.getNombre() + " NO HAY ASIENTOS SUFICIENTES.");
				} else if (pelicula.getEdad_minima() > e.getEdad()) {
					System.out.println(e.getNombre() + ": NO TIENES EDAD SUFICIENTE.");
					log.warn(e.getNombre() + ": NO TIENES EDAD SUFICIENTE.");
				} else if ((this.getPrecio_entrada() * e.getEntradas()) > e.getDinero()) {
					if (e.getEntradas() == 1) {
						System.out.println(e.getNombre() + ": NO TIENES SUFICIENTE DINERO PARA COMPRAR "
								+ e.getEntradas() + " ENTRADA");
						log.warn(e.getNombre() + ": NO TIENES SUFICIENTE DINERO PARA COMPRAR " + e.getEntradas()
								+ " ENTRADA");
					} else {
						System.out.println(e.getNombre() + ": NO TIENES SUFICIENTE DINERO PARA COMPRAR "
								+ e.getEntradas() + " ENTRADAS");
						log.warn(e.getNombre() + ": NO TIENES SUFICIENTE DINERO PARA COMPRAR " + e.getEntradas()
								+ " ENTRADAS");
					}
				} else {
					System.out.println(e.getNombre() + ": PUEDES COMPRAR LAS ENTRADAS.");

					if (assignarAsientosConjuntos(e, e.getEntradas()) == false) {
						if (contarAsientosLlenos() < (8 * 9) && e.getEntradas() <= ((8 * 9) - contarAsientosLlenos())) {
							log.warn(e.getNombre() + " NO CABEIS TODOS JUNTOS, SE OS ASSIGNARÁ POR GRUPOS.");
							assignarHuequitos(e);
						} else {
							System.out.println(e.getNombre() + " NO CABES EN EL CINE");
							log.warn(e.getNombre() + " NO CABES EN EL CINE");
						}
					}
				}
			}
		}
	}

	private void assignarHuequitos(Espectador espectador) {
		int entradas_maximas = espectador.getEntradas();
		int i = espectador.getEntradas();
		while (entradas_maximas > 0) {
			if (assignarAsientosConjuntos(espectador, i) == false) {
				i--;
				// if (i > entradas_maximas) {
				// i = entradas_maximas;
				// }
			} else {
				entradas_maximas = entradas_maximas - i;
				if (i > entradas_maximas) {
					i = entradas_maximas;
				}
			}
		}
	}

	private boolean assignarAsientosConjuntos(Espectador espectador, int entradas) {
		int disponible = 0;
		String mensaje_log = "";
		int i = 0;
		for (int j = 0; j < 9; j++) {
			if (asientoOcupado[i][j] == null) {
				disponible++;
				if (disponible == entradas) {
					for (int k = (j + 1 - entradas); k <= j; k++) {
						asientoOcupado[i][k] = espectador.getNombre();
						mensaje_log = mensaje_log + asiento[i][k] + ", ";
					}
					log.info(espectador.getNombre() + ", TIENES LOS ASIENTOS: " + mensaje_log);
					return true;
				}
			} else {
				disponible = 0;
			}
			if (j == 8) {
				i++;
				disponible = 0;
				if (i == 8) {
					return false;
				} else {
					j = -1;
				}
			}
		}
		return false;
	}

	private int contarAsientosLlenos() {
		int cont = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 9; j++) {
				if (asientoOcupado[i][j] != null && asientoOcupado[i][j] != "") {
					cont++;
				}
			}
		}
		return cont;
		// if (cont == (8*9)) {
		// return 1;
		// } else {
		// return 0;
		// }
	}

	private double calcularRecaudacion() {
		return contarAsientosLlenos() * this.getPrecio_entrada();
	}

}
