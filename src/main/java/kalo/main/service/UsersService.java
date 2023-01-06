package kalo.main.service;

import org.springframework.stereotype.Service;

import kalo.main.repository.UsersRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService {
    UsersRepository usersRepository;

    void join() {
        
    }
}
