package org.abondar.experimental.urlshortner.model

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object UrlMapping: LongIdTable("url_mapping") {
    var longUrl = varchar("long_url",2048)
    var shortUrl = varchar("short_url",50)
    var createdAt =   datetime("created_at")

}

