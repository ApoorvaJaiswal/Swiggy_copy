package com.example.user.swiggy_copy;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

public class Card {
    private String imge;
    private String tex;


    public String getImageText() {
        return imge;
    }

    public void setImageText(String imge) {
        this.imge = imge;
    }

    public String getText() {
        return tex;
    }

    public void setText(String tex) {
        this.tex = tex;
    }
}
