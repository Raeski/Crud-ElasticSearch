
<!-- SOBRE O PROJETO -->
## SOBRE O PROJETO

Crud utilizando Spring Boot e ElasticSearch, para aprendizado. 


<!-- GETTING STARTED -->
## Instalação

### Pré requisitos

* Insomnia/Postman ( Para testar os endpoints ) 
  
* Alguma IDE que rode Java  como Eclipse, Intellij... 

* Docker  


### Instalação

1. Pegue o link do repositório https://github.com/Raeski/crud-place.git
2. Clone o repo
   ```sh
   git clone https://github.com/Raeski/Crud-ElasticSearch
   ``
3. No terminal execute docker-compose up (dentro do diretório do projeto)

4. No insomnia teste os endpoins no localhost:8080

```
    Exemplo de JSON :
    {
      "id": "1",
      "name": "teste",
      "cpf": "11111111111"
    }
 ```

   ```JS
   POST /bulk - criar vários usuários
   
   POST /search - Busca por um ou mais usuários
   
   POST  - Cria um único usuário
   
   DELETE /delete - Deleta o usuário
   
   PUT /update - Atualiza o usuário que foi passado
   ```


<p>Feito por <b>Gustavo Raeski</b>  :octocat: | - gustavoraeski@outlook.com


