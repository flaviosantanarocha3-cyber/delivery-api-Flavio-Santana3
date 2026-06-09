package com.deliverytech.deliverytech_fat.controller;

import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private MeterRegistry meterRegistry;

    @Hidden
    @GetMapping
    public ModelAndView dashboard() {
        return new ModelAndView("redirect:/dashboard.html"); 
    }

    @GetMapping("/api/metrics")
    public ResponseEntity<Map<String, Object>> getMetricsData() {
        Map<String, Object> metrics = new HashMap<>();

        // Métricas de pedidos
        metrics.put("pedidos_total", getCounterValue("delivery.pedidos.total"));
        metrics.put("pedidos_sucesso", getCounterValue("delivery.pedidos.sucesso"));
        metrics.put("pedidos_erro", getCounterValue("delivery.pedidos.erro"));
        metrics.put("receita_total", getCounterValue("delivery.receita.total") / 100.0);

        // Métricas de performance
        metrics.put("tempo_medio_pedido", getTimerMean("delivery.pedido.processamento.tempo"));
        metrics.put("tempo_medio_banco", getTimerMean("delivery.database.consulta.tempo"));

        // Métricas de sistema
        metrics.put("memoria_usada", getGaugeValue("jvm.memory.used"));
        metrics.put("memoria_max", getGaugeValue("jvm.memory.max"));
        metrics.put("cpu_usage", getGaugeValue("system.cpu.usage"));

        // Métricas de estado
        metrics.put("usuarios_ativos", getGaugeValue("delivery.usuarios.ativos"));
        metrics.put("produtos_estoque", getGaugeValue("delivery.produtos.estoque"));

        // Health status
        metrics.put("health_status", "UP");

        return ResponseEntity.ok(metrics);
    }

    private double getCounterValue(String name) {
        var counter = meterRegistry.find(name).counter();
        return counter != null ? counter.count() : 0.0;
    }

    private double getTimerMean(String name) {
        var timer = meterRegistry.find(name).timer();
        return timer != null ? timer.mean(TimeUnit.MILLISECONDS) : 0.0;
    }

    private double getGaugeValue(String name) {
        var gauge = meterRegistry.find(name).gauge();
        return gauge != null ? gauge.value() : 0.0;
    }
}