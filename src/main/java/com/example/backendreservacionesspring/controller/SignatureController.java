package com.example.backendreservacionesspring.controller;


import com.example.backendreservacionesspring.model.Signature;
import com.example.backendreservacionesspring.service.SignatureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/signatures")
public class SignatureController {

    private final SignatureService signatureService;

    public SignatureController(SignatureService signatureService) {
        this.signatureService = signatureService;
    }

    @GetMapping
    public List<Signature> findAllSignatures() {
        return  signatureService.findAll();
    }

    @GetMapping("/name")
    public ResponseEntity<Signature> findSignatureByName(@PathVariable String name){
        Optional<Signature> signature = signatureService.findByName(name);
        return signature.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
