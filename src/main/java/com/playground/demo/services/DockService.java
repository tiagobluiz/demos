package com.playground.demo.services;

import com.playground.demo.persistence.repositories.DockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DockService {

    private final DockRepository dockRepository;
}
