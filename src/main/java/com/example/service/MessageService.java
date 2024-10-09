package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }


    public Message saveMessage(Message message){
        return messageRepository.save(message);
    }

    public List<Message> allMessages(){
        return messageRepository.findAll();
    }

    public Optional<Message> findMessageById(int id){
        return messageRepository.findById(id);
    }

    public void deleteMessageById(int id){
        messageRepository.deleteById(id);
    }

    public List<Message> findMessagesByAccountId(int accountId){
        return messageRepository.findByPostedBy(accountId);
    }
}
