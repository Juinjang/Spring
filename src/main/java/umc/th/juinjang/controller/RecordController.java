package umc.th.juinjang.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.th.juinjang.apiPayload.ApiResponse;
import umc.th.juinjang.model.dto.limjang.LimjangMemoResponseDTO;
import umc.th.juinjang.model.dto.limjang.LimjangUpdateRequestDTO;
import umc.th.juinjang.model.dto.record.RecordRequestDTO;
import umc.th.juinjang.model.dto.record.RecordResponseDTO;
import umc.th.juinjang.model.entity.Member;
import umc.th.juinjang.service.recordService.RecordService;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecordController {

    @Autowired
    private RecordService recordService;



    //등록
    @Operation(summary = " 녹음 등록 API")
    @PostMapping("/record")
    public  ApiResponse<RecordResponseDTO.RecordDto> uploadRecord( @AuthenticationPrincipal Member member,
            @RequestPart(name = "file", required = true) MultipartFile file,
            @RequestPart RecordRequestDTO.RecordDto recordRequestDTO) {
        try{
            RecordResponseDTO.RecordDto result = recordService.uploadRecord(member, recordRequestDTO, file);
            return ApiResponse.onSuccess(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = " 녹음 삭제 API")
    @DeleteMapping("/record/{recordId}")
    public ApiResponse<String> deleteRecord(@AuthenticationPrincipal Member member, @PathVariable(name="recordId") Long recordId){
        try{
            String result = recordService.deleteRecord(member, recordId);
            return ApiResponse.onSuccess(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Operation(summary = " 녹음 전체 조회 API")
    @GetMapping("/record/all/{limjangId}")
    public ApiResponse <List<RecordResponseDTO.RecordDto>> getAllRecord(@AuthenticationPrincipal Member member, @PathVariable(name="limjangId") Long limjangId){
        try{
            List<RecordResponseDTO.RecordDto> recordList = recordService.getAllRecord(member, limjangId);
            return ApiResponse.onSuccess(recordList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = " 녹음 3개 조회 및 임장 메모 조회 API")
    @GetMapping("/record/{limjangId}")
    public ApiResponse <RecordResponseDTO.RecordMemoDto> getThreeRecord(@AuthenticationPrincipal Member member, @PathVariable(name="limjangId") Long limjangId){
        try{
            RecordResponseDTO.RecordMemoDto recordMemo = recordService.getThreeRecord(member, limjangId);

            return ApiResponse.onSuccess(recordMemo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = " 메모 생성 및 수정 API")
    @PostMapping("/memo/{limjangId}")
    public ApiResponse<LimjangMemoResponseDTO.MemoDto> createLimjangMemo(@AuthenticationPrincipal Member member, @PathVariable(name="limjangId") Long limjangId, @RequestBody LimjangUpdateRequestDTO.LimjangMemoDto memoDto){
        return ApiResponse.onSuccess(recordService.createLimjangMemo(member, limjangId, memoDto.getMemo()));
    }

    @Operation(summary = " 녹음 스크립트 내용 수정 API")
    @PatchMapping("/record/content/{recordId}")
    public ApiResponse<RecordResponseDTO.RecordDto> updateRecordContent(@AuthenticationPrincipal Member member, @PathVariable(name="recordId") Long recordId, @RequestBody RecordRequestDTO.RecordContentDto contentDto ){
        return ApiResponse.onSuccess(recordService.updateRecordContent(member, recordId, contentDto.getRecordScript()));
    }

    @Operation(summary = " 녹음 스크립트 제목 수정 API")
    @PatchMapping("/record/title/{recordId}")
    public ApiResponse<RecordResponseDTO.RecordDto> updateRecordTitle(@AuthenticationPrincipal Member member, @PathVariable(name="recordId") Long recordId, @RequestBody RecordRequestDTO.RecordTitleDto titleDto){
        return ApiResponse.onSuccess(recordService.updateRecordTitle(member, recordId, titleDto.getRecordName()));
    }
}