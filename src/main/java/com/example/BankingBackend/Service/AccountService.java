package com.example.BankingBackend.Service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
@Service
public interface AccountService {
    Map<String, Long> accountStats();
}
