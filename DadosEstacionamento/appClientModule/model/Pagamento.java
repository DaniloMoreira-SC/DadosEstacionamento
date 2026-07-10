package model;

public enum Pagamento {
	DINHEIRO("Dinheiro", 0.0), CARTAO_CREDITO("Cartão de Crédito", 0.0), CARTAO_DEBITO("Cartão de Débito", 0.0),
	PIX("PIX", 0.0);

	private String descricao;
	private double desconto;

	Pagamento(String descricao, double desconto) {
		this.descricao = descricao;
		this.desconto = desconto;
	}

	public String getDescricao() {
		return descricao;
	}

	public double getDesconto() {
		return desconto;
	}

	public static void listarOpcoes() {
		System.out.println("\n===== FORMA DE PAGAMENTO =====");
		int i = 1;
		for (Pagamento p : Pagamento.values()) {
			System.out.println(i + " - " + p.getDescricao());
			i++;
		}
		System.out.println("================================");
	}

	public static Pagamento obterPorIndice(int indice) {
		return Pagamento.values()[indice - 1];
	}
}
