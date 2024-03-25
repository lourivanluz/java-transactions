package br.com.lourivanrluz.tutorial.wallet;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("WALLETS")
public record Wallet(
                @Id Long id,
                String fullName,
                Long cpf,
                String email,
                String password,
                Integer type,
                BigDecimal balance,
                BigDecimal credit,
                Boolean blocked

) {
        public Wallet {
                balance = balance.setScale(2);
                credit = credit.setScale(2);
        }

        public Wallet subBalance(BigDecimal value) {
                return new Wallet(id, fullName, cpf, email, password, type, balance.subtract(value), credit, blocked);
        }

        public Wallet addBalance(BigDecimal value) {
                return new Wallet(id, fullName, cpf, email, password, type, balance.add(value), credit, blocked);
        }

        public Wallet subCredit(BigDecimal value) {
                return new Wallet(id, fullName, cpf, email, password, type, balance, credit.subtract(value), blocked);
        }

        public Wallet addCredit(BigDecimal value) {
                return new Wallet(id, fullName, cpf, email, password, type, balance, credit.add(value), blocked);
        }

        public Wallet blockWallet() {
                return new Wallet(id, fullName, cpf, email, password, type, balance, credit, true);
        }

        public Wallet unBlockWallet() {
                return new Wallet(id, fullName, cpf, email, password, type, balance, credit, false);
        }

}
