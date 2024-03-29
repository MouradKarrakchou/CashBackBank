package fr.teama.accountservice.components;

import fr.teama.accountservice.exceptions.BankUserWithEmailAlreadyExistException;
import fr.teama.accountservice.interfaces.UserRegistration;
import fr.teama.accountservice.models.BankUser;
import fr.teama.accountservice.models.Card;
import fr.teama.accountservice.repository.BankUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Authentification implements UserRegistration {
    @Autowired
    BankUserRepository bankUserRepository;
    @Override
    public BankUser registerUser(String firstName, String lastName, String email, String password) throws BankUserWithEmailAlreadyExistException {
        if (bankUserRepository.findByEmail(email) != null) {
            throw new BankUserWithEmailAlreadyExistException(email);
        }

        BankUser user = new BankUser(firstName, lastName, email, password);

        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder(16);
        StringBuilder cvv = new StringBuilder(3);
        for (int i = 0; i < 16; i++) {
            cardNumber.append(random.nextInt(10));
        }
        for (int i = 0; i < 3; i++) {
            cvv.append(random.nextInt(10));
        }
        Card card = new Card(cardNumber.toString(), "12/24", cvv.toString());

        StringBuilder iban = new StringBuilder(22);
        for (int i = 0; i < 22; i++) {
            iban.append(random.nextInt(10));
        }

        user.getBankAccount().setCard(card);
        user.getBankAccount().setIban("FR" + iban + "A12");

        return bankUserRepository.save(user);
    }
}
