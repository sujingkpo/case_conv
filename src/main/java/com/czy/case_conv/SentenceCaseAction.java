package com.czy.case_conv;

import java.util.Optional;

public class SentenceCaseAction extends SelectionReplacerAction {
    @Override
    protected String replace(String s) {
        return WordSplitConverter.convert(
                s,
                " ",
                (i, part) -> Optional.of(i == 0 ?
                        CamelCaseAction.ucfirst(part)
                        : part
                )
        );
    }
}
