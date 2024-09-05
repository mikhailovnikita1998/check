package org.example.service;

import org.example.audit.UserActionLog;
import org.example.repository.UserActionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserActionLogService {

    @Autowired
    private UserActionLogRepository actionLogRepository;

    public void logAction(String username, String action) {
        UserActionLog log = new UserActionLog(username, action);
        actionLogRepository.save(log);
    }
}