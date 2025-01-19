# SpaceGalaxy Minecraft Backend

Esse foi um desafio proposto no servidor do discord da spacegalaxy https://discord.gg/spacelaxy, o desafio proposto foi esse abaixo:

---
### Desafio de Back End

**Introdução**: Chegamos no Minecraft, finalmente! ... Calma aí..., por que toda vez que eu saio e entro no servidor eu perco todos os meus itens e basicamente tudo o que fiz, casas, blocos adicionados, quebrados e nada mais está aqui, parece que o servidor não está salvando as coisas que eu faço, então eu mesmo vou criar o sistema para salvar:

- Primeiro de tudo, vamos fazer o inventário parar de deletar assim que eu sair, então vou criar rotas para que eu consiga adicionar itens no inventário, remover (quando eu dropar), mudar as posições dos itens no inventário e mudar a quantidade de itens de um slot do inventário para outro, acho que isso já vai resolver essa parte

- Agora, preciso fazer o sistema de durabilidade funcionar, então vou criar na minha API os endpoints para remover pontos de durabilidade quando eu tomar um hit, ou quebrar algo, e também fazer com que o back-end me retorne quando o item for quebrar, ou seja, quando a durabilidade chegar a 0

- Pra finalizar, só vou precisar agora de uns endpoints para que eu consiga salvar as coordenadas X, Y e Z e onde eu consiga colocar os blocos, remover (quebrar) ou mudar para outro bloco (cada bloco tem um ID específico)

> Acho que com isso vou conseguir fazer um sistema lixo ainda, mas pelo menos já gerenciável para não perder tudo o que eu fiz/construí no servidor, que servidor paia em

---

# Tecnologia usada

Para implementar a solução eu utilizei java 21 com quarkus, e para acesso ao banco de dados foi Hibernate com Panache, banco de dados postgres.

# Como usar

Uma forma facil de usar é olhar a documentação disponivel em https://doc.desafio.ovidio.dev/ lá é possivel verificar como funciona os endpoints e também testar em um ambiente de produção https://desafio.ovidio.dev/

Se quiser subir localmente so clonar o projeto, ou somente baixar o docker-compose.yml já basta, e executar o comando:

```
docker compose up -d
```

dessa forma ira subir um container postgres e um container docker com o backend.