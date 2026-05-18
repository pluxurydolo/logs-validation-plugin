package com.pluxurydolo.util

import java.security.SecureRandom

class PrefixGenerator {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom()

    static String generate() {
        SECURE_RANDOM.with { (0..<4).collect {  ('a'..'z')[nextInt(26)] }.join() }
    }
}
