<h1 align="center" style="font-weight: bold;">Gerador de Relatórios Jasper 💻📋</h1>

<p>Autor: Inácio Souza Rocha</p>
<a href="https://www.linkedin.com/in/inacio-souza/" target="_blank">
  <img src="https://img.shields.io/badge/-LinkedIn-0077B5?logo=linkedin&logoColor=white&style=flat-square"/>
</a>

## ✅  Descrição do Projeto

Microsserviço de geração de relatórios genéricos, relatórios no formato PDF contendo qualquer tipo de assunto.<br>
O objetivo desse serviço é centralizar toda a infraestrutura necessária para a criação de relatórios,
permitindo que outros sistemas consumam essa funcionalidade de forma simples.

Os sistemas clientes ficam responsáveis apenas por cadastrar os templates de relatórios e solicitar sua geração, enviando os dados necessários na requisição.<br>
Dessa forma, a complexidade da geração de relatórios é totalmente abstraída pelo microsserviço.

## ✅ Arquitetura
- Padrão de comunicação REST.
- Desenvolvido utilizando a arquitetura em camadas. Cada componente lógico do sistema está separado na sua respectiva camada.
- Na medida do possível, prezei pela simplicidade ao desenvolver os componentes do serviço,
com isso diminui a dificuldade de futuras manutenções no sistema. 

## ✅ Principais Casos de Uso

 - Cadastrar relatório (template jasper)
 - Cadastrar versão de relatório
 - Gerar relatório (PDF)

## ✅ Considerações / Instruções

### Criação de templates jrxml

 - Quando precisar de um field com casas decimais, utilize o wrapper Float do java.<br>
No momento, quando utilizamos a classe BigDecimal no template recebemos erro ao tentar gerar o relatório.
Isso será corrigido em versões posteriores da API;

 - O nome de um parâmetro sempre deve estar em maiúsculo;

 - Ao utilizar subrereports, estes devem ser passado como parâmetro para o template do relatório principal. <br>
Crie o novo parâmetro no template principal e nas propriedades do subreport adicione-o ao campo "Expression";

 - O nome do arquivo correspondente ao subreport deve ser igual ao nome do parâmetro no relatório principal;

 - O arquivo principal deve conter a palavra "main" no nome;

 - Sempre utilize atributos planos, wrappers do java, nos fields. Nunca adicione uma classe referente ao seu modelo de negócio nos fields do template.

### Submissão dos templates jrxml
- Ao cadastrar um relatório na API, todos os arquivos jrxml relacionados - o template principal e seus subreports - devem, obrigatoriamente serem compactados no formato zip;
- Ainda que o relatório seja formado por um único arquivo jrxml este deve ser compactado no formato zip;

### Sobre a geração dos relatórios
 - Uma limitação do projeto é que os relatórios só podem ser gerados no formato PDF. 

## ✅🔐 Segurança

Este serviço foi desenvolvido para ser utilizado por outros projetos que necessitam terceirizar a lógica de geração de relatórios.<br>
Para atender a esse cenário, foi criada a entidade Cliente, que representa um usuário consumidor do serviço.

### Estrutura de entidades

Para entender melhor o papel da entidade Cliente na aplicação, considere a seguinte estrutura:<br>
Existe uma entidade Sistema. Cada Relatório está vinculado a um Sistema.<br>
Por exemplo: um relatório de funcionários pode estar associado ao sistema "RH Simplificado".<br>
A entidade Sistema representa uma aplicação externa que se comunica com o serviço de geração de relatórios.<br>

### Relacionamento entre Sistema e Cliente

Um mesmo Sistema pode possuir vários Clientes.
Por exemplo, o sistema "RH Simplificado" pode ter os seguintes clientes:

Prefeitura de Konoha
Empresa Sésamo

Apesar de estarem vinculados ao mesmo Sistema, cada Cliente possui seus próprios relatórios, não sendo permitido o compartilhamento entre eles.

### Autenticação via API Key

O acesso ao serviço de geração de relatórios é concedido individualmente para cada Cliente, por meio de uma chave de API.<br>
Ao realizar o cadastro de um Cliente pelo endpoint responsável, a chave de API é gerada e retornada na resposta.<br>
Essa chave deve ser utilizada pelo Cliente em todas as requisições ao serviço.
O formato da chave de API é o seguinte:

hash-gerado-automaticamente.id-do-cliente

### Usuário administrador

O serviço também possui a entidade Usuário, que representa o administrador da aplicação.<br>
É o Usuário quem gerencia os Clientes, utilizando os endpoints administrativos.<br>
O acesso do Usuario à aplicação é feito por meio de login e senha, que são definidos no momento da inicialização do serviço e obtidos a partir de variáveis de ambiente.

## ✅ Especificações Técnicas

### Linguagens e Tecnologias Utilizadas

- Java 17
- Maven
- Spring Boot (3.5.9)
- JPA, Hibernate, Spring Data
- PostgreSQL
- FlywayDb (Criação do modelo)
- JasperReport (Motor de geração dos relatórios)
- Junit, Mockito, Testcontainers

### Para subir o sistema (Recomendação)
- Tenha o Java 17 e maven configurados na sua máquina
- Suba uma instância de container docker do postgres (Crie a database: generate-report )
- Clone o projeto
- Abra o projeto no intellij
- Execute o comando `mvn clean install` no terminal dentro da pasta do projeto
- Rode a aplicação pelo intellij