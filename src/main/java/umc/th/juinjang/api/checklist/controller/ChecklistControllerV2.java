package umc.th.juinjang.api.checklist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
@Validated
public class ChecklistControllerV2 {
    int test;
}
