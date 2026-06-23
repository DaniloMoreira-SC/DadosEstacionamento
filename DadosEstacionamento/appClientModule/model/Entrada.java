package model;

import java.time.LocalDateTime;

public class Entrada {

	private int idEntrada;
	private String nomeCliente;
	private String placa;
	private String tipoVeiculo;

	public int getIdEntrada() {
		return idEntrada;
	}

	public void setIdEntrada(int idEntrada) {
		this.idEntrada = idEntrada;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente.toUpperCase();
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa.toUpperCase();
	}

	public String getTipoVeiculo() {
		return tipoVeiculo;
	}

	public void setTipoVeiculo(String tipoVeiculo) {
		this.tipoVeiculo = tipoVeiculo.toUpperCase();
	}

	// ATRIBUTO SAIDA

	private LocalDateTime dtEntrada;
	private String statusVeiculo;

	// GETTERS E SETTERS SAIDA

	public LocalDateTime getDtEntrada() {
		return dtEntrada;
	}

	public void setDtEntrada(LocalDateTime dtEntrada) {
		this.dtEntrada = dtEntrada;
	}

	public String getStatusVeiculo() {
		return statusVeiculo;
	}

	public void setStatusVeiculo(String statusVeiculo) {
		this.statusVeiculo = statusVeiculo;
	}

}
