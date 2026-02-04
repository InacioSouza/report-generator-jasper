# Gerador de Relatórios Jasper

## Descrição

Este projeto fornece um serviço genérico de geração de relatórios para aplicações em uma arquitetura de microsserviços.

O serviço permite o cadastro de templates de relatórios no formato JasperReports, que podem ser reutilizados posteriormente para a geração de relatórios dinâmicos. A geração do relatório ocorre a partir dos dados enviados na requisição, combinados com o template previamente cadastrado.

Ao utilizar este serviço, aplicações consumidoras não precisam implementar a lógica de geração de relatórios internamente. Dessa forma, os desenvolvedores podem se concentrar exclusivamente no design dos templates Jasper e no envio dos dados necessários, reduzindo o esforço de desenvolvimento e promovendo reutilização e padronização.

## Arquitetura
Este sistema foi desenvolvido utilizando a arquitetura em camadas e o padrão de comunicação REST.
Cada componente lógico do sistema está separado na sua respectiva camada.
Boas práticas de código foram aplicadas, interfaces foram utilizadas de forma estratégica para evitar o acoplamento entre as partes.

## Casos de Uso

<ol>
  <li>
    <span>Genérico (independente de modelo)</span>
    <ul>
      <li>
        Deve atender a requisições de diferentes sistemas com seus respectivos modelos
      </li>
    </ul>
  </li>

  <li>
    <span>Deve haver um end-point para cadastro dos templates de relatório Jasper</span>
    <ul>
      <li>
        O usuário criará templetes para os seus relatórios considerando os dados que deseja mostrar e subimeterá o template ao microsserviço, o qual o usará para gerar o relatório
      </li>
    </ul>
  </li>
</ol>

### Outras funcionalidades interessantes
<ul>
    <li>Versionamento de templates (Usuário pode cadastrar várias versões para um mesmo template)</li>
    <li>Adição de sub-relatórios (envio de template por pacote '.zip')</li>
</ul>
