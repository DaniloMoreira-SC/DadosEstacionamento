import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

import dao.EntradaDAO;
import dao.SaidaDAO;
import dao.TarifaDAO;
import dao.VagaDAO;
import dao.VagasDAO;
import dao.RelatorioDAO;
import model.Entrada;
import model.Saida;
import model.Pagamento;
import service.EstacionamentoService;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		EstacionamentoService service = new EstacionamentoService();
		EntradaDAO entradaDAO = new EntradaDAO();
		TarifaDAO tarifaDAO = new TarifaDAO();
		VagasDAO vagasDAO = new VagasDAO();
		RelatorioDAO relatorioDAO = new RelatorioDAO();

		int opcao = -1;

		while (opcao != 0) {
			exibirMenu();
			System.out.print("Escolha: ");
			opcao = scanner.nextInt();
			scanner.nextLine();

			switch (opcao) {

			case 1:
				registrarEntrada(service, scanner);
				break;

			case 2:
				registrarSaida(entradaDAO, tarifaDAO, scanner);
				break;

			case 3:
				try {
					entradaDAO.listarPatio();
				} catch (SQLException e) {
					System.out.println("Erro ao listar pátio!");
					System.out.println(e.getMessage());
				}
				break;

			case 4:
				buscarVeiculoPorPlaca(entradaDAO, scanner);
				break;

			case 5:
				exibirHistoricoCliente(entradaDAO, scanner);
				break;

			case 6:
				exibirFaturamentoDia(relatorioDAO);
				break;

			case 7:
				gerenciarTabeaPrecos(tarifaDAO, scanner);
				break;

			case 8:
				exibirVagasDisponiveis(vagasDAO);
				break;

			case 9:
				exibirRelatorioGeral(relatorioDAO);
				break;

			case 0:
				System.out.println("\n✅ Sistema encerrado. Até logo!");
				break;

			default:
				System.out.println("\n❌ Opção inválida!");

			}
		}

		scanner.close();
	}

	private static void exibirMenu() {
		System.out.println("\n======= ESTACIONAMENTO =======");
		System.out.println("1 - Registrar Entrada");
		System.out.println("2 - Registrar Saída");
		System.out.println("3 - Listar Veículos no Pátio");
		System.out.println("4 - Buscar Veículo por Placa");
		System.out.println("5 - Histórico do Cliente");
		System.out.println("6 - Faturamento do Dia");
		System.out.println("7 - Gerenciar Tabela de Preços");
		System.out.println("8 - Vagas Disponíveis");
		System.out.println("9 - Relatório Geral");
		System.out.println("0 - Sair");
		System.out.println("========================================");
	}

	private static void registrarEntrada(EstacionamentoService service, Scanner scanner) {
		try {
			service.registrarEntrada(scanner);
		} catch (SQLException e) {
			System.out.println("\n❌ Erro ao salvar entrada!");
			System.out.println(e.getMessage());
		}
	}

	private static void registrarSaida(EntradaDAO entradaDAO, TarifaDAO tarifaDAO, Scanner scanner) {
		try {
			System.out.print("Informe a placa: ");

			String placaSaida = scanner.nextLine().trim().toUpperCase();

			Entrada entrada = entradaDAO.buscarEntradaAberta(placaSaida);

			if (entrada == null) {
				System.out.println("❌ Veículo não encontrado.");
				return;
			}

			LocalDateTime agora = LocalDateTime.now();
			long minutos = java.time.Duration.between(entrada.getDtEntrada(), agora).toMinutes();

			double valorHora = tarifaDAO.buscarValorHora();
			double valorHoraExtra = tarifaDAO.buscarValorHoraExtra();
			double valorTotal;

			// Lógica: até 5 min cobra 1 hora, acima disso cobra hora + extras
			if (minutos <= 5) {
				valorTotal = valorHora;
				minutos = 1; // Cobra como 1 minuto (primeira hora)
			} else {
				long minutosExtra = minutos - 60;
				valorTotal = valorHora + (minutosExtra > 0 ? (minutosExtra / 60.0) * valorHoraExtra : 0);
			}

			System.out.println("\n========================================");
			System.out.println("            CÁLCULO DE SAÍDA              ");
			System.out.println(" Cliente: " + entrada.getNomeCliente());
			System.out.println(" Placa: " + entrada.getPlaca());
			System.out.println(" Vaga: " + entrada.getNumeroVaga());

			// Formata tempo
			long horas = minutos / 60;
			long mins = minutos % 60;
			System.out.printf(" Tempo estacionado: %02d:%02d:%02d          %n", horas, mins, 0);
			System.out.printf(" Valor a cobrar: R$ %.2f               %n", valorTotal);
			System.out.println("========================================");

			System.out.print("\n✅ Deseja registrar a saída? (s/n): ");
			String confirmacao = scanner.nextLine().trim().toLowerCase();

			if (!confirmacao.equals("s")) {
				System.out.println("❌ Operação cancelada!");
				return;
			}

			// Exibir forma de pagamento
			Pagamento.listarOpcoes();
			System.out.print("Escolha a forma de pagamento: ");
			int opcaoPagamento = scanner.nextInt();
			scanner.nextLine();

			Pagamento pagamento;
			try {
				pagamento = Pagamento.obterPorIndice(opcaoPagamento);
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("❌ Opção inválida! Usando PIX como padrão.");
				pagamento = Pagamento.PIX;
			}

			// Registrar saída
			Saida saida = new Saida();
			saida.setIdEntrada(entrada.getIdEntrada());
			saida.setDtSaida(agora);
			saida.setTempoMinutos((int) minutos);
			saida.setValorTotal(valorTotal);
			saida.setFormaPagamento(pagamento);

			SaidaDAO saidaDAO = new SaidaDAO();
			saidaDAO.inserir(saida);

			VagaDAO vagaDAO = new VagaDAO();
			vagaDAO.liberarVaga(entrada.getNumeroVaga());

			entradaDAO.finalizarEntrada(entrada.getIdEntrada());

			// Recibo formatado
			exibirRecibo(entrada, minutos, valorTotal, pagamento);

		} catch (SQLException e) {
			System.out.println("\n❌ Erro no banco de dados!");
			System.out.println(e.getMessage());
		}
	}

	private static void exibirRecibo(Entrada entrada, long minutos, double valor, Pagamento pagamento) {
		long horas = minutos / 60;
		long mins = minutos % 60;

		System.out.println("\n========== RECIBO ==========");
		System.out.println("Cliente: " + entrada.getNomeCliente());
		System.out.println("Placa: " + entrada.getPlaca());
		System.out.println("Vaga: " + entrada.getNumeroVaga());
		System.out.printf("Tempo: %02d:%02d:00%n", horas, mins);
		System.out.println("Forma de Pagamento: " + pagamento.getDescricao());
		System.out.printf("Valor Total: R$ %.2f%n", valor);
		System.out.println("========================================\n");
	}

	private static void buscarVeiculoPorPlaca(EntradaDAO entradaDAO, Scanner scanner) {
		try {
			System.out.print("Informe a placa: ");
			String placa = scanner.nextLine().trim().toUpperCase();
			entradaDAO.buscarVeiculoPorPlaca(placa);
		} catch (SQLException e) {
			System.out.println("❌ Erro ao buscar veículo!");
			System.out.println(e.getMessage());
		}
	}

	private static void exibirHistoricoCliente(EntradaDAO entradaDAO, Scanner scanner) {
		try {
			System.out.println("\nBuscar por:");
			System.out.println("1 - Nome do cliente");
			System.out.println("2 - Placa do veículo");
			System.out.print("Escolha: ");
			int opcao = scanner.nextInt();
			scanner.nextLine();

			if (opcao == 1) {
				System.out.print("Nome do cliente: ");
				String nome = scanner.nextLine().trim().toUpperCase();
				entradaDAO.buscarHistoricoCliente(nome);
			} else if (opcao == 2) {
				System.out.print("Placa do veículo: ");
				String placa = scanner.nextLine().trim().toUpperCase();
				entradaDAO.buscarVeiculoPorPlaca(placa);
			} else {
				System.out.println("❌ Opção inválida!");
			}
		} catch (SQLException e) {
			System.out.println("❌ Erro ao buscar histórico!");
			System.out.println(e.getMessage());
		}
	}

	private static void exibirFaturamentoDia(RelatorioDAO relatorioDAO) {
		try {
			relatorioDAO.exibirFaturamentoDia(LocalDate.now());
		} catch (SQLException e) {
			System.out.println("❌ Erro ao exibir faturamento!");
			System.out.println(e.getMessage());
		}
	}

	private static void gerenciarTabeaPrecos(TarifaDAO tarifaDAO, Scanner scanner) {
		try {
			System.out.println("\n===== GERENCIAR PREÇOS =====");
			System.out.println("1 - Exibir preços atuais");
			System.out.println("2 - Atualizar preços");
			System.out.print("Escolha: ");
			int opcao = scanner.nextInt();
			scanner.nextLine();

			if (opcao == 1) {
				tarifaDAO.exibirTarifasAtuais();
			} else if (opcao == 2) {
				System.out.print("Valor da 1ª hora: R$ ");
				double hora = scanner.nextDouble();
				System.out.print("Valor da hora extra: R$ ");
				double horaExtra = scanner.nextDouble();
				System.out.print("Valor da diária: R$ ");
				double diaria = scanner.nextDouble();
				scanner.nextLine();

				tarifaDAO.atualizarTarifas(hora, horaExtra, diaria);
			} else {
				System.out.println("❌ Opção inválida!");
			}
		} catch (SQLException e) {
			System.out.println("❌ Erro ao gerenciar preços!");
			System.out.println(e.getMessage());
		}
	}

	private static void exibirVagasDisponiveis(VagasDAO vagasDAO) {
		try {
			vagasDAO.exibirVagasDisponiveis();
		} catch (SQLException e) {
			System.out.println("❌ Erro ao exibir vagas!");
			System.out.println(e.getMessage());
		}
	}

	private static void exibirRelatorioGeral(RelatorioDAO relatorioDAO) {
		try {
			relatorioDAO.exibirRelatorioGeral(LocalDate.now());
		} catch (SQLException e) {
			System.out.println("❌ Erro ao exibir relatório!");
			System.out.println(e.getMessage());
		}
	}
}
