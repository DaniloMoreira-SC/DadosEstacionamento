package service;

import java.sql.SQLException;
import java.util.Scanner;
import dao.EntradaDAO;
import dao.VagaDAO;
import model.Entrada;

public class EstacionamentoService {

	private EntradaDAO entradaDAO;
	private VagaDAO vagaDAO;

	public EstacionamentoService() {

		entradaDAO = new EntradaDAO();
		vagaDAO = new VagaDAO();

	}

	public void registrarEntrada(Scanner scanner) throws SQLException {

		Entrada entrada = new Entrada();

		Integer vaga = vagaDAO.buscarVagaLivre();

		if (vaga == null) {

			System.out.println("Estacionamento lotado!");

			return;

		}

		System.out.println("\nVaga disponível: " + vaga);

		entrada.setNumeroVaga(vaga);

		System.out.println("Nome do cliente: ");
		entrada.setNomeCliente(scanner.nextLine());

		String placa;

		do {

			System.out.print("Placa do veículo: ");

			placa = scanner.nextLine().trim().toUpperCase();

			if (!placa.matches("[A-Z]{3}[0-9][A-Z0-9][0-9]{2}")) {

				System.out.println("Placa inválida!");

			}

		} while (!placa.matches("[A-Z]{3}[0-9][A-Z0-9][0-9]{2}"));

		entrada.setPlaca(placa);

		if (entradaDAO.veiculoEstaNoPatio(placa)) {

			System.out.println("\nVeículo já está no estacionamento!");

			return;
		}

		String tipo;

		do {

			System.out.println("Tipo do veículo (CARRO/MOTO): ");

			tipo = scanner.nextLine().trim().toUpperCase();

			if (!tipo.equals("CARRO") && !tipo.equals("MOTO")) {

				System.out.println("Tipo inválido! Digite apenas CARRO ou MOTO. ");
			}

		} while (!tipo.equals("CARRO") && !tipo.equals("MOTO"));

		entrada.setTipoVeiculo(tipo);

		entradaDAO.inserir(entrada);

		vagaDAO.ocuparVaga(entrada.getNumeroVaga());

		System.out.println("\n==================================");
		System.out.println("Entrada registrada com sucesso!");
		System.out.println("Vaga ocupada: " + entrada.getNumeroVaga());
		System.out.println("==================================");
	}

}
