import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

import dao.EntradaDAO;
import dao.SaidaDAO;
import dao.TarifaDAO;

import model.Entrada;
import model.Saida;

public class Main {
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner scanner = new Scanner(System.in);

		EntradaDAO entradaDAO = new EntradaDAO();

		int opcao = -1;

		while (opcao != 0) {

			System.out.println("==== ESTACIONAMENTO ====");
			System.out.println("1 - Registrar Entrada");
			System.out.println("2 - Registrar Saída");
			System.out.println("0 - Sair");
			System.out.print("Escolha: ");

			opcao = scanner.nextInt();
			scanner.nextLine();

			switch (opcao) {

			case 1:

				try {

					Entrada entrada = new Entrada();

					System.out.print("Nome do cliente: ");

					entrada.setNomeCliente(scanner.nextLine());

					System.out.println("Digite a placa do veículo: ");

					entrada.setPlaca(scanner.nextLine());

					System.out.println("Digite o tipo do veiculo (CARRO/MOTO): ");

					entrada.setTipoVeiculo(scanner.nextLine());

					entradaDAO.inserir(entrada);

					System.out.println("\nEntrada registrada com sucesso!");

				} catch (SQLException e) {

					System.out.println("\nErro ao salvar com o banco de dados!");

					System.out.println(e.getMessage());

				}

				break;

			case 2:

				try {

					System.out.print("Informe a placa: ");

					String placaSaida = scanner.nextLine();

					Entrada entrada = entradaDAO.buscarEntradaAberta(placaSaida);

					if (entrada == null) {

						System.out.println("Veículo não encontrado.");

						break;
					}

					LocalDateTime agora = LocalDateTime.now();

					long minutos = Duration.between(entrada.getDtEntrada(), agora).toMinutes();

					if (minutos < 1) {
						minutos = 1;
					}

					TarifaDAO tarifaDAO = new TarifaDAO();

					double valorHora = tarifaDAO.buscarValorHora();

					double valorTotal = (minutos / 60.0) * valorHora;

					Saida saida = new Saida();

					saida.setIdEntrada(entrada.getIdEntrada());

					saida.setDtSaida(agora);

					saida.setTempoMinutos((int) minutos);

					saida.setValorTotal(valorTotal);

					SaidaDAO saidaDAO = new SaidaDAO();

					saidaDAO.inserir(saida);

					entradaDAO.finalizarEntrada(entrada.getIdEntrada());

					System.out.println("\n===== RECIBO =====");

					System.out.println("Cliente: " + entrada.getNomeCliente());

					System.out.println("Placa: " + entrada.getPlaca());

					System.out.println("Tempo: " + minutos + " minutos");

					System.out.printf("Valor: R$ %.2f%n", valorTotal);

				} catch (SQLException e) {

					System.out.println("\nErro no banco de dados!");

					System.out.println(e.getMessage());
				}

				break;

			case 0:

				System.out.println("\nSistema encerrado. ");

				break;

			default:

				System.out.println("\nOpção inválida");

			}

		}

		scanner.close();

	}

}