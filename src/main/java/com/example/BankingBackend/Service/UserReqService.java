package com.example.BankingBackend.Service;
import com.example.BankingBackend.Model.UserRequests;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserReqService {
    public ResponseEntity<?> createRequest(UserRequests userRequests);
    public List<UserRequests> getAllPendingReq();
    public Optional<UserRequests> PendingRequestsById(int id);
}
