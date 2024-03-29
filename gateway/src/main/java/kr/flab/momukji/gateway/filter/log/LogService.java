package kr.flab.momukji.gateway.filter.log;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LogService {
    
    private final LogRepository logRepository;

    public void save(Log log) {
        logRepository.save(log);
    }

    public List<Log> findAll() {
        return logRepository.findAll();
    }

    public long getOrderAmount(Long storeId) {
        return logRepository.countOrders(storeId);
    }
}
