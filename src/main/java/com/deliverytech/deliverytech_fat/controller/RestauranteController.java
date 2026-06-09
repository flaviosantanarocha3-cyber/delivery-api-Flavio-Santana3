package com.deliverytech.deliverytech_fat.controller;

import com.deliverytech.deliverytech_fat.dto.ApiResponseWrapper;
import com.deliverytech.deliverytech_fat.dto.PagedResponseWrapper;
import com.deliverytech.deliverytech_fat.dto.req.RestauranteReqDTO;
import com.deliverytech.deliverytech_fat.dto.res.ProdutoResDTO;
import com.deliverytech.deliverytech_fat.dto.res.RestauranteResDTO;
import com.deliverytech.deliverytech_fat.service.ProdutoService;
import com.deliverytech.deliverytech_fat.service.RestauranteService;
import com.deliverytech.deliverytech_fat.service.MetricsService;
import com.deliverytech.deliverytech_fat.validation.ValidCEP;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController //Avisa ao Spring que esta classe processa requisições HTTP e devolve respostas em JSON (padrão REST).
@RequestMapping("/api/restaurantes")//Define a URL base para acessar os recursos de restaurantes.
@CrossOrigin(origins = "*") // Permite que o frontend (Web ou Mobile) acesse as rotas da API sem bloqueios de segurança do navegador (CORS)
@Validated // Ativa o motor de validação do Spring para checar se os dados enviados (como CEP ou e-mail) são válidos.
@Tag(name = "Restaurantes", description = "Operações relacionadas aos restaurantes") // Organiza e dá nome ao bloco de rotas dentro do Swagger.
public class RestauranteController {

    @Autowired // Organiza e dá nome ao bloco de rotas dentro do Swagger.
    private RestauranteService restauranteService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private MetricsService metricsService;

    @PostMapping // Define endpoint HTTP POST para criação de recursos (cadastro de restaurante).
    @Operation(summary = "Cadastrar restaurante", description = "Cria um novo restaurante no sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Restaurante criado com sucesso"),//  Created indica que uma requisição foi bem-sucedida
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),// Bad Request, indica que o servidor não conseguiu processar a solicitação
        @ApiResponse(responseCode = "409", description = "Restaurante já existe") // Conflict indica que a solicitação feita pelo cliente entrou em conflito 
    })
    public ResponseEntity<ApiResponseWrapper<RestauranteResDTO>> cadastrar(
            @Valid @RequestBody RestauranteReqDTO dto) {

        metricsService.incrementarPedidosProcessados();
        try {
            RestauranteResDTO restaurante = restauranteService.cadastrarRestaurante(dto);
            ApiResponseWrapper<RestauranteResDTO> response =
                new ApiResponseWrapper<>(true, restaurante, "Restaurante criado com sucesso");
            metricsService.incrementarPedidosComSucesso();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            metricsService.incrementarPedidosComErro();
            throw e; // Repassa erro para tratamento global
        }
    }

    @GetMapping // Para consulta listagem de restaurantes.
    @Operation(summary = "Listar restaurantes", description = "Lista restaurantes com filtros opcionais e paginação")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    })
    public ResponseEntity<PagedResponseWrapper<RestauranteResDTO>> listar(
            @Parameter(description = "Categoria do restaurante")
            @RequestParam(required = false) String categoria,
            @Parameter(description = "Status ativo do restaurante")
            @RequestParam(required = false) Boolean ativo,
            @Parameter(description = "Parâmetros de paginação")
            Pageable pageable) {

        Page<RestauranteResDTO> restaurantes =
            restauranteService.listarRestaurantes(categoria, ativo, pageable);

        PagedResponseWrapper<RestauranteResDTO> response =
            new PagedResponseWrapper<>(restaurantes);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar restaurante por ID", description = "Recupera um restaurante específico pelo ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<ApiResponseWrapper<RestauranteResDTO>> buscarPorId(
            @Parameter(description = "ID do restaurante")
            @PathVariable Long id) {

        RestauranteResDTO restaurante = restauranteService.buscarPorId(id);
        ApiResponseWrapper<RestauranteResDTO> response =
            new ApiResponseWrapper<>(true, restaurante, "Restaurante encontrado");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}") // atualiza dados de um restaurante existente.
    @Operation(summary = "Atualizar restaurante", description = "Atualiza os dados de um restaurante existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Restaurante updated com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ApiResponseWrapper<RestauranteResDTO>> atualizar(
            @Parameter(description = "ID do restaurante")
            @PathVariable Long id,
            @Valid @RequestBody RestauranteReqDTO dto) {

        RestauranteResDTO restaurante = restauranteService.atualizarRestaurante(id, dto);
        ApiResponseWrapper<RestauranteResDTO> response =
            new ApiResponseWrapper<>(true, restaurante, "Restaurante atualizado com sucesso");

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Ativar/Desativar restaurante", description = "Alterna o status ativo/inativo do restaurante")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status alterado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<ApiResponseWrapper<RestauranteResDTO>> alterarStatus(
            @Parameter(description = "ID do restaurante")
            @PathVariable Long id) {

        RestauranteResDTO restaurante = restauranteService.alterarStatusRestaurante(id);
        ApiResponseWrapper<RestauranteResDTO> response =
            new ApiResponseWrapper<>(true, restaurante, "Status alterado com sucesso");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar por categoria", description = "Lista restaurantes de uma categoria específica")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Restaurantes encontrados")
    })
    public ResponseEntity<ApiResponseWrapper<List<RestauranteResDTO>>> buscarPorCategoria(
            @Parameter(description = "Categoria do restaurante")
            @PathVariable String categoria) {

        List<RestauranteResDTO> restaurantes =
            restauranteService.buscarRestaurantesPorCategoria(categoria);

        ApiResponseWrapper<List<RestauranteResDTO>> response =
            new ApiResponseWrapper<>(true, restaurantes, "Restaurantes encontrados");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/taxa-entrega/{cep}")
    @Operation(summary = "Calcular taxa de entrega", description = "Calcula a taxa de entrega para um CEP específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Taxa calculada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<ApiResponseWrapper<BigDecimal>> calcularTaxaEntrega(
            @Parameter(description = "ID do restaurante")
            @PathVariable Long id,
            @Parameter(description = "CEP de destino")
            @PathVariable String cep) {

        BigDecimal taxa = restauranteService.calcularTaxaEntrega(id, cep);
        ApiResponseWrapper<BigDecimal> response =
            new ApiResponseWrapper<>(true, taxa, "Taxa calculada com sucesso");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/proximos/{cep}")
    @Operation(summary = "Restaurantes próximos", description = "Lista restaurantes próximos a um CEP")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Restaurantes próximos encontrados")
    })
    public ResponseEntity<ApiResponseWrapper<List<RestauranteResDTO>>> buscarProximos(
            @Parameter(description = "CEP de referência")
            @PathVariable @ValidCEP String cep,
            @Parameter(description = "Raio em km")
            @RequestParam(defaultValue = "10") Integer raio) {

        List<RestauranteResDTO> restaurantes =
            restauranteService.buscarRestaurantesProximos(cep, raio);

        ApiResponseWrapper<List<RestauranteResDTO>> response =
            new ApiResponseWrapper<>(true, restaurantes, "Restaurantes próximos encontrados");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{restauranteId}/produtos")
    @Operation(summary = "Produtos do restaurante", description = "Lista todos os produtos de um restaurante")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Produtos encontrados"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<ApiResponseWrapper<List<ProdutoResDTO>>> buscarProdutosDoRestaurante(
            @Parameter(description = "ID do restaurante")
            @PathVariable Long restauranteId,
            @Parameter(description = "Filtrar apenas disponíveis")
            @RequestParam(required = false) Boolean disponivel) {

        List<ProdutoResDTO> produtos =
            produtoService.buscarProdutosPorRestaurante(restauranteId, disponivel);

        ApiResponseWrapper<List<ProdutoResDTO>> response =
            new ApiResponseWrapper<>(true, produtos, "Produtos encontrados");

        return ResponseEntity.ok(response);
    }
}