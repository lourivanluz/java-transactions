ALTER TABLE users
ADD CONSTRAINT fk_wallet_id
FOREIGN KEY (wallet_id) REFERENCES wallets(ID);