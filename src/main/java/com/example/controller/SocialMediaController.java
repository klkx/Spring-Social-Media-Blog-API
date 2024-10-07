package com.example.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    private AccountService accountService;
    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity registerAccount(@RequestBody Account account) {
        if(account.getUsername().length()>0 && account.getPassword().length()>=4){
            Optional<Account> foundAccount_opt = accountService.findAccountByUsername(account.getUsername());
            if(foundAccount_opt.isEmpty()){
                Account savedAccount = accountService.registerAccount(account);
                return ResponseEntity.status(200).body(savedAccount);
            }else{
                return ResponseEntity.status(409).body("Duplicated username.");
            }
        }else{
            return ResponseEntity.status(400).body("Empty username or too short password.");
        }
        
        
    }

    

}
