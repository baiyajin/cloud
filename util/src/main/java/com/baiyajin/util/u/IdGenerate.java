package com.baiyajin.util.u;

import java.util.UUID;

public class IdGenerate {
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
