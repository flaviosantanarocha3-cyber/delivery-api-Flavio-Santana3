Delivery Tech API

Uma API de delivery moderna, robusta e escalável desenvolvida com **Java 21** e **Spring Boot 3.4.x**. O projeto aplica conceitos rigorosos de **Arquitetura em Camadas** e boas práticas de desenvolvimento de software.

 Arquitetura do Sistema
O projeto foi estruturado seguindo o padrão de **Arquitetura em Camadas (Layered Architecture)**, garantindo a separação de responsabilidades, facilidade de manutenção e isolamento do código:
* **Camada de Apresentação (`Controller`):** Responsável por expor os endpoints REST da API e receber as requisições HTTP do cliente.
* **Camada de Negócio (`Service`):** Onde residem as regras de negócio do sistema (validações, cálculos e fluxos de entrega).
* **Camada de Acesso a Dados (`Repository`):** Interface de comunicação direta com o banco de dados utilizando Spring Data JPA.
* **Camada de Configuração (`Config`):** Centraliza as definições de segurança (JWT), monitoramento e infraestrutura.
Tecnologias e Stack Utilizada

 Infraestrutura
* **Java 21 (LTS):** Utilização de recursos modernos da linguagem para maior performance.
* **Spring Boot 3.4.x:** Framework base para inicialização e gerenciamento do ecossistema.
* **Maven:** Gerenciador de dependências e automação de builds pelo terminal.

 Persistência & Performance
* **Spring Data JPA / Hibernate:** Abstração completa da camada de banco de dados.
* **H2 Database:** Banco de dados em memória de alta velocidade para o ambiente de desenvolvimento.
* **Spring Boot Cache & Redis:** Estratégia de cache distribuído para aceleração de respostas e economia de memória.

Segurança & Validação
* **Spring Security:** Controle de autenticação e autorização robusto.
* **JSON Web Tokens (JWT):** Emissão e validação de tokens para rotas protegidas.
* **Spring Validation:** Validação rigorosa dos dados de entrada nas requisições.

Observabilidade & Monitoramento
* **Spring Boot Actuator:** Indicadores automáticos de saúde do sistema (`/health`).
* **Micrometer & Prometheus:** Coleta de métricas, contadores de requisições e monitoramento de performance.
* **Brave & Zipkin:** Rastreamento distribuído (telemetria) de requisições de ponta a ponta.

Como Executar o Projeto no Terminal
Siga os passos abaixo para rodar a aplicação localmente de forma rápida:
1. **Clonar o repositório:**
   ```bash
   git clone [https://github.com/seu-usuario/deliverytech_fat.git](https://github.com/seu-usuario/deliverytech_fat.git)
   cd deliverytech_fat
Compilar e baixar as dependências:
Bash
mvn clean compile
Iniciar o servidor da aplicação:
Bash
mvn spring-boot:run
O servidor iniciará automaticamente. Se estiver utilizando a porta padrão, a API estará disponível em: http://localhost:8080
📖 Documentação da API (Swagger UI)
Com a aplicação rodando, você pode visualizar, testar e interagir com todos os endpoints e controladores (ex: DashboardController) diretamente pelo navegador através da interface gráfica do Swagger:
🔗 http://localhost:8080/swagger-ui/index.html
Desenvolvedores
Flavio Santana - Desenvolvedor Principal