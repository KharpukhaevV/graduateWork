package ru.kharpukhaev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kharpukhaev.entity.Client;
import ru.kharpukhaev.entity.enums.Role;
import ru.kharpukhaev.repository.ClientRepository;

import java.util.Set;

@Controller
@RequestMapping("/admin")
//@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    private final ClientRepository clientRepository;

    public AdminController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping
    public String clientList(Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        return "admin";
    }

    @GetMapping("{client}")
    public String clientEdit(@PathVariable Client client, Model model) {
        model.addAttribute("client", client);
        model.addAttribute("roles", Role.values());
        return "clientEdit";
    }

    @PostMapping("/client")
    public String clientSave(@RequestParam("clientId") Client client, @RequestParam("roles") Set<Role> roles) {

        client.setRoles(roles);
        clientRepository.save(client);
        return "redirect:/admin";
    }
}
