package br.com.lourivanrluz.tutorial.credit;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lourivanrluz.tutorial.wallet.Wallet;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("credit")
public class CreditController {
    private CreditService creditService;

    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    @PutMapping("{id}")
    public Wallet putMethodName(@PathVariable Long id, @RequestBody Map<String, BigDecimal> body) {
        return creditService.HandlerCredit(id, body.get("value"));
    }

}
