package org.abondar.experimental.urlshortner.config

import com.github.benmanes.caffeine.cache.Caffeine
import io.ktor.http.*
import org.koin.dsl.module
import java.util.concurrent.TimeUnit


//object DependencyConfig {
//    fun configDependencies() = module{
//        single {
//            Caffeine.newBuilder()
//                .expireAfterWrite(10, TimeUnit.MINUTES)
//                .maximumSize(10_000)
//                .build<String, String>()
//        }
//
//        single {
//            //TODO Shortner service
//        }
//}
