package kr.flab.momukji.gateway.dto.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse {
    public ResultCode resultCode = ResultCode.SUCCESS;
}
