# Gerador de Relatórios Jasper
Microsserviço destinado a criação de relatórios Jasper (PDF)

## Requisitos
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
