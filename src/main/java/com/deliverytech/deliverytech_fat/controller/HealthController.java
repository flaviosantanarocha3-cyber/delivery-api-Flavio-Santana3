package com.deliverytech.deliverytech_fat.controller;
//Importa a notação para criar endpoints GET.
import org.springframework.web.bind.annotation.GetMapping;
// Define que esta classe é um API REST.
import org.springframework.web.bind.annotation.RestController;
//Permite retornar respostas HTTP personalizadas.
import org.springframework.http.ResponseEntity;
//Classes para trabalhar com data e hora.
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//Classe Map para marmazenar dados no formato chave/valor.
import java.util.Map;

/**
 * Controller responsável pelos endpoints de monitoramento da aplicação
 * Demonstra o uso de recursos modernos do Java 21.
 * Sua função e informar se a API sobre o sistema.
 */
@RestController
public class HealthController {
    //Formato padrao utilizado para exibir data e hora.

    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

   /**
     * Endpoint para verificar o status da aplicação
     * url.
     * GET/helth.
     * Utilizado para verificar rapidaemte se a API esta online.
     * @return Map com informações de saúde da aplicação
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        // Usando Map.of() (Java 9+) para criar mapa imutável
        //Status da aplicação.
        Map<String, String> healthInfo = Map.of(
            "status", "UP",
            "timestamp", LocalDateTime.now().format(FORMATTER),
            //Nome do serviço.
            "service", "Delivery API JAVA",
            //Versão do java utilizada.
            "javaVersion", System.getProperty("java.version"),
            //Versão do Spring Boot.
            "springBootVersion", getClass().getPackage().getImplementationVersion() != null
                ? getClass().getPackage().getImplementationVersion() : "3.2.x",
                //Anbiente atual.
            "environment", "development"
        );
        // Retorna um HTTP (OK) com os dados.

        return ResponseEntity.ok(healthInfo);
    }   
/**
     * Endpoint com informações detalhadas da aplicação.
     * Retorna um objeto contendo informações gerais do sistema.
     * Demonstra o uso de Records (Java 14+)
     * @return AppInfo com dados da aplicação
     */
    @GetMapping("/info")
    public ResponseEntity<AppInfo> info() {
        //Cria um objeto AppInfo com os dados da aplicação.
        AppInfo appInfo = new AppInfo(
            "Delivery Tech API",
            "1.0.0",
            "Anderson Demoner",
            System.getProperty("java.version"),
            "Spring Boot 3.2.x",
            LocalDateTime.now().format(FORMATTER),
            "Sistema de delivery moderno desenvolvido com as mais recentes tecnologias Java"
        );
        //Retorna HTT 200 (ok).

        return ResponseEntity.ok(appInfo);
    }   
 /**
     * Record para demonstrar recurso do Java 14+ (disponível no JDK 21)
     * Records são classes imutáveis ideais para DTOs
     * Record e um recurso moderno do java.
     * funciona como uma classe simplificada para armazenar dados
     * O java cria automaticamente:
     * construtor.
     * Getters()
     * toString
     * equals()
     * hashCode()
     * ideal para DTOS.
     */
    public record AppInfo(
        //Nome da aplicação.
        String application,
        //Versão do sistema.
        String version,
        //Desenvolverdor 
        String developer,
        //versão do java.
        String javaVersion,
        //Framework  utilizado
        String framework,
        //Data da hora da consulta.
        String timestamp,
        //Descrição do sistema.
        String description
    ) {
        // Construtor compacto para validação (opcional)
        //Executado automaticamente quando o objeto e criado.
        //utilizado para validaçoes.
        public AppInfo {
            //Verifica se o nome da
            if (application == null || application.isBlank()) {
                throw new IllegalArgumentException("Application name cannot be null or blank");
            }
        }
    }
}
