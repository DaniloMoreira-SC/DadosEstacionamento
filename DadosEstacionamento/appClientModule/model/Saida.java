package model;

import java.time.LocalDateTime;

public class Saida {

	private int idSaida;
	private int idEntrada;
	private LocalDateTime dtSaida;
	private int tempoMinutos;
	private double valorTotal;

	public int getIdSaida() {
		return idSaida;
	}

	public void setIdSaida(int idSaida) {
		this.idSaida = idSaida;
	}

	public int getIdEntrada() {
		return idEntrada;
	}

	public void setIdEntrada(int idEntrada) {
		this.idEntrada = idEntrada;
	}

	public LocalDateTime getDtSaida() {
		return dtSaida;
	}

	public void setDtSaida(LocalDateTime dtSaida) {
		this.dtSaida = dtSaida;
	}

	public int getTempoMinutos() {
		return tempoMinutos;
	}

	public void setTempoMinutos(int tempoMinutos) {
		this.tempoMinutos = tempoMinutos;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

}
