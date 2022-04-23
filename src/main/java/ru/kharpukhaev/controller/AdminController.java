package ru.kharpukhaev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kharpukhaev.entity.Client;
import ru.kharpukhaev.entity.enums.Role;
import ru.kharpukhaev.repository.ClientRepository;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
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
    public String clientSave(@RequestParam Map<String, String> form,
                             @RequestParam("clientId") Client client,
                             @RequestParam("roles") Set<String> roles) {

//        Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());

//        client.getRoles().clear();
//        for (String key : form.keySet()) {
//            if (roles.contains(key)) {
//                client.getRoles().add(Role.valueOf(key));
//            }
//        }
        clientRepository.save(client);
        return "redirect:/admin";
    }
}
