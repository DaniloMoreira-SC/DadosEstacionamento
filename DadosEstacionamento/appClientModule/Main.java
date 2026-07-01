import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

import dao.EntradaDAO;
import dao.SaidaDAO;
import dao.TarifaDAO;
import dao.VagaDAO;
import model.Entrada;
import model.Saida;
import service.EstacionamentoService;

public class Main {
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner scanner = new Scanner(System.in);

		EstacionamentoService service = new EstacionamentoService();

		EntradaDAO entradaDAO = new EntradaDAO();

		int opcao = -1;

		while (opcao != 0) {

			System.out.println("==== ESTACIONAMENTO ====");
			System.out.println("1 - Registrar Entrada");
			System.out.println("2 - Registrar Saída");
			System.out.println("3 - Listar Veículo no Pátio");
			System.out.println("4- Histórico de Cliente");
			System.out.println("0 - Sair");
			System.out.print("Escolha: ");

			opcao = scanner.nextInt();
			scanner.nextLine();

			switch (opcao) {

			case 1:

				try {

					service.registrarEntrada(scanner);

				} catch (SQLException e) {

					System.out.println("\nErro ao salvar no banco!");

					System.out.println(e.getMessage());

				}

				break;

			// MOSTRAR RECIBO

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

					VagaDAO vagaDAO = new VagaDAO();

					vagaDAO.liberarVaga(entrada.getNumeroVaga());

					entradaDAO.finalizarEntrada(entrada.getIdEntrada());

					System.out.println("\n===== RECIBO =====");

					System.out.println("Cliente: " + entrada.getNomeCliente());

					System.out.println("Placa: " + entrada.getPlaca());

					System.out.println("Tempo: " + entrada.getTempoFormatado());

					System.out.printf("Valor: R$ %.2f%n", valorTotal);

					System.out.println("\n");

				} catch (SQLException e) {

					System.out.println("\nErro no banco de dados!");

					System.out.println(e.getMessage());
				}

				break;

			case 3:

				try {
					entradaDAO.listarPatio();

				} catch (SQLException e) {

					System.out.println("Erro ao consultar o banco de dados!");

					System.out.println(e.getMessage());

				}

				break;

			case 4:

				try {

					entradaDAO.listarHistorico();

				} catch (SQLException e) {

					System.out.println("Erro ao consultar histórico.");

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