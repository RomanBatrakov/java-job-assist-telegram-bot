package com.batrakov.service.impl;

import com.batrakov.dao.AppUserRepository;
import com.batrakov.service.UserActivationService;
import com.batrakov.utils.CryptoTool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Реализация сервиса активации пользователя.
 * Класс реализует функциональность активации пользователя на основе
 * криптографического идентификатора пользователя.
 *
 * @version 1.0
 */
@RequiredArgsConstructor
@Service
public class UserActivationServiceImpl implements UserActivationService {
    private final AppUserRepository appUserRepository;
    private final CryptoTool cryptoTool;

    /**
     * Активация пользователя.
     * Метод активирует пользователя на основе криптографического идентификатора.
     *
     * @param cryptoUserId Криптографический идентификатор пользователя.
     * @return true, если активация прошла успешно, false - в противном случае.
     */
    @Override
    public boolean activation(String cryptoUserId) {
        var userId = cryptoTool.idOf(cryptoUserId);
        var optional = appUserRepository.findById(userId);
        if (optional.isPresent()) {
            var user = optional.get();
            user.setIsActive(true);
            appUserRepository.save(user);
            return true;
        }
        return false;
    }
}
