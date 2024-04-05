package br.com.lourivanrluz.tutorial.wallet;

import java.math.BigDecimal;
import java.util.UUID;

import br.com.lourivanrluz.tutorial.users.Users;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "wallets")
@Entity

public class Wallet {

        private @Id @GeneratedValue(strategy = GenerationType.UUID) UUID id;
        private Integer type;
        private BigDecimal balance;
        private BigDecimal credit;
        private Boolean isBlocked;

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "user_id", referencedColumnName = "id")
        Users user;

        public Wallet(UUID id) {
                this.id = id;
        }

        public void subBalance(BigDecimal value) {
                this.balance = this.balance.subtract(value);
        }

        public void addBalance(BigDecimal value) {
                this.balance = this.balance.add(value);
        }

        public void subCredit(BigDecimal value) {
                this.credit = this.credit.subtract(value);
        }

        public void addCredit(BigDecimal value) {
                this.credit = this.credit.add(value);
        }

        public void blockWallet() {
                this.isBlocked = true;
        }

        public void unblockWallet() {
                this.isBlocked = false;
        }
}
