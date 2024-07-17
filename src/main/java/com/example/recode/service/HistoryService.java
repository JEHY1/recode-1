package com.example.recode.service;

import com.example.recode.domain.History;
import com.example.recode.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;

    public List<History> findAllByUserId(long userId){
        return historyRepository.findAllByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("not found history"));
    }

    public History save(History history){
        return historyRepository.save(history);
    }

    public History findByProductIdAndUserId(long productId, long userId){
        return historyRepository.findByProductIdAndUserId(productId, userId)
                .orElse(null);
    }

    public void delete(History history){
        historyRepository.delete(history);
    }

    public void deleteByProductId(Long productId) { // productId로 History 삭제
        List<History> historyList = historyRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("not found history"));
        historyList.forEach(history -> historyRepository.deleteById(history.getHistoryId()));
    }
}
