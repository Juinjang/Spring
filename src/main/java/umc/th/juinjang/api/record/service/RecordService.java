package umc.th.juinjang.api.record.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import umc.th.juinjang.common.ExceptionHandler;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.LimjangHandler;
import umc.th.juinjang.api.record.service.converter.LimjangMemoConverter;
import umc.th.juinjang.api.record.service.converter.RecordConverter;
import umc.th.juinjang.api.limjang.service.response.LimjangMemoResponseDTO;
import umc.th.juinjang.api.record.controller.request.RecordRequestDTO;
import umc.th.juinjang.api.record.service.response.RecordResponseDTO;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.record.model.Record;
import umc.th.juinjang.domain.limjang.repository.LimjangRepository;
import umc.th.juinjang.domain.record.repository.RecordRepository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecordService {


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.uploadPath}")
    private String defaultUrl;

    @Autowired
    private final AmazonS3Client amazonS3Client;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private LimjangRepository limjangRepository;


    public RecordResponseDTO.RecordDTO uploadRecord(Member member, RecordRequestDTO.RecordDto recordRequestDTO, MultipartFile multipartFile) throws IOException {

        if(limjangRepository.findLimjangByLimjangIdAndMemberIdAndDeletedIsFalse(recordRequestDTO.getLimjangId(), member).isEmpty()){
            throw new ExceptionHandler(ErrorStatus.LIMJANG_NOTFOUND_ERROR);
        }
        Limjang limjang = limjangRepository.findById(recordRequestDTO.getLimjangId()).get();
        String recordName = "새로운 녹음 "+String.valueOf(limjang.getRecordCount()+1);

        limjangRepository.incrementRecordCount(limjang.getLimjangId());

        if(multipartFile != null) {
            String originalFilename = multipartFile.getOriginalFilename();
            String newfileName = UUID.randomUUID()+"_"+originalFilename;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());


            //S3에 저장
            amazonS3Client.putObject(bucket, "record/"+newfileName, multipartFile.getInputStream(), metadata);

            //DB에 저장
            Record record = RecordConverter.toEntity(recordRequestDTO, amazonS3Client.getUrl(bucket, "record/"+newfileName).toString(), limjang, recordName);
            Record saveRecord = recordRepository.save(record);

            return RecordConverter.toDto(record);

        }
        else{
            throw new ExceptionHandler(ErrorStatus._BAD_REQUEST);
        }
    }


    public String deleteRecord(Member member, Long recordId) {

        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.RECORD_NOT_FOUND));

        if (!Objects.equals(record.getLimjangId().getMemberId().getMemberId(), member.getMemberId())) {
            throw new ExceptionHandler(ErrorStatus.RECORD_NOT_FOUND);
        }

        try{
            String keyName = record.getRecordUrl().replace(defaultUrl+"/", "");

            boolean isObjectExist = amazonS3Client.doesObjectExist(bucket, keyName);

            if (!isObjectExist) {
                throw new FileNotFoundException();
            } else {
                //S3에서 삭제
                amazonS3Client.deleteObject(bucket, keyName);

                //db에서 삭제
                recordRepository.deleteById(recordId);
            }

        } catch (Exception e) {
//            throw new Exception(e);
        }
        return "삭제 성공했습니다.";
    }

    public List<RecordResponseDTO.RecordDTO> getAllRecord(Member member, Long limjangId) {
        Limjang limjang = limjangRepository.findLimjangByLimjangIdAndMemberIdAndDeletedIsFalse(limjangId, member)
                .orElseThrow(() -> new LimjangHandler(ErrorStatus.LIMJANG_NOTFOUND_ERROR));

        List<Record> records = recordRepository.findAllByLimjangIdOrderByRecordIdDesc(limjang);

        return RecordConverter.toDtoList(records);

    }

    public RecordResponseDTO.RecordMemoDto getThreeRecord(Member member, Long limjangId) {
        Limjang limjang = limjangRepository.findLimjangByLimjangIdAndMemberIdAndDeletedIsFalse(limjangId, member)
                .orElseThrow(() -> new LimjangHandler(ErrorStatus.LIMJANG_NOTFOUND_ERROR));
        List<Record> records = recordRepository.findTop3ByLimjangIdOrderByRecordIdDesc(limjang);

        return RecordConverter.toDto(records, limjang);

    }

    public LimjangMemoResponseDTO.MemoDto createLimjangMemo(Member member, Long limjangId, String memo) {


        Limjang limjang = limjangRepository.findLimjangByLimjangIdAndMemberIdAndDeletedIsFalse(limjangId, member)
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.LIMJANG_NOTFOUND_ERROR));

        limjang.updateMemo(memo);

        Limjang updatedLimjang = limjangRepository.save(limjang);

//
        return LimjangMemoConverter.toDto(updatedLimjang);
    }

    public RecordResponseDTO.RecordDTO updateRecordContent(Member member, Long recordId, String content) {

        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.RECORD_NOT_FOUND));

        Limjang limjang = limjangRepository.findById(record.getLimjangId().getLimjangId()).orElseThrow(()
                -> new ExceptionHandler(ErrorStatus.LIMJANG_NOTFOUND_ERROR));

        if(limjang.getMemberId().getMemberId() != member.getMemberId()){
            throw new ExceptionHandler(ErrorStatus.RECORD_NOT_FOUND);
        }

        record.updateRecordContent(content);

        Record updatedRecord = recordRepository.save(record);

        return RecordConverter.toDto(updatedRecord);
    }

    public RecordResponseDTO.RecordDTO updateRecordTitle(Member member, Long recordId, String title) {

        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.RECORD_NOT_FOUND));

        Limjang limjang = limjangRepository.findById(record.getLimjangId().getLimjangId()).orElseThrow(()
                -> new ExceptionHandler(ErrorStatus.LIMJANG_NOTFOUND_ERROR));

        if(limjang.getMemberId().getMemberId() != member.getMemberId()){
            throw new ExceptionHandler(ErrorStatus.RECORD_NOT_FOUND);
        }

        record.updateRecordTitle(title);

        Record updatedRecord = recordRepository.save(record);

        return RecordConverter.toDto(updatedRecord);
    }
}
