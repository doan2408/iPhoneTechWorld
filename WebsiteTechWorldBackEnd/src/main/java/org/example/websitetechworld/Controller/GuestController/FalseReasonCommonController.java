package org.example.websitetechworld.Controller.GuestController;

import lombok.Getter;
import org.example.websitetechworld.Dto.Response.CommonResponse.FalseReasonResponse.FalseReasonResponse;
import org.example.websitetechworld.Enum.CaseReason.CaseType;
import org.example.websitetechworld.Services.CommonSerivces.FalseReasonCommonService.FalseReasonCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/false-reason")
public class FalseReasonCommonController {

    private final FalseReasonCommonService falseReasonCommonService;

    public FalseReasonCommonController(FalseReasonCommonService falseReasonCommonService) {
        this.falseReasonCommonService = falseReasonCommonService;
    }

    @GetMapping("/case-type")
    public ResponseEntity<?> getAllFalseReasonByCaseReason(@RequestParam("caseType") CaseType caseType) {
        List<FalseReasonResponse> falseReasonResponseList = falseReasonCommonService.getByCaseReason(caseType);
        return ResponseEntity.ok(falseReasonResponseList);
    }
}
