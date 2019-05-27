package com.atmira.practicafinal.ejercicioRecopilatorio2;

public class Peliculas {

	private String titulo, director;
	private int duracion, edad_minima;

	public Peliculas() {
		super();
		this.titulo = "Los Vengadores: ENDGAME";
		this.director = "Los hermanos Russo";
		this.duracion = 180;
		this.edad_minima = 16;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getDirector() {
		return director;
	}

	public int getDuracion() {
		return duracion;
	}

	public int getEdad_minima() {
		return edad_minima;
	}

}
