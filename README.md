# Exercício Zup Bootcamp

## Resumo

Uma API para simular a criação de uma conta em banco digital (batizado aqui de MaoBank).

## Contexto

Todo cliente precisa solicitar uma proposta de criação de nova conta de pessoa física antes de qualquer
outra coisa e este é justamente processo que precisamos implementar. Só que tal processo precisa
ser dividido em várias etapas, caso o contrário o cliente seria obrigado a passar um grande número 
de informações e poderíamos perder tudo por conta de uma falha de internet, falta de bateria no 
celular etc. A ideia do projeto seria minimizar esse problema. Aqui uma lista com o fluxo básico de criação de
proposta e algumas funcionalidades extras que foram sugeridas: 

- No primeiro passo precisamos de algumas informações básicas.
- No segundo passo o cliente precisa colocar os dados do endereço.
- No terceiro passo o cliente precisa enviar um documento de identificação.
- Agora precisamos mostrar tudo que foi enviado para o cliente do aceite. 
- Caso a proposta seja aceita pelo cliente, uma nova conta deve ser criada em função daquela proposta.
- No primeiro acesso pós-aprovação precisamos passar pelo processo de confirmação de identidade e criação de nova senha. 
- Por último vamos simular a integração com um endpoint externo onde é necessário checar se determinado CPF tem uma nova transferência para aquele banco, agencia e conta. 

## Executando o projeto

Sugiro que execute o projeto utilizando Intellij (https://www.jetbrains.com/pt-br/idea/).

Clone o projeto em seguida acesse a pasta em ./docker/kafka e execute o comando:

```
docker-compose up -d
```
Este arquivo cria um container rodando kafka (https://kafka.apache.org/) para desenvolvimento. Podemos assim simular o consumo de uma transferência bancária.

Após, execute o comando:

```
./mvnw spring-boot:run
```

Em relação ao banco de dados, estamos utilizando o h2 (https://www.h2database.com/), um banco de dados em memória, então não é preciso instalar em separado. Você pode visualizar o banco acessando http://localhost:8080/h2-console.
 
Para testar a API você pode utilizar o arquivo "_maobank.postman_collection.json" para ser importado no Postman (https://www.postman.com/). Ou ainda utilizar o Swagger (https://swagger.io/) acessando http://localhost:8080/swagger-ui.html#/.


### Visualizando e-mails

Para visualizar os e-mails enviados pelo sistema durante a fase de desenvolvimento, sugiro o uso do papercut https://github.com/ChangemakerStudios/Papercut-SMTP. Após instalá-lo é possível testar os endpoints e visualizar os e-mails de maneira simples.