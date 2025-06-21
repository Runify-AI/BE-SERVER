package com.example.runity.service;

import com.example.runity.DTO.EmailLookupResponseDTO;
import com.example.runity.enums.RunningType;

public interface EmailLookupService {
    EmailLookupResponseDTO findBynickNameAndRunningType(String nickName, RunningType runningType);
}
