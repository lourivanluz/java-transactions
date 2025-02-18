package br.com.lourivanrluz.tutorial.users;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.lourivanrluz.tutorial.wallet.Wallet;
import br.com.lourivanrluz.tutorial.wallet.WalletRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    public Users createUser(UserDto userDto) {
        Users user = UserDto.dtoToUser(userDto);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        Wallet wallet = new Wallet(null, user.getType(), BigDecimal.valueOf(0), BigDecimal.valueOf(0), false,
                user);
        walletRepository.save(wallet);
        user.setWallet(wallet);
        return userRepository.save(user);
    }
}
