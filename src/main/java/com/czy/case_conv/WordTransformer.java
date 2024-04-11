package com.czy.case_conv;

import java.util.Optional;

interface WordTransformer {
    Optional<String> transform(int i, String s);
}
