package com.playground.demo.models;

import com.fasterxml.jackson.annotation.JsonAlias;

public record Station(@JsonAlias("id_expl") String id,
                      @JsonAlias("num_bicicletas") int availableBikes,
                      @JsonAlias("num_docas") int capacity) {
}
