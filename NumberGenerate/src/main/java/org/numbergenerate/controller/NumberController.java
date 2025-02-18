package org.numbergenerate.controller;

import org.numbergenerate.service.NumberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/numbers")
@Tag(name = "Number Generator", description = "API для генерации уникального номера заказа")
class NumberController {
    private final NumberService numberService;

    public NumberController(NumberService numberService) {
        this.numberService = numberService;
    }

    @PostMapping
    @Operation(summary = "Генерация уникального номера заказа", description = "Возвращает сгенерированный уникальный номер заказа")
    public String generateOrderNumber() {
        return numberService.generateUniqueOrderNumber();
    }
}


