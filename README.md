# Gerador de Relatórios Jasper

## Descrição

Este projeto fornece um serviço genérico de geração de relatórios para aplicações em uma arquitetura de microsserviços.

O serviço permite o cadastro de templates de relatórios no formato JasperReports, que podem ser reutilizados posteriormente para a geração de relatórios dinâmicos. A geração do relatório ocorre a partir dos dados enviados na requisição, combinados com o template previamente cadastrado.

Ao utilizar este serviço, aplicações consumidoras não precisam implementar a lógica de geração de relatórios internamente. Dessa forma, os desenvolvedores podem se concentrar exclusivamente no design dos templates Jasper e no envio dos dados necessários, reduzindo o esforço de desenvolvimento e promovendo reutilização e padronização.

## Arquitetura
Este sistema foi desenvolvido utilizando a arquitetura em camadas e o padrão de comunicação REST.
Cada componente lógico do sistema está separado na sua respectiva camada.
Boas práticas de código foram aplicadas, interfaces foram utilizadas de forma estratégica para evitar o acoplamento entre as partes.

## Principais Casos de Uso

<ul>
    <li>Cadastrar relatório (template jasper)</li>
    <li>Cadastrar versão de relatório</li>
    <li>Gerar relatório (PDF)</li>
</ul>

## Instruções

### Criação de templates jrxml

Quando precisar de um field com casas decimais, utilize o wrapper Float do java.
No momento, quando utilizamos a classe BigDecimal no template recebemos erro ao tentar gerar o relatório.
Isso será corrigido em versões posteriores da API;

O nome de um parâmetro sempre deve estar em maiúsculo;

Um subrereport deve ser passado como parâmetro para o template do relatório principal. 
Crie novo parâmetro no template principal e nas propriedades do subreport adicione-o ao campo "Expression";

