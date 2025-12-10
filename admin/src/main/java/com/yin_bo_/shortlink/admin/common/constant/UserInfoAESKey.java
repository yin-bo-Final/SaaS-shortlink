package com.yin_bo_.shortlink.admin.common.constant;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;


@Data
@Component
public class UserInfoAESKey {

    private final AES aes;


    public UserInfoAESKey() {
        this.aes = new AES(
                Mode.CBC,
                Padding.PKCS5Padding,
                "yin_bo_shortlink".getBytes(StandardCharsets.UTF_8),
                "yin_bo_shortlink".getBytes(StandardCharsets.UTF_8)
        );
    }

}
