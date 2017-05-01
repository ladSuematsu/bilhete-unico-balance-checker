package com.ladsoft.view.text;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public abstract class Mask {

    public static String unmask(final String maskedString) {
        return maskedString
                .replaceAll("[.]", "")
                .replaceAll("[-]", "")
                .replaceAll("[/]", "")
                .replaceAll("[(]", "")
                .replaceAll("[)]", "");
    }

    public static TextWatcher get(final String maskTemplate, final EditText editText) {
        return new TextWatcher() {
            private boolean isUpdating;
            private String old = "";

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String unmaskedInput = unmask(charSequence.toString());
                String maskedOutput = "";

                if (isUpdating) {
                    old = unmaskedInput;
                    isUpdating = false;
                    return;
                }

                int pos = 0;
                for (char maskChar : maskTemplate.toCharArray()) {
                    if(pos >= unmaskedInput.length()) {
                        break;
                    }

                    if (maskChar != '#' && unmaskedInput.length() > old.length()) {
                        maskedOutput += maskChar;
                        continue;
                    }

                    maskedOutput += unmaskedInput.charAt(pos);
                    pos++;
                }

                isUpdating = true;
                editText.setText(maskedOutput);
                editText.setSelection(maskedOutput.length());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        };
    }
}
