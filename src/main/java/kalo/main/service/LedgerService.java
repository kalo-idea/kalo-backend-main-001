package kalo.main.service;

import org.springframework.stereotype.Service;

import kalo.main.repository.LedgerRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LedgerService {
    private final LedgerRepository ledgerRepository;

}
