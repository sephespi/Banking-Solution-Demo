package demo.onlinebanking.repositories;

import demo.onlinebanking.models.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {


    @Query(value = "SELECT * FROM accounts WHERE user_id = :user_id", nativeQuery = true)
    List<Account> getUserAccountById(@Param("user_id") int user_id);

    @Query(value = "SELECT sum(balance) FROM accounts WHERE user_id = :user_id", nativeQuery = true)
    BigDecimal getTotalBalance(@Param("user_id") int user_id);

    @Modifying
    @Query(value = "INSERT INTO accounts (user_id,account_number, account_name, account_type) VALUES " +
            "(:user_id, :account_number, :account_name, :account_type)", nativeQuery = true)
    @Transactional
    void createNewAccount(@Param("user_id") int user_id,
                           @Param("account_number") String account_number,
                           @Param("account_name") String account_name,
                           @Param("account_type") String account_type);

}
