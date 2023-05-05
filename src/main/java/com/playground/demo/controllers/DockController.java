package com.playground.demo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/stations/{id}/docks")
@RequiredArgsConstructor
public class DockController {
}
