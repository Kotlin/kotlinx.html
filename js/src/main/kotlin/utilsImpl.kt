package kotlinx.html

import kotlin.js.Date

impl fun currentTimeMillis(): Long = Date().getTime().toLong()
