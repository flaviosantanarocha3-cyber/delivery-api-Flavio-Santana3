package com.deliverytech.deliverytech_fat.controller;

// Classe que padroniza as respostas da API.
import com.deliverytech.deliverytech_fat.dto.ApiResponseWrapper;
// DTO que recebe os dados enviados pelo usuario.
import com.deliverytech.deliverytech_fat.dto.req.ProdutoReqDTO;
// DTO que devolve os dados para o cliente
import com.deliverytech.deliverytech_fat.dto.res.ProdutoResDTO;
// CORREÇÃO: Importação adicionada para corrigir o erro de compilação
import com.deliverytech.deliverytech_fat.service.ProdutoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

// Define esta classe como um controller REST. Todas as respostas serão enviadas em formato JSON.
@RestController
// Define a rota principal da API
@RequestMapping("/api/produtos")
// Permite acesso externo a API.
@CrossOrigin(origins = "*")
// Cria a categoria "produtos" dentro do SWAGGER.
@Tag(name = "Produtos", description = "Operações relacionadas aos produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    // CORREÇÃO: Substituído @Autowired no atributo por Injeção via Construtor (Melhor Prática)
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // Endpoint responsável por cadastrar um novo produto no sistema
    @PostMapping
    @Operation(summary = "Cadastrar produto", description = "Cria um novo produto no sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<ApiResponseWrapper<ProdutoResDTO>> cadastrar(
            // Recebe os dados enviados no corpo da requisição e executa as validações do DTO.
            @Valid @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados do produto a ser criado"
            ) ProdutoReqDTO dto) {

        // Chama a camada de serviço para salvar o produto.
        ProdutoResDTO produto = produtoService.cadastrar(dto);
        // Monta a resposta padronizada da API.
        ApiResponseWrapper<ProdutoResDTO> response =
            new ApiResponseWrapper<>(true, produto, "Produto criado com sucesso");

        // Retorna HTTP 201 (Created).
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Busca um produto pelo ID. Método HTTP: GET.
    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Recupera um produto específico pelo ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Produto encontrado"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ApiResponseWrapper<ProdutoResDTO>> buscarPorId(
            @Parameter(description = "ID do produto")
            @PathVariable Long id) {

        ProdutoResDTO produto = produtoService.buscarProdutoPorId(id);
        ApiResponseWrapper<ProdutoResDTO> response =
            new ApiResponseWrapper<>(true, produto, "Produto encontrado");

        return ResponseEntity.ok(response);
    }

    // Atualiza os dados de um produto existente. Método HTTP: PUT.
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto", description = "Atualiza os dados de um produto existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ApiResponseWrapper<ProdutoResDTO>> atualizar(
            @Parameter(description = "ID do produto")
            @PathVariable Long id,
            @Valid @RequestBody ProdutoReqDTO dto) {

        ProdutoResDTO produto = produtoService.atualizarProduto(id, dto);
        ApiResponseWrapper<ProdutoResDTO> response =
            new ApiResponseWrapper<>(true, produto, "Produto atualizado com sucesso");

        return ResponseEntity.ok(response);
    }

    // Remove um produto do sistema. Método HTTP: DELETE.
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover produto", description = "Remove um produto do sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Produto removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
        @ApiResponse(responseCode = "409", description = "Produto possui pedidos associados")
    })
    public ResponseEntity<Void> remover(
            @Parameter(description = "ID do produto")
            @PathVariable Long id) {

        produtoService.removerProduto(id);
        return ResponseEntity.noContent().build();
    }

    // Alterna a disponibilidade do produto. Método HTTP: PATCH.
    @PatchMapping("/{id}/disponibilidade")
    @Operation(summary = "Alterar disponibilidade", description = "Alterna a disponibilidade do produto")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Disponibilidade alterada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ApiResponseWrapper<ProdutoResDTO>> alterarDisponibilidade(
            @Parameter(description = "ID do produto")
            @PathVariable Long id) {

        ProdutoResDTO produto = produtoService.alterarDisponibilidade(id);
        ApiResponseWrapper<ProdutoResDTO> response =
            new ApiResponseWrapper<>(true, produto, "Disponibilidade alterada com sucesso");

        // CORREÇÃO: Corrigido o comentário para refletir o retorno real 200 (OK) com corpo de resposta.
        return ResponseEntity.ok(response);
    }

    // Lista produtos de uma categoria. Método HTTP: GET.
    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar por categoria", description = "Lista produtos de uma categoria específica")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Produtos encontrados")
    })
    public ResponseEntity<ApiResponseWrapper<List<ProdutoResDTO>>> buscarPorCategoria(
            @Parameter(description = "Categoria do produto")
            @PathVariable String categoria) {

        List<ProdutoResDTO> produtos = produtoService.buscarProdutosPorCategoria(categoria);
        ApiResponseWrapper<List<ProdutoResDTO>> response =
            new ApiResponseWrapper<>(true, produtos, "Produtos encontrados");

        return ResponseEntity.ok(response);
    }

    // Busca o produto pelo nome. Método HTTP: GET.
    @GetMapping("/buscar")
    @Operation(summary = "Buscar por nome", description = "Busca produtos pelo nome")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    public ResponseEntity<ApiResponseWrapper<List<ProdutoResDTO>>> buscarPorNome(
            @Parameter(description = "Nome do produto")
            // Captura o parâmetro da URL (?nome=...)
            @RequestParam String nome) {

        List<ProdutoResDTO> produtos = produtoService.buscarProdutosPorNome(nome);
        ApiResponseWrapper<List<ProdutoResDTO>> response =
            new ApiResponseWrapper<>(true, produtos, "Busca realizada com sucesso");

        return ResponseEntity.ok(response);
    }
}