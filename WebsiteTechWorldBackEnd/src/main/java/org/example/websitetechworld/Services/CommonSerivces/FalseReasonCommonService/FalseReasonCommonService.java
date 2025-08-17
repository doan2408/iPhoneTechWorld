package org.example.websitetechworld.Services.CommonSerivces.FalseReasonCommonService;

import org.example.websitetechworld.Dto.Response.CommonResponse.FalseReasonResponse.FalseReasonResponse;
import org.example.websitetechworld.Enum.CaseReason.CaseType;
import org.example.websitetechworld.Repository.LyDoXuLyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FalseReasonCommonService {
    private final LyDoXuLyRepository lyDoXuLyRepository;

    public FalseReasonCommonService(LyDoXuLyRepository lyDoXuLyRepository) {
        this.lyDoXuLyRepository = lyDoXuLyRepository;
    }

    public List<FalseReasonResponse> getByCaseReason(CaseType caseType){
        return lyDoXuLyRepository.findRealsonByType(caseType);
    }
}
