package com.marleyspoon.net

import com.contentful.java.cda.CDAClient

object ContentfulFactory {

    val get: CDAClient by lazy {
        CDAClient.builder()
            .setSpace("kk2bw5ojx476")
            .setToken("7ac531648a1b5e1dab6c18b0979f822a5aad0fe5f1109829b8a197eb2be4b84c")
            .build()
    }
}
