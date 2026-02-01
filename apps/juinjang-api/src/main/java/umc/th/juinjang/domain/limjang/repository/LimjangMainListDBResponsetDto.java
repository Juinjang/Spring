package umc.th.juinjang.domain.limjang.repository;

import umc.th.juinjang.domain.image.model.Image;
import umc.th.juinjang.domain.limjang.model.LimjangPrice;
import umc.th.juinjang.domain.report.model.Report;

public record LimjangMainListDBResponsetDto(
    Long limjangId,
    Image image,
    String nickname,
    LimjangPrice limjangPrice,
    Report report,
    String address
    //      "limjangId": 8,
//          "image": null,
//          "nickname": "string",
//          "price": "20000",
//          "totalAverage": null,
//          "address": "string"

) {

}
