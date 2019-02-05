/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

environments {
    production {
        grails.serverURL = "http://NOT_USED:8080/${appName}"
    }
    development {
        grails.serverURL = "http://NOT_USED:8080/${appName}"
    }
    test {
        grails.serverURL = "http://NOT_USED:8080/${appName}"
    }
}


hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.region.factory_class = 'org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory'
    //hbm2ddl.auto = null
    show_sql = false
    packagesToScan="net.hedtech.**.*"
    flush.mode = AUTO
    dialect = "org.hibernate.dialect.Oracle10gDialect"
    config.location = [
            "classpath:hibernate-banner-core.cfg.xml",
            "classpath:hibernate-banner-general-utility.cfg.xml"
    ]
}

grails.config.locations = [
        BANNER_APP_CONFIG: "banner_configuration.groovy"
]

webAppExtensibility {
    locations {
        resources     = "/Users/dharmarayudu/BanXE/sstext/i18n"
        extensions    = "/Users/dharmarayudu/BanXE/sstext/extensions"
    }
    adminRoles = "ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M"
}