<h1 align="center" style="font-weight: bold;">Gerador de Relatórios Jasper 💻📋</h1>

<p>Autor: Inácio Souza Rocha</p>
<a href="https://www.linkedin.com/in/inacio-souza/" target="_blank">
  <img src="https://img.shields.io/badge/-LinkedIn-0077B5?logo=linkedin&logoColor=white&style=flat-square"/>
</a>


## ☑️ Descrição do Projeto

Este projeto fornece um serviço genérico de geração de relatórios para aplicações em uma arquitetura de microsserviços.

O serviço permite o cadastro de templates de relatórios no formato JasperReports, que podem ser reutilizados posteriormente para a geração de relatórios dinâmicos. A geração do relatório ocorre a partir dos dados enviados na requisição, combinados com o template previamente cadastrado.

Ao utilizar este serviço, aplicações consumidoras não precisam implementar a lógica de geração de relatórios internamente. Dessa forma, os desenvolvedores podem se concentrar exclusivamente no design dos templates Jasper e no envio dos dados necessários, reduzindo o esforço de desenvolvimento e promovendo reutilização e padronização.



## ☑️ Arquitetura
Este sistema foi desenvolvido utilizando a arquitetura em camadas e o padrão de comunicação REST.
Cada componente lógico do sistema está separado na sua respectiva camada.
Boas práticas de código foram aplicadas, interfaces foram utilizadas de forma estratégica para evitar o acoplamento entre as partes.

## ☑️ Principais Casos de Uso

 - Cadastrar relatório (template jasper)
 - Cadastrar versão de relatório
 - Gerar relatório (PDF)

## ☑️ Considerações / Instruções

### Criação de templates jrxml

 - Quando precisar de um field com casas decimais, utilize o wrapper Float do java.
No momento, quando utilizamos a classe BigDecimal no template recebemos erro ao tentar gerar o relatório.
Isso será corrigido em versões posteriores da API;

 - O nome de um parâmetro sempre deve estar em maiúsculo;

 - Ao utilizar subrereports, estes devem ser passado como parâmetro para o template do relatório principal. 
Crie o novo parâmetro no template principal e nas propriedades do subreport adicione-o ao campo "Expression";

 - O nome do arquivo correspondente ao subreport deve ser igual ao nome do parâmetro no relatório principal;

 - O arquivo principal deve conter a palavra "main" no nome;

 - Sempre utilize atributos planos, wrappers do java, nos fields. Nunca adicione uma classe referente ao seu modelo de negócio nos fields do template.

### Submissão dos templates jrxml
- Ao cadastrar um relatório na API, todos os arquivos jrxml relacionados - o template principal e seus subreports - devem, obrigatoriamente serem compactados no formato zip;
- Ainda que o relatório seja formado por um único arquivo jrxml este deve ser compactado no formato zip;

### Sobre a geração dos relatórios
 - Uma limitação do projeto é que os relatórios só podem ser gerados no formato PDF. 

## ☑️ Especificações Técnicas

### Linguagens e Tecnologias Utilizadas

- Java 17
- Spring Boot 3.5.9
- PostgreSQL
- JasperReport
