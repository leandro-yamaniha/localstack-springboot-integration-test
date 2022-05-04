Feature:
  Como engenheiro de software
  Eu quero realizar um teste de integracao automatizado entre o microservico e os servicos associados
  Para que garantir que os fluxos do processos funcione corretamente

Scenario: Gravar dados no banco de dados Dynamo através de uma chamada REST
  Given microservico e aws iniciados
  When realizar um cadastro de consumidor
  Then quero consultar os dados do consumidor
