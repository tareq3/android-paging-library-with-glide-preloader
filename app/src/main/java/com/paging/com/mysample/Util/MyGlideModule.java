package com.paging.com.mysample.Util;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/***
 * Created by mtita on 15,June,2019.
 */

@GlideModule
public class MyGlideModule extends AppGlideModule {


    // Disable manifest parsing to avoid adding similar modules twice.
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}


