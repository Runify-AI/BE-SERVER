package com.example.runity.service;

import com.example.runity.DTO.PreferenceRequestDTO;
import com.example.runity.DTO.PreferenceResponseDTO;
import com.example.runity.constants.ErrorCode;
import com.example.runity.domain.Preference;
import com.example.runity.domain.User;
import com.example.runity.util.JwtUtil;
import com.example.runity.error.CustomException;
import com.example.runity.repository.PreferenceRepository;
import com.example.runity.repository.UserRepository;
import com.example.runity.service.PreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PreferenceServiceImpl  implements PreferenceService {

    private final PreferenceRepository preferenceRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void createPreference(String token, PreferenceRequestDTO preferenceRequestDTO) {
        Long userId = jwtUtil.getUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        if (preferenceRepository.existsByUser(user)) {
            throw new CustomException(ErrorCode.DUPLICATE_PREFERENCE_EXISTS, "이미 선호도가 존재합니다.");
        }

        Preference preference = Preference.builder()
                .user(user)
                .preferencePlaces(preferenceRequestDTO.getPreferencePlaces())
                .preferenceRoutes(preferenceRequestDTO.getPreferenceRoutes())
                .preferenceAvoids(preferenceRequestDTO.getPreferenceAvoids())
                .preferenceEtcs(preferenceRequestDTO.getPreferenceEtcs())
                .build();

        preferenceRepository.save(preference);
    }

    @Override
    public PreferenceResponseDTO getPreference(String token) {
        Long userId = jwtUtil.getUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        Preference preference = preferenceRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.PREFERENCE_NOT_FOUND, "선호도를 찾을 수 없습니다."));

        return new PreferenceResponseDTO(preference);
    }

    @Override
    public void updatePreference(String token, PreferenceRequestDTO preferenceRequestDTO) {
        Long userId = jwtUtil.getUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        Preference preference = preferenceRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.PREFERENCE_NOT_FOUND, "선호도를 찾을 수 없습니다."));

        preference.updatePreference(
                preferenceRequestDTO.getPreferencePlaces(),
                preferenceRequestDTO.getPreferenceRoutes(),
                preferenceRequestDTO.getPreferenceAvoids(),
                preferenceRequestDTO.getPreferenceEtcs()
        );

        preferenceRepository.save(preference);
    }

    @Override
    public void deletePreference(String token) {
        Long userId = jwtUtil.getUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        Preference preference = preferenceRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.PREFERENCE_NOT_FOUND, "선호도를 찾을 수 없습니다."));

        preferenceRepository.delete(preference);
    }

}
