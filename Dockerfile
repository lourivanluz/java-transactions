# Use uma imagem base com o JDK instalado
FROM openjdk:21-jdk-slim

# Instale o Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Defina o diretório de trabalho como /app
WORKDIR /app

# Copie o arquivo pom.xml e outros arquivos de configuração
COPY pom.xml /app/pom.xml

# Instale as dependências do Maven
RUN mvn dependency:go-offline

# Copie o restante do código-fonte
COPY src /app/src

# Compile o aplicativo usando o Maven
RUN mvn package -DskipTests

# # Comando para executar o aplicativo quando o contêiner iniciar
CMD ["java", "-jar", "target/tutorial-0.0.1-SNAPSHOT.jar"]