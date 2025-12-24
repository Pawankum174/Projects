package com.vaultcore.fintech.account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepo extends JpaRepository<Account, UUID> {}
