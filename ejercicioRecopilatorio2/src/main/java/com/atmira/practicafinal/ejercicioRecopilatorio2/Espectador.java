package com.atmira.practicafinal.ejercicioRecopilatorio2;

public class Espectador {

	private String nombre;
	private int edad, dinero, entradas;

	public Espectador() {
		super();
	}

	public Espectador(String nombre, int edad, int entradas, int dinero) {
		super();
		this.nombre = nombre;
		this.edad = edad;
		this.entradas = entradas;
		this.dinero = dinero;
	}

	public String getNombre() {
		return nombre;
	}

	public int getEdad() {
		return edad;
	}

	public int getDinero() {
		return dinero;
	}

	public int getEntradas() {
		return entradas;
	}

	public void setEntradas(int entradas) {
		this.entradas = entradas;
	}

}
