package com.example.mixer.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MixerResponse {
    private String status;
    private Optional<String> depositAddress;
    private Optional<Set<String>> invalidAddress;
}
