# Proposta
Este projeto utiliza o Quarkus, o Supersonic Subatomic Java Framework.

Se você quiser saber mais sobre o Quarkus, visite o site: https://quarkus.io/.

## Executando a aplicação em modo de desenvolvimento
Você pode executar a aplicação em modo de desenvolvimento, que permite o live coding, utilizando o seguinte comando:

```bash
./mvnw compile quarkus:dev
```
> NOTA: O Quarkus agora inclui uma interface de usuário de desenvolvimento (Dev UI), disponível apenas em modo de desenvolvimento em http://localhost:8091/q/dev/.

## Empacotando e executando a aplicação
A aplicação pode ser empacotada utilizando o seguinte comando:

```bash
./mvnw package
```
Isso produzirá o arquivo **'quarkus-run.jar'** no diretório **'target/quarkus-app/'**.
Lembre-se de que não é um über-jar, pois as dependências são copiadas para o diretório **'target/quarkus-app/lib/'**.

A aplicação pode ser executada utilizando o seguinte comando: **'java -jar target/quarkus-app/quarkus-run.jar'**.

Se você desejar construir um über-jar, execute o seguinte comando:

```bash
./mvnw package -Dquarkus.package.type=uber-jar
```
A aplicação, empacotada como um über-jar, pode ser executada utilizando o seguinte comando: **'java -jar target/*-runner.jar'**.

## Criando um executável nativo
Você pode criar um executável nativo utilizando o seguinte comando:

```bash
./mvnw package -Pnative
```
Ou, se você não tiver o GraalVM instalado, pode executar a compilação nativa em um contêiner utilizando o seguinte comando:

```bash
./mvnw package -Pnative -Dquarkus.native.container-build=true
```
Em seguida, você pode executar seu executável nativo com o comando: **'./target/gateway-1.0-SNAPSHOT-runner'**

Se você quiser saber mais sobre a criação de executáveis nativos, consulte https://quarkus.io/guides/maven-tooling.

## Guias Relacionados
- SmallRye Reactive Messaging ([guia](https://quarkus.io/guides/reactive-messaging)): Produza e consuma mensagens e implemente aplicativos orientados a eventos e streaming de dados.
- SmallRye Reactive Messaging - Kafka Connector ([guia](https://quarkus.io/guides/kafka-reactive-getting-started)): Conecte-se ao Kafka com o Reactive Messaging.
- Hibernate ORM with Panache ([guia](https://quarkus.io/guides/hibernate-orm-panache)): Simplifique o código de persistência para o Hibernate ORM usando o padrão Active Record ou o padrão de repositório.
- JDBC Driver - MySQL ([guia](https://quarkus.io/guides/datasource)): Conecte-se ao banco de dados MySQL via JDBC.
