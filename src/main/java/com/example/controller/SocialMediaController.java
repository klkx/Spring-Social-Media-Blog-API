package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
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
    private MessageService messageService;
    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity registerAccount(@RequestBody Account account) {
        if(account.getUsername().length()>0 || account.getPassword().length()>=4){
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

    @PostMapping("/login")
    public ResponseEntity loginAccount(@RequestBody Account account){
        Optional<Account> foundAccount_opt = accountService.findAccountByUsername(account.getUsername());
        if(foundAccount_opt.isPresent()){
            Account foundAccount = foundAccount_opt.get();
            if(foundAccount.getPassword().equals(account.getPassword())){
                return ResponseEntity.status(200).body(foundAccount);
            }else{
                return ResponseEntity.status(401).body("password is incorrect.");
            }
        }else{
            return ResponseEntity.status(401).body("Account is not existed.");
        }
    }

    @PostMapping("/messages")
    public ResponseEntity saveMessage(@RequestBody Message message){
        if(message.getMessageText().length() > 0 && message.getMessageText().length() <=255){
            Optional<Account> foundAccount_opt = accountService.findAccountById(message.getPostedBy());
            if(foundAccount_opt.isPresent()){
                Message savedMessage = messageService.saveMessage(message);
                return ResponseEntity.status(200).body(savedMessage);
            }else{
                return ResponseEntity.status(400).body("The account of the message is not existed.");
            }
        }else{
            return ResponseEntity.status(400).body("The message is blank or over 255 chars.");
        }
    }

    @GetMapping("/messages")
    public ResponseEntity getAllMessages(){
        List<Message> messages = messageService.allMessages();
        return ResponseEntity.status(200).body(messages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity getMessageById(@PathVariable int messageId){
        Optional<Message> foundMessage_opt = messageService.findMessageById(messageId);
        Message message = foundMessage_opt.orElse(null);
        return ResponseEntity.status(200).body(message);
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity deleteMessageById(@PathVariable int messageId){
        Optional<Message> foundMessage_opt = messageService.findMessageById(messageId);
        if(foundMessage_opt.isPresent()){
            Message deletedMessage = foundMessage_opt.get();
            messageService.deleteMessageById(messageId);
            return ResponseEntity.status(200).body(1);
        }else{
            return ResponseEntity.status(200).body(null);
        }
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity updateMessageById(@PathVariable int messageId, @RequestBody Message message){
        String messageText = message.getMessageText();
        Optional<Message> foundMessage_opt = messageService.findMessageById(messageId);
        if(foundMessage_opt.isPresent()){
            if(messageText != null && messageText.length() >= 1 && messageText.length() <= 255){
                Message update_message = foundMessage_opt.get();
                update_message.setMessageText(messageText);
                messageService.saveMessage(update_message);
                return ResponseEntity.status(200).body(1);
            }else{
                return ResponseEntity.status(400).body("MessageText is blank or too long for update.");
            }
        }else{
            return ResponseEntity.status(400).body("Message is not existed to update");
        }
    }
    
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity getMessagesByAccountId(@PathVariable int accountId){
        List<Message> messages = messageService.findMessagesByAccountId(accountId);
        return ResponseEntity.status(200).body(messages);
    }
   
}
