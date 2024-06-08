package com.example.ebankingbackend;

import com.example.ebankingbackend.dto.CustomerDTO;
import com.example.ebankingbackend.entities.CurrentAccount;
import com.example.ebankingbackend.entities.Customer;
import com.example.ebankingbackend.entities.Operation;
import com.example.ebankingbackend.entities.SavingAccount;
import com.example.ebankingbackend.enums.AccountStatus;
import com.example.ebankingbackend.enums.OperationType;
import com.example.ebankingbackend.exceptions.AccountNotFoundException;
import com.example.ebankingbackend.exceptions.BalanceNotSufficientException;
import com.example.ebankingbackend.exceptions.CustomerNotFoundException;
import com.example.ebankingbackend.repositories.BankAccountRepo;
import com.example.ebankingbackend.repositories.CustomerRepo;
import com.example.ebankingbackend.repositories.OperationRepo;
import com.example.ebankingbackend.service.BankAccountServiceImpl;
import com.example.ebankingbackend.util.BankAccountRequest;
import com.example.ebankingbackend.util.OperationRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EBankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EBankingBackendApplication.class, args);
	}

	//@Bean
	CommandLineRunner start(CustomerRepo customerRepo, BankAccountRepo bankAccountRepo, OperationRepo operationRepo){
		return args -> {
			Stream.of("Hassan", "Yassin", "Aicha").forEach(name -> {
				Customer customer = Customer.builder()
						.name(name)
						.email(name+"@gmail.com")
						.build();
				customerRepo.save(customer);
			});
			customerRepo.findAll().forEach(customer -> {
				CurrentAccount currentAccount = new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random()*90000);
				currentAccount.setStatus(AccountStatus.CREATED);
				currentAccount.setOverDraft(10000);
				currentAccount.setCreatedAt(new Date());
				currentAccount.setCustomer(customer);
				bankAccountRepo.save(currentAccount);

				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random()*100000);
				savingAccount.setStatus(AccountStatus.CREATED);
				savingAccount.setInterestRate(5.5);
				savingAccount.setCreatedAt(new Date());
				savingAccount.setCustomer(customer);
				bankAccountRepo.save(savingAccount);
			});
			bankAccountRepo.findAll().forEach(account -> {
				for (int i=0;i<10;i++){
					Operation operation = Operation.builder()
							.type(Math.random()>0.5 ? OperationType.CREDIT : OperationType.DEBIT)
							.amount(Math.random()*12000)
							.operationDate(new Date())
							.bankAccount(account)
							.build();
					operationRepo.save(operation);
				}
			});
		};
	}

	@Bean
	CommandLineRunner commandLineRunner(BankAccountServiceImpl bankAccountService){
		return args -> {
			Stream.of("Hassan", "Yassin", "Aicha").forEach(name -> {
				CustomerDTO customerDTO = CustomerDTO.builder()
						.name(name)
						.email(name+"@gmail.com")
						.build();
				bankAccountService.saveCustomer(customerDTO);
			});
			bankAccountService.listCustomers().forEach(customer -> {
				try {
					bankAccountService.saveBankAccount(new BankAccountRequest(Math.random() * 90000, "current", customer.getId(), 0, 8000));
					bankAccountService.saveBankAccount(new BankAccountRequest(Math.random() * 90000, "saving", customer.getId(), 5.5, 0));
				} catch (CustomerNotFoundException e) {
					e.printStackTrace();
				}
            });
			bankAccountService.listBankAccounts().forEach(account -> {
				for (int i = 0; i<10; i++){
                    try {
                        bankAccountService.credit(new OperationRequest(account.getId(),Math.random()*10000+5000,"credit"));
						bankAccountService.debit(new OperationRequest(account.getId(),Math.random()*10000+5000,"debit"));
                    } catch (AccountNotFoundException | BalanceNotSufficientException e) {
                        e.printStackTrace();
                    }
                }
			});
		};

	}

	/*@Bean
	CommandLineRunner commandLineRunner2(JdbcUserDetailsManager jdbcUserDetailsManager){
		return args -> {
			jdbcUserDetailsManager.createUser(User.withUsername("user1").password(passwordEncoder().encode("1234")).roles("USER").build());
			jdbcUserDetailsManager.createUser(User.withUsername("user2").password(passwordEncoder().encode("1234")).roles("USER").build());
			jdbcUserDetailsManager.createUser(User.withUsername("admin").password(passwordEncoder().encode("1234")).roles("USER","ADMIN").build());
		};
	}*/
	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
}
