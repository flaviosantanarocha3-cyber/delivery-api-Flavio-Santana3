package com.deliverytech.deliverytech_fat.controller;

// DTO que recebe os dados para criar os pedidos.
import com.deliverytech.deliverytech_fat.dto.req.PedidoReqDTO;
// DTO que devolve os dados do pedido para o cliente.
import com.deliverytech.deliverytech_fat.dto.res.PedidoResDTO;
// DTO que representa os itens do pedido.
import com.deliverytech.deliverytech_fat.dto.ItemPedidoDTO;
// DTO que representa o status do pedido.
import com.deliverytech.deliverytech_fat.dto.StatusPedidoDTO;
// Classe responsável pelas regras de negócio dos pedidos.
import com.deliverytech.deliverytech_fat.service.PedidoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * Controller responsável pelo gerenciamento de pedidos.
 * Aqui chegam as requisições HTTP relacionadas aos pedidos.
 */
@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoService pedidoService;

    // CORREÇÃO: Substituído @Autowired por Injeção via Construtor (Melhor Prática)
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // Cadastra um novo pedido.
    // POST /api/pedidos
    @PostMapping
    public ResponseEntity<PedidoResDTO> criarPedido(@Valid @RequestBody PedidoReqDTO dto) {
        // Chama a camada de serviço.
        PedidoResDTO pedido = pedidoService.criarPedido(dto);
        // Retorna HTTP 201 (Created).
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    // Busca de pedido por ID.
    // GET /api/pedidos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResDTO> buscarPorId(@PathVariable Long id) {
        PedidoResDTO pedido = pedidoService.buscarPedidoPorId(id);
        return ResponseEntity.ok(pedido);
    }

    // Lista de pedidos de um cliente.
    // GET /api/pedidos/cliente/{clienteId}
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PedidoResDTO>> buscarPorCliente(@PathVariable Long clienteId) {
        List<PedidoResDTO> pedidos = pedidoService.buscarPedidosPorCliente(clienteId);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Atualiza o status de um pedido.
     * Status possíveis: PENDENTE, CONFIRMADO, PREPARADO, ENTREGUE, CANCELADO.
     * * PATCH /api/pedidos/{id}/status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResDTO> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusPedidoDTO statusDTO) {
        PedidoResDTO pedido = pedidoService.atualizarStatusPedido(id, statusDTO.getStatus());
        return ResponseEntity.ok(pedido);
    }

    // Cancela o pedido.
    // DELETE /api/pedidos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarPedido(@PathVariable Long id) {
        pedidoService.cancelarPedido(id);
        // Operação realizada com sucesso, sem retorno de conteúdo (HTTP 204).
        return ResponseEntity.noContent().build();
    }

    /**
     * Calcula o valor total de um pedido com base em uma lista de itens.
     * * POST /api/pedidos/calcular
     */
    @PostMapping("/calcular")
    public ResponseEntity<BigDecimal> calcularTotal(@Valid @RequestBody List<ItemPedidoDTO> itens) {
        BigDecimal total = pedidoService.calcularTotalPedido(itens);
        return ResponseEntity.ok(total);
    }
}