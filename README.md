# API REST de Transações

Este projeto implementa uma API REST para realizar transações financeiras, oferecendo suporte para transações à vista e a prazo com possibilidade de notificação async e envio de email.

## Índice

- [Como Rodar](#como-rodar)
- [Endpoints](#endpoints)
  - [POST /transactions](#post-transactions)
- [Exemplos de Corpo da Requisição](#exemplos-de-corpo-da-requisição)


## Como Rodar

Para configurar este projeto, você precisará seguir os seguintes passos:

1. Fork do projeto.
2. Leia e configure suas variaveis de ambiente no diretorio "src/resources/application.properties".
3. Instalar o docker e o Docker-compose.
4. Na raiz do projeto Execute o "docker-compose up --build".
6. Em um outro terminal execute o comando "./mvnw spring-boot:run".
5. faça um café

## Regra de negocio

* Existe 2 tipos de wallet, tipo logista e tipo comum.
* Não será possivel transfeir de logista para comum.
* Não será possivel transferir se o pagador for o mesmo que recebedor.
* O pagador precisa ter o valor da transferencia.
* Para efetuar uma transação a carteira do pagador nao pode estar bloqueada.
* caso a compra seja parcelada o pagador precisa ter o valor da parcela na wallet e precisa ter o valor total da compra em credito.
* o valor do credito retornará ao pagador assim que as parcelas forem quitadas.
* Durante o debto automatico, caso o pagador nao tenha o valor da parcela a carteira será bloqueada.


## Endpoints
O servidor estará disponível em http://localhost:8080 por padrão.
### POST /transactions
Este endpoint é usado para realizar transações financeiras.

Parâmetros da Requisição
payer: ID do pagador.
payee: ID do recebedor.
amount: Valor da transação.
installments: Número de parcelas (0 para transação à vista).
typeTransaction: Tipo de transação (1 para à vista, 2 para a prazo).

Exemplo de Corpo da Requisição
* Transação à Vista
```` json
{
	"payer": 3,
	"payee": 2,
	"amount": 100,
	"installments": 3,
	"typeTransaction": 2
}
````
* Transação à prazo
```` json
{
	"payer": 3,
	"payee": 2,
	"amount": 100,
	"installments": 3,
	"typeTransaction": 2
}
````
### Get /transactions
Este endpoint é usado para retornar todas transações feitas.

Retorno 
```` json
[
	{
		"payer": 3,
		"payee": 2,
		"amount": 100,
		"installments": 3,
		"typeTransaction": 2
	},
	{
		"payer": 3,
		"payee": 2,
		"amount": 100,
		"installments": 3,
		"typeTransaction": 2
	}
]
````
continua...