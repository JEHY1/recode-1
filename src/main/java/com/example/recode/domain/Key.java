package com.example.recode.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "key_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Key {

    @Id
    @Column(name = "key_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long keyId;

    @Column(name = "key_name")
    String keyName;

    @Column(name = "key_ciphered_key")
    String keyCipheredKey;

    @Column(name = "key_decryption_key")
    String keyDecryptionKey;

    @Builder
    public Key(String keyName, String keyCipheredKey, String keyDecryptionKey) {
        this.keyName = keyName;
        this.keyCipheredKey = keyCipheredKey;
        this.keyDecryptionKey = keyDecryptionKey;
    }
}
