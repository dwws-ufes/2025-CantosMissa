# Cantos Para Missa

Sistema web para gestão e visualização de cifras de músicas litúrgicas utilizadas em missas. As músicas são associadas a tempos litúrgicos e momentos da missa, servindo, portanto, de recomendação para músicos da igreja. Além disso, as cifras contam com a funcionalidade de transposição de tom.

## Tecnologias utilizadas

- Java 21
- Jakarta EE (JSF, CDI, EJB, JPA)
- PrimeFaces
- Hibernate
- MySQL
- WildFly
- Maven

## Como executar o projeto

### 1. Clone o repositório:

   ```bash
   git clone https://github.com/dwws-ufes/2025-CantosMissa.git
   cd cantosparamissa
   ```

### 2. Configure o banco de dados MySQL

   Se não estiver instalado, execute (no Linux):
   ```bash
   sudo apt update
   sudo apt install mysql-server
   ```

   Dentro do prompt do MySQL, crie o banco de dados e o usuário:
   ```bash
   CREATE DATABASE cantosparamissa DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   CREATE USER 'dwws'@'localhost' IDENTIFIED BY 'dwws';
   GRANT ALL PRIVILEGES ON cantosparamissa.* TO 'dwws'@'localhost';
   FLUSH PRIVILEGES;
   ```

   Se desejar utilizar outra configuração para o banco de dados, altere o arquivo `src/main/webapp/WEB-INF/web.xml` dentro de `<data-source>`:
   ```java
    <data-source>
        <name>java:app/datasources/cantosparamissa</name>
        <class-name>com.mysql.cj.jdbc.MysqlDataSource</class-name>
        <server-name>localhost</server-name>
        <port-number>3306</port-number>
        <database-name>cantosparamissa</database-name>
        <user>dwws</user>
        <password><![CDATA[dwws]]></password>
    </data-source>
   ```

### 3. Faça deploy com o WildFly

  Primeiramente, se não possuí-lo em sua máquina, faça download [aqui](https://www.wildfly.org/downloads/).

  #### Resolvendo bug relacionado ao Jakarta Security:
  
  1. Começe criando a seguinte variável de ambiente com o caminho no qual o WildFly está instalado:
  ```bash
  export WILDFLY_HOME=/caminho/para/o/wildfly/no/seu/computador
  ```

  2. Execute o seguinte comando e crie um usuário administrador (você pode usar o usuário existente `admin`, indicando uma nova senha pra ele):
  ```bash
  $WILDFLY_HOME/bin/add-user.sh
  ```

  3. Feito isso, execute o servidor:
  ```bash
  $WILDFLY_HOME/bin/standalone.sh
  ```

  4. Em um navegador Web, abra o console de administração do WildFly em http://localhost:9990/ e entre com as credenciais do usuário que foi adicionado no segundo passo;

  5. Nos menus do console de administração, acesse Configuraion > Subsystems > Web > Application Security Domain > Other > View;

  6. Clique no botão Edit, modifique o valor do campo Integrated JASPI para OFF e clique em Save;

  7. Clique no botão Reload para recarregar o servidor.

  Resolvido o bug, vamos seguir com o deploy. Se estiver utilizando o Intellij IDEA, você pode fazer a integração com o WildFly seguindo este [tutorial](https://gitlab.labes.inf.ufes.br/labes/catalogo/-/wikis/plataformas/configura%C3%A7%C3%A3o-da-ide-para-desenvolvimento-jakarta-ee#configura%C3%A7%C3%A3o-do-intellij-idea).

  Para fazer o deploy manualmente, faça o seguinte:

  1. Para compilar o código e criar o pacote do projeto, execute:
  ```bash
  mvn package
  ```

  2. Depois, execute os seguinte comandos (altere a variável de ambiente `WILDFLY_HOME`):
  ```bash
  export WILDFLY_HOME=/caminho/para/o/wildfly/no/seu/computador
  export WILDFLY_DEPLOY=$WILDFLY_HOME/standalone/deployments 
  ln -s `pwd`/target/cantosparamissa/ $WILDFLY_DEPLOY/cantosparamissa.war
  touch $WILDFLY_DEPLOY/cantosparamissa.war.dodeploy
  ```

  3. Finalmente, inicie o servidor e acesse http://localhost:8080/cantosparamissa/ no navegador:
  ```bash
  $WILDFLY_HOME/bin/standalone.sh
  ```

  4. Se quiser mudar algo no código, execute o seguinte para fazer redeploy:
  ```bash
  mv $WILDFLY_DEPLOY/cantosparamissa.war.deployed $WILDFLY_DEPLOY/cantosparamissa.war.dodeploy
  ```

  5. Para fazer undeploy e remover a aplicação do servidor:
  ```bash
  mv $WILDFLY_DEPLOY/cantosparamissa.war.deployed $WILDFLY_DEPLOY/cantosparamissa.war.undeploy
  rm -r $WILDFLY_DEPLOY/cantosparamissa.*
  ```
