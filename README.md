# API para cadastro de empresas

A API recebe como dados de entrada o CNPJ, Nome, E-mail, Moeda de Trabalho, e Endereço contendo Logradouro, número, complemento, CEP, bairro e cidade. De todos esses campos, apenas o CNPJ é necessário. Os outros campos, caso não fornecidos, são preenchidos através do acesso à API Receita WS (https://receitaws.com.br/). 

O campo de moeda, apesar de não ser obrigatório, só pode ser inserido manualmente, pois não é possível obtê-lo via API externa. 

O objeto obtido nessa API também retorna um campo Cotação, que contém a cotação atual da moeda de trabalho, em relação ao Real, com valor atualizado no máximo nos últimos 20 minutos.

## Solução

No banco de dados, foi criada uma entidade Cotação, para ser o repositório de onde a API pega os dados de cotação. Caso a cotação tenha sido inserida há mais de 20 minutos, a API pega o dado de cotação utilizando a API de Moedas (http://docs.awesomeapi.com.br/api-de-moedas).

Cada vez que um dado de empresa é solicitado, um serviço recupera o valor da cotação internamente, e o atualiza caso ele tenha expirado, utilizando o Web Service externo.

## Execução

Para executar o Microsserviço, é necessário utilizar o comando
```
mvn clean package
```
E depois executar o arquivo `.jar` gerado utilizando o comando
```
java -jar target/*.jar
```
Ou simplesmente abrir o aquivo POM do projeto utilizando um editor como o IntelliJ Idea.

Para executar os testes unitários basta utilizar o comando
```
mvn test
```

## Swagger

O arquivo Swagger com o contrado dessa API, com detalhes dos objetos recebidos e retornados, e utilização de filtros e paginação, está na raiz do projeto, com o nome `swagger.yaml`. Para visualizá-lo, basta copiar seu conteúdo no editor de Swagger http://editor.swagger.io/.
