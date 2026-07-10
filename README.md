# Sistema de Gerenciamento de Estacionamento

Sistema desenvolvido em **Java**, utilizando **JDBC** e **MySQL**, com foco na aplicação dos conceitos de Programação Orientada a Objetos, persistência de dados e regras de negócio.

## Tecnologias

* Java 17
* JDBC
* MySQL
* MySQL Workbench
* Eclipse IDE
* Git e GitHub

## Funcionalidades

* Registro de entrada de veículos
* Registro de saída com cálculo automático do tempo e valor
* Controle de vagas disponíveis
* Busca de veículo por placa
* Histórico de clientes
* Cálculo do faturamento diário
* Gerenciamento da tabela de preços
* Relatórios do estacionamento

## Regras de Negócio

### Registrar Entrada

* Localiza automaticamente uma vaga disponível.
* Valida a placa do veículo.
* Impede entradas duplicadas para veículos já presentes no pátio.
* Registra cliente, veículo e vaga.
* Atualiza o status da vaga para **OCUPADA**.

### Registrar Saída

* Localiza o veículo pela placa.
* Calcula automaticamente o tempo de permanência.
* Calcula o valor da estadia conforme a tabela de preços.
* Registra a forma de pagamento.
* Gera um recibo da operação.
* Libera a vaga e finaliza a entrada.

### Consultas

* Veículos atualmente no pátio.
* Histórico de entradas e saídas.
* Pesquisa de veículo por placa.
* Quantidade de vagas ocupadas e disponíveis.
* Faturamento diário.

## Estrutura do Projeto

```text
src
├── connection
├── dao
├── model
├── service
└── Main.java
```

## Banco de Dados

O sistema utiliza as seguintes tabelas:

* ENTRADA
* SAIDA
* VAGA
* TARIFA

## Conceitos Aplicados

* Programação Orientada a Objetos (POO)
* Arquitetura em camadas (DAO, Model e Service)
* JDBC
* CRUD
* SQL
* PreparedStatement
* Tratamento de exceções
* Versionamento com Git

## Melhorias Futuras

* Interface gráfica (JavaFX)
* Controle de usuários e permissões
* Relatórios em PDF
* Dashboard com indicadores
* Exportação para Excel
* Reserva de vagas

## Autor

**Danilo Moreira**

Projeto desenvolvido para fins acadêmicos e composição de portfólio, demonstrando conhecimentos em Java, JDBC, SQL e desenvolvimento de aplicações orientadas a objetos.
