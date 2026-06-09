package com.deliverytech.deliverytech_fat.controller;
//importa a classe reponsavel pelas regras de negocio.
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.deliverytech_fat.dto.req.ClienteReqDTO;
import com.deliverytech.deliverytech_fat.dto.res.ClienteResDTO;
import com.deliverytech.deliverytech_fat.service.ClienteService;

import jakarta.validation.Valid;
//Define esta classe como uma API .
@RestController
@RequestMapping("/api/clientes")
//Permite acesso a API por aplicaçoes externas.
@CrossOrigin(origins = "*")
public class ClienteController {
//Injeta automaticamante o serviço de clientes.
    @Autowired
    private ClienteService clienteService;
//Cadastra um novo cliente.
    @PostMapping
    public ResponseEntity<ClienteResDTO> cadastrarCliente(@Valid @RequestBody ClienteReqDTO dto) {
        //Chama o service para salvar o cliente.
        ClienteResDTO cliente = clienteService.cadastrarCliente(dto);
        //Retorna status 201 (Created).
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }
//Busca o cliente pelo ID.
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResDTO> buscarPorId(@PathVariable Long id) {
        //Busca o cliente pelo banco.
        ClienteResDTO cliente = clienteService.buscarClientePorId(id);
        //Retorna status 200 ok.
        return ResponseEntity.ok(cliente);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResDTO>> listarClientesAtivos() {
        List<ClienteResDTO> clientes = clienteService.listarClientesAtivos();
        return ResponseEntity.ok(clientes);
    }
//Atualiza od dados de um cliente.
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResDTO> atualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteReqDTO dto) {
                //Atualiza o cliente informado.
        ClienteResDTO cliente = clienteService.atualizarCliente(id, dto);
        //Retorna o cliente informado
        return ResponseEntity.ok(cliente);
    }
//Ativa ou desativa um cliente.
    @PatchMapping("/{id}/status")
    public ResponseEntity<ClienteResDTO> ativarDesativarCliente(@PathVariable Long id) {
        //Altera o status do cliente.
        ClienteResDTO cliente = clienteService.ativarDesativarCliente(id);
        //Retorna o novo status.
        return ResponseEntity.ok(cliente);
    }
//Busca p cliente por email.
    @GetMapping("/email/{email}")
    public ResponseEntity<ClienteResDTO> buscarPorEmail(@PathVariable String email) {
        //Realiza a busca pelo email.
        ClienteResDTO cliente = clienteService.buscarClientePorEmail(email);
        //Retorna o cliente encontrado.
        return ResponseEntity.ok(cliente);
    }
}
